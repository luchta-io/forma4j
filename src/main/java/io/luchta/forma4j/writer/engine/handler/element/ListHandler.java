package io.luchta.forma4j.writer.engine.handler.element;

import io.luchta.forma4j.writer.definition.schema.attribute.Style;
import io.luchta.forma4j.writer.definition.schema.attribute.index.ColumnIndex;
import io.luchta.forma4j.writer.definition.schema.attribute.index.RowIndex;
import io.luchta.forma4j.writer.definition.schema.attribute.loop.Collection;
import io.luchta.forma4j.writer.definition.schema.element.ListElement;
import io.luchta.forma4j.writer.engine.buffer.BuildBuffer;
import io.luchta.forma4j.writer.engine.model.cell.XlsxCell;
import io.luchta.forma4j.writer.engine.model.cell.address.XlsxCellAddress;
import io.luchta.forma4j.writer.engine.model.cell.address.XlsxColumnNumber;
import io.luchta.forma4j.writer.engine.model.cell.address.XlsxRowNumber;
import io.luchta.forma4j.writer.engine.model.cell.value.Text;
import io.luchta.forma4j.writer.engine.resolver.StyleResolver;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * {@code ListHandler} は list タグのハンドラです
 * <p>
 * list タグの定義に従って EXCEL へ書き込む内容を {@link BuildBuffer } へセットします。
 * </p>
 *
 * @since 1.1.0
 */
public class ListHandler {
    private BuildBuffer buffer;

    /**
     * コンストラクタ
     * @param buffer バッファ
     */
    public ListHandler(BuildBuffer buffer) {
        this.buffer = buffer;
    }

    /**
     * list タグのハンドリングを行うメソッドです
     * <p>
     * list タグのプロパティを読み取り EXCEL への書き込み指示データを {@link BuildBuffer} にセットします。
     * </p>
     * <p>
     * collection プロパティに設定された値をキーとして JSON から値を取得します。未設定の場合は、sheet タグの最初の子要素をキーとします。JSON から値を取得できない場合は何も処理を行わずに終了します。
     * </p>
     * <p>
     * JSON から値を取得できたとき、最初の要素のキーが一覧表のヘッダとして EXCEL に出力されます。
     * </p>
     * @param listElement list タグ
     */
    public void handle(ListElement listElement) {
        Long rowIndex = startRowIndex(listElement);
        Long columnIndex = startColumnIndex(listElement);
        Style headerStyle = listElement.headerStyle();
        Style detailStyle = listElement.detailStyle();
        Collection collection = listElement.collection();

        header(address(new XlsxRowNumber(rowIndex), new XlsxColumnNumber(columnIndex)), headerStyle, collection);
        detail(address(new XlsxRowNumber(rowIndex + 1), new XlsxColumnNumber(columnIndex)), detailStyle, collection);
    }

    private void header(XlsxCellAddress address, Style style, Collection collection) {
        Set<String> keySet = buffer.variableResolver().getKeySet();
        String key = keySet.iterator().next();
        if (!collection.isEmpty()) {
            key = collection.toString();
        }
        List<Object> list = buffer.variableResolver().getList(key);
        if (list.size() == 0 || !(list.get(0) instanceof Map<?, ?>)) {
            return;
        }

        StyleResolver styleResolver = new StyleResolver();
        Map<String, Object> map = (Map<String, Object>) list.get(0);
        for (String headerName : map.keySet()) {
            buffer.accumulator().put(
                    address,
                    new XlsxCell(address, new Text(headerName), styleResolver.get(style, buffer.variableResolver())));
            address = address.columnNumberIncrement();
        }
    }

    private void detail(XlsxCellAddress address, Style style, Collection collection) {
        Set<String> keySet = buffer.variableResolver().getKeySet();
        String key = keySet.iterator().next();
        if (!collection.isEmpty()) {
            key = collection.toString();
        }
        List<Object> list = buffer.variableResolver().getList(key);
        if (list.size() == 0 || !(list.get(0) instanceof Map<?, ?>)) {
            return;
        }

        StyleResolver styleResolver = new StyleResolver();
        Long columnIndex = address.columnNumber().toLong();
        for (Object obj : list) {
            if (!(obj instanceof Map<?, ?>)) {
                continue;
            }

            Map<String, Object> line = (Map<String, Object>) obj;
            for (Object value : line.values()) {
                buffer.accumulator().put(
                        address,
                        new XlsxCell(address, new Text(value.toString()), styleResolver.get(style, buffer.variableResolver())));
                address = address.columnNumberIncrement();
            }

            address = address.rowNumberIncrement().with(new XlsxColumnNumber(columnIndex));
        }
    }

    private Long startRowIndex(ListElement listElement) {
        RowIndex startRowIndex = listElement.startRowIndex();
        Long rowIndex = 0L;
        if (!startRowIndex.isEmpty()) {
            rowIndex = startRowIndex.value();
        }
        return rowIndex;
    }

    private Long startColumnIndex(ListElement listElement) {
        ColumnIndex startColumnIndex = listElement.startColumnIndex();
        Long columnIndex = 0L;
        if (!startColumnIndex.isEmpty()) {
            columnIndex = startColumnIndex.value();
        }
        return columnIndex;
    }

    private XlsxCellAddress address(XlsxRowNumber rowNumber, XlsxColumnNumber columnNumber) {
        XlsxCellAddress address = buffer.addressStack().peek();
        return address.with(rowNumber).with(columnNumber);
    }
}
