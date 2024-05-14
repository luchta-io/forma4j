package io.luchta.forma4j.writer.engine.handler.element;

import io.luchta.forma4j.writer.definition.schema.Element;
import io.luchta.forma4j.writer.definition.schema.ElementList;
import io.luchta.forma4j.writer.definition.schema.element.Cell;
import io.luchta.forma4j.writer.definition.schema.element.Column;
import io.luchta.forma4j.writer.definition.schema.element.HorizontalFor;
import io.luchta.forma4j.writer.definition.schema.element.VerticalFor;
import io.luchta.forma4j.writer.engine.buffer.BuildBuffer;
import io.luchta.forma4j.writer.engine.model.cell.address.XlsxCellAddress;
import io.luchta.forma4j.writer.engine.model.cell.address.XlsxColumnNumber;
import io.luchta.forma4j.writer.engine.model.cell.address.XlsxRowNumber;

/**
 * Columnタグのハンドルクラス
 */
public class ColumnHandler {
    BuildBuffer buffer;

    /**
     * コンストラクタ
     * @param buffer バッファ
     */
    public ColumnHandler(BuildBuffer buffer) {
        this.buffer = buffer;
    }

    /**
     * ハンドル
     * <p>
     * Columnタグの定義内容を読み取り書き込み定義を作成する
     * </p>
     * @param column
     */
    public void handle(Column column) {
        XlsxCellAddress address = buffer.addressStack().peek();
        if (!column.startRowIndex().isEmpty() || !column.columnIndex().isEmpty()) {
            buffer.addressStack().push(address.with(
                    new XlsxRowNumber(column.startRowIndex().value()),
                    new XlsxColumnNumber(column.columnIndex())
            ));
        }
        dispatch(column.children());
    }

    private void dispatch(ElementList children) {
        boolean isNotFirst = false;
        for (Element element : children) {
            switch (element.type()) {
                case CELL:
                    if (isNotFirst) {
                        buffer.addressStack().push(buffer.addressStack().peek().rowNumberIncrement());
                    }
                    isNotFirst = true;
                    new CellHandler(buffer).handle((Cell) element);
                    break;
                case HORIZONTAL_FOR:
                    new HorizontalForHandler(buffer).handle((HorizontalFor) element);
                    break;
                case VERTICAL_FOR:
                    new VerticalForHandler(buffer).handle((VerticalFor) element);
                    break;
                case ROW:
                case COLUMN:
                case SHEET:
                case LIST:
                default:
                    // TODO
                    throw new IllegalStateException();
            }
        }
    }
}
