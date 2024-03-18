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

public class RowHandler {
    BuildBuffer buffer;

    public RowHandler(BuildBuffer buffer) {
        this.buffer = buffer;
    }

    public void handle(Row row) {
        XlsxCellAddress address = buffer.addressStack().peek();
        if (!(row.rowIndex().isEmpty() && row.startColumnIndex().isEmpty())) {
            buffer.addressStack().push(address.with(
                    new XlsxRowNumber(row.rowIndex()),
                    new XlsxColumnNumber(row.startColumnIndex())
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
                case LIST:
                default:
                    // TODO
                    throw new IllegalStateException();
            }
        }
    }
}
