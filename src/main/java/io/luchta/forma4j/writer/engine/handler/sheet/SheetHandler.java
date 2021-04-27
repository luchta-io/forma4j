package io.luchta.forma4j.writer.engine.handler.sheet;

import io.luchta.forma4j.writer.definition.schema.element.*;
import io.luchta.forma4j.writer.engine.handler.column.ColumnHandler;
import io.luchta.forma4j.writer.engine.handler.verticalfor.VerticalForHandler;
import io.luchta.forma4j.writer.definition.schema.Element;
import io.luchta.forma4j.writer.definition.schema.ElementList;
import io.luchta.forma4j.writer.engine.buffer.BuildBuffer;
import io.luchta.forma4j.writer.engine.handler.cell.CellHandler;
import io.luchta.forma4j.writer.engine.handler.horizontalfor.HorizontalForHandler;
import io.luchta.forma4j.writer.engine.handler.row.RowHandler;
import io.luchta.forma4j.writer.engine.model.cell.address.XlsxCellAddress;
import io.luchta.forma4j.writer.engine.model.cell.address.XlsxColumnNumber;
import io.luchta.forma4j.writer.engine.model.cell.address.XlsxRowNumber;
import io.luchta.forma4j.writer.engine.model.cell.address.XlsxSheetName;

public class SheetHandler {
    BuildBuffer buffer;

    public SheetHandler(BuildBuffer buffer) {
        this.buffer = buffer;
    }

    public void handle(Sheet sheet) {
        XlsxSheetName sheetName = new XlsxSheetName(sheet.name());
        XlsxCellAddress address = new XlsxCellAddress(sheetName, XlsxRowNumber.init(), XlsxColumnNumber.init());
        buffer.accumulator().add(sheetName);
        buffer.addressStack().push(address);
        dispatch(sheet.children());
    }

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
                case SHEET:
                default:
                    // TODO
                    throw new IllegalStateException();
            }
        }
    }
}
