package io.luchta.forma4j.writer.engine.handler.element;

import io.luchta.forma4j.writer.definition.schema.Element;
import io.luchta.forma4j.writer.definition.schema.ElementList;
import io.luchta.forma4j.writer.definition.schema.element.Cell;
import io.luchta.forma4j.writer.definition.schema.element.Column;
import io.luchta.forma4j.writer.definition.schema.element.HorizontalFor;
import io.luchta.forma4j.writer.engine.buffer.BuildBuffer;
import io.luchta.forma4j.writer.engine.model.cell.address.XlsxCellAddress;
import io.luchta.forma4j.writer.engine.model.cell.address.XlsxColumnNumber;
import io.luchta.forma4j.writer.engine.model.cell.address.XlsxRowNumber;
import io.luchta.forma4j.writer.engine.resolver.VariableResolver;

import java.util.List;

/**
 * horizontal-forタグのハンドラ
 */
public class HorizontalForHandler {
    BuildBuffer buffer;

    /**
     * コンストラクタ
     * @param buffer
     */
    public HorizontalForHandler(BuildBuffer buffer) {
        this.buffer = buffer;
    }

    /**
     * ハンドル
     * <p>
     * horizontal-forタグの定義内容を読み取り書き込み定義を作成する
     * </p>
     * @param horizontalFor
     */
    public void handle(HorizontalFor horizontalFor) {
        VariableResolver variableResolver = buffer.variableResolver();
        List<Object> collection = variableResolver.getList(horizontalFor.collection().toString());
        XlsxCellAddress baseAddress = buffer.addressStack().peek();
        XlsxRowNumber startRowNumber = horizontalFor.startRowIndex().isEmpty()
                ? baseAddress.rowNumber()
                : new XlsxRowNumber(horizontalFor.startRowIndex());
        XlsxColumnNumber startColumnNumber = horizontalFor.startColumnIndex().isEmpty()
                ? baseAddress.columnNumber()
                : new XlsxColumnNumber(horizontalFor.startColumnIndex());
        try {
            for (int i = 0; i < collection.size(); i++) {
                XlsxCellAddress currentAddress = baseAddress.with(
                        startRowNumber,
                        new XlsxColumnNumber(startColumnNumber.toLong() + i)
                );
                buffer.addressStack().push(currentAddress);
                buffer.loopContext().put(horizontalFor.index(), i);
                buffer.loopContext().put(horizontalFor.item(), collection.get(i));
                try {
                    dispatch(horizontalFor.children());
                } finally {
                    buffer.addressStack().pop();
                }
            }
        } finally {
            buffer.loopContext().remove(horizontalFor.index());
            buffer.loopContext().remove(horizontalFor.item());
        }
    }

    private void dispatch(ElementList children) {
        for (Element element : children) {
            switch (element.type()) {
                case CELL:
                    new CellHandler(buffer).handle((Cell) element);
                    break;
                case COLUMN:
                    new ColumnHandler(buffer).handle((Column) element);
                    break;
                case VERTICAL_FOR:
                case HORIZONTAL_FOR:
                case ROW:
                case SHEET:
                case LIST:
                default:
                    // TODO
                    throw new IllegalStateException();
            }
        }
    }
}
