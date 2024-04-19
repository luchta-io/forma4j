package io.luchta.forma4j.writer.engine.handler.element;

import io.luchta.forma4j.writer.definition.schema.Element;
import io.luchta.forma4j.writer.definition.schema.ElementList;
import io.luchta.forma4j.writer.definition.schema.element.Cell;
import io.luchta.forma4j.writer.definition.schema.element.HorizontalFor;
import io.luchta.forma4j.writer.definition.schema.element.Row;
import io.luchta.forma4j.writer.definition.schema.element.VerticalFor;
import io.luchta.forma4j.writer.engine.buffer.BuildBuffer;
import io.luchta.forma4j.writer.engine.model.cell.address.XlsxCellAddress;
import io.luchta.forma4j.writer.engine.model.cell.address.XlsxColumnNumber;
import io.luchta.forma4j.writer.engine.model.cell.address.XlsxRowNumber;
import io.luchta.forma4j.writer.engine.model.row.address.XlsxRowAddress;
import io.luchta.forma4j.writer.engine.model.row.property.AutoFilterProperty;

/**
 * Rowタグのハンドラクラス
 */
public class RowHandler {
    BuildBuffer buffer;

    /**
     * コンストラクタ
     * @param buffer
     */
    public RowHandler(BuildBuffer buffer) {
        this.buffer = buffer;
    }

    /**
     * ハンドル
     * <p>
     * Rowタグの定義内容を読み取り書き込み定義を作成する
     * </p>
     * @param row
     */
    public void handle(Row row) {
        XlsxCellAddress address = buffer.addressStack().peek();
        if (!(row.rowIndex().isEmpty() && row.startColumnIndex().isEmpty())) {
            buffer.addressStack().push(address.with(
                    new XlsxRowNumber(row.rowIndex()),
                    new XlsxColumnNumber(row.startColumnIndex().value() - 1)
            ));
            buffer.accumulator().putRowProperty(
                    new XlsxRowAddress(address.sheetName(), new XlsxRowNumber(row.rowIndex())),
                    AutoFilterProperty.create(row.autoFilter().value()));
        }
        dispatch(row.children());
    }

    private void dispatch(ElementList children) {
        for (Element element : children) {
            switch (element.type()) {
                case CELL:
                    buffer.addressStack().push(buffer.addressStack().peek().columnNumberIncrement());
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
