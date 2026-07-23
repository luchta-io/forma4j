package io.luchta.forma4j.writer.engine.handler.element;

import io.luchta.forma4j.writer.definition.schema.Element;
import io.luchta.forma4j.writer.definition.schema.ElementList;
import io.luchta.forma4j.writer.definition.schema.ElementType;
import io.luchta.forma4j.writer.definition.schema.attribute.Name;
import io.luchta.forma4j.writer.definition.schema.attribute.loop.Collection;
import io.luchta.forma4j.writer.definition.schema.attribute.loop.Index;
import io.luchta.forma4j.writer.definition.schema.attribute.loop.Item;
import io.luchta.forma4j.writer.definition.schema.element.*;
import io.luchta.forma4j.writer.engine.buffer.BuildBuffer;
import io.luchta.forma4j.writer.engine.model.cell.address.XlsxCellAddress;
import io.luchta.forma4j.writer.engine.model.cell.address.XlsxColumnNumber;
import io.luchta.forma4j.writer.engine.model.cell.address.XlsxRowNumber;
import io.luchta.forma4j.writer.engine.model.cell.address.XlsxSheetName;
import io.luchta.forma4j.writer.engine.resolver.VariableResolver;

import java.util.Map;

/**
 * シートハンドラ
 */
public class SheetHandler {
    /** バッファ */
    private BuildBuffer buffer;

    /**
     * コンストラクタ
     * @param buffer
     */
    public SheetHandler(BuildBuffer buffer) {
        this.buffer = buffer;
    }

    /**
     * ハンドル
     * <p>
     * collectionを指定したsheetタグの子要素がlistタグの場合、JSONのフォーマットが異なるため処理が変わる
     * </p>
     * @param sheet
     */
    public void handle(Sheet sheet) {
        Collection collection = sheet.collection();
        Boolean autoSizeColumnEnabled = autoSizeColumnEnabled(sheet);
        if (collection.isEmpty()) {
            XlsxSheetName sheetName = new XlsxSheetName(sheet.name());
            XlsxCellAddress address = new XlsxCellAddress(sheetName, XlsxRowNumber.init(), XlsxColumnNumber.init());
            buffer.outputStrategy().startSheet(sheetName, autoSizeColumnEnabled);
            buffer.addressStack().push(address);
            try {
                dispatch(sheet.children());
            } finally {
                buffer.addressStack().pop();
                buffer.outputStrategy().finishSheet(sheetName);
            }
        } else {
            if (sheet.children().size() == 1 && sheet.children().get(0).type() == ElementType.LIST) {
                handleCollectionWithListTag(sheet, collection);
            } else {
                handleCollection(sheet, collection);
            }
        }
    }

    /**
     * collectionプロパティ設定時の処理
     * @param sheet
     * @param collection
     */
    private void handleCollection(Sheet sheet, Collection collection) {
        try (VariableResolver.Iteration list =
                     buffer.variableResolver().openIteration(collection.toString())) {
            while (list.hasNext()) {
                Object obj = list.next();
                if (obj instanceof Map) {
                    // シートの設定
                    Map<String, Object> map = (Map<String, Object>) obj;
                    XlsxSheetName sheetName = new XlsxSheetName(new Name(map.get("sheetName").toString()));
                    XlsxCellAddress address = new XlsxCellAddress(sheetName, XlsxRowNumber.init(), XlsxColumnNumber.init());
                    buffer.outputStrategy().startSheet(sheetName, autoSizeColumnEnabled(sheet));
                    buffer.addressStack().push(address);
                    buffer.loopContext().put(new Index(), 0);
                    buffer.loopContext().put(new Item(sheet.item().toString()), map);
                    try {
                        dispatch(sheet.children());
                    } finally {
                        buffer.addressStack().pop();
                        buffer.outputStrategy().finishSheet(sheetName);
                    }
                }
            }
        } finally {
            buffer.loopContext().remove(new Index());
            buffer.loopContext().remove(new Item(sheet.item().toString()));
        }
    }

    /**
     * collectionプロパティ設定時かつlistタグを使用しているときの処理
     * @param sheet
     * @param collection
     */
    private void handleCollectionWithListTag(Sheet sheet, Collection collection) {
        try (VariableResolver.Iteration list =
                     buffer.variableResolver().openIteration(collection.toString())) {
            while (list.hasNext()) {
                Object obj = list.next();
                if (obj instanceof Map) {
                    Map<String, Object> sheets = (Map<String, Object>) obj;
                    for (Map.Entry<String, Object> entry : sheets.entrySet()) {
                        XlsxSheetName sheetName = new XlsxSheetName(new Name(entry.getKey()));
                        XlsxCellAddress address = new XlsxCellAddress(sheetName, XlsxRowNumber.init(), XlsxColumnNumber.init());
                        buffer.outputStrategy().startSheet(sheetName, autoSizeColumnEnabled(sheet));
                        buffer.addressStack().push(address);
                        buffer.loopContext().put(new Index(), 0);
                        buffer.loopContext().put(new Item(sheet.item().toString()), entry.getValue());
                        try {
                            dispatch(sheet.children());
                        } finally {
                            buffer.addressStack().pop();
                            buffer.outputStrategy().finishSheet(sheetName);
                        }
                    }
                }
            }
        } finally {
            buffer.loopContext().remove(new Index());
            buffer.loopContext().remove(new Item(sheet.item().toString()));
        }
    }

    private Boolean autoSizeColumnEnabled(Sheet sheet) {
        if (sheet.autoSizeColumn().isEmpty()) {
            return null;
        }

        String value = sheet.autoSizeColumn().value().toLowerCase();
        if ("true".equals(value)) {
            return true;
        }
        if ("false".equals(value)) {
            return false;
        }
        return null;
    }

    /**
     * 子要素へ処理を割り振る
     * @param children
     */
    private void dispatch(ElementList children) {
        for (Element element : children) {
            switch (element.type()) {
                case ROW:
                    new RowHandler(buffer)
                            .handle((Row) element);
                    break;
                case COLUMN:
                    new ColumnHandler(buffer)
                            .handle((Column) element);
                    break;
                case CELL:
                    new CellHandler(buffer)
                            .handle((Cell) element);
                    break;
                case HORIZONTAL_FOR:
                    new HorizontalForHandler(buffer)
                            .handle((HorizontalFor) element);
                    break;
                case VERTICAL_FOR:
                    new VerticalForHandler(buffer)
                            .handle((VerticalFor) element);
                    break;
                case LIST:
                    new ListHandler(buffer)
                            .handle((ListElement) element);
                    break;
                case SHEET:
                default:
                    // TODO
                    throw new IllegalStateException();
            }
        }
    }
}
