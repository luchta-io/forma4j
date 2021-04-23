package jp.co.actier.luchta.forma4j.writer.engine.handler.row;

import jp.co.actier.luchta.forma4j.writer.definition.schema.Element;
import jp.co.actier.luchta.forma4j.writer.definition.schema.ElementList;
import jp.co.actier.luchta.forma4j.writer.definition.schema.element.Cell;
import jp.co.actier.luchta.forma4j.writer.definition.schema.element.VerticalFor;
import jp.co.actier.luchta.forma4j.writer.definition.schema.element.Row;
import jp.co.actier.luchta.forma4j.writer.definition.schema.element.HorizontalFor;
import jp.co.actier.luchta.forma4j.writer.engine.buffer.BuildBuffer;
import jp.co.actier.luchta.forma4j.writer.engine.handler.cell.CellHandler;
import jp.co.actier.luchta.forma4j.writer.engine.handler.horizontalfor.HorizontalForHandler;
import jp.co.actier.luchta.forma4j.writer.engine.handler.verticalfor.VerticalForHandler;
import jp.co.actier.luchta.forma4j.writer.engine.model.cell.address.XlsxCellAddress;
import jp.co.actier.luchta.forma4j.writer.engine.model.cell.address.XlsxColumnNumber;
import jp.co.actier.luchta.forma4j.writer.engine.model.cell.address.XlsxRowNumber;

public class RowHandler {
    BuildBuffer buffer;

    public RowHandler(BuildBuffer buffer) {
        this.buffer = buffer;
    }

    public void handle(Row row) {
        XlsxCellAddress address = buffer.addressStack().peek();
        if (!(row.rowIndex().isEmpty() && row.startColumnIndex().isEmpty()))
            buffer.addressStack().push(address.with(
                    new XlsxRowNumber(row.rowIndex()),
                    new XlsxColumnNumber(row.startColumnIndex())
            ));
        dispatch(row.children());
    }

    private void dispatch(ElementList children) {
        for (Element element : children) {
            switch (element.type()) {
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
                case ROW:
                case COLUMN:
                case SHEET:
                default:
                    // TODO
                    throw new IllegalStateException();
            }
        }
    }
}
