package jp.co.actier.luchta.forma4j.writer.engine.handler.sheet;

import jp.co.actier.luchta.forma4j.writer.definition.schema.Element;
import jp.co.actier.luchta.forma4j.writer.definition.schema.ElementList;
import jp.co.actier.luchta.forma4j.writer.definition.schema.element.*;
import jp.co.actier.luchta.forma4j.writer.engine.buffer.BuildBuffer;
import jp.co.actier.luchta.forma4j.writer.engine.handler.cell.CellHandler;
import jp.co.actier.luchta.forma4j.writer.engine.handler.column.ColumnHandler;
import jp.co.actier.luchta.forma4j.writer.engine.handler.horizontalfor.HorizontalForHandler;
import jp.co.actier.luchta.forma4j.writer.engine.handler.row.RowHandler;
import jp.co.actier.luchta.forma4j.writer.engine.handler.verticalfor.VerticalForHandler;
import jp.co.actier.luchta.forma4j.writer.engine.model.cell.address.XlsxCellAddress;
import jp.co.actier.luchta.forma4j.writer.engine.model.cell.address.XlsxColumnNumber;
import jp.co.actier.luchta.forma4j.writer.engine.model.cell.address.XlsxRowNumber;
import jp.co.actier.luchta.forma4j.writer.engine.model.cell.address.XlsxSheetName;

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
