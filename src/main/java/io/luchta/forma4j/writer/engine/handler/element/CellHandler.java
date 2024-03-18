package io.luchta.forma4j.writer.engine.handler.element;

import io.luchta.forma4j.writer.definition.schema.attribute.Name;
import io.luchta.forma4j.writer.definition.schema.element.Cell;
import io.luchta.forma4j.writer.engine.buffer.BuildBuffer;
import io.luchta.forma4j.writer.engine.model.cell.XlsxCell;
import io.luchta.forma4j.writer.engine.model.cell.address.XlsxCellAddress;
import io.luchta.forma4j.writer.engine.model.cell.address.XlsxColumnNumber;
import io.luchta.forma4j.writer.engine.model.cell.address.XlsxSheetName;
import io.luchta.forma4j.writer.engine.model.column.XlsxColumnAddress;
import io.luchta.forma4j.writer.engine.model.column.property.XlsxColumnProperties;
import io.luchta.forma4j.writer.engine.model.column.property.XlsxColumnProperty;
import io.luchta.forma4j.writer.engine.model.column.property.XlsxColumnStyle;
import io.luchta.forma4j.writer.engine.resolver.StyleResolver;
import io.luchta.forma4j.writer.engine.model.cell.value.Text;
import io.luchta.forma4j.writer.engine.model.cell.value.XlsxCellValue;

public class CellHandler {
    BuildBuffer buffer;

    public CellHandler(BuildBuffer buffer) {
        this.buffer = buffer;
    }

    public void handle(Cell cell) {
        XlsxCellAddress address = buffer.addressStack().peek();
		StyleResolver styleResolver = new StyleResolver();

        XlsxCell xlsxCell = new XlsxCell(address, value(cell), styleResolver.get(cell.style()));

        XlsxColumnAddress columnAddress = new XlsxColumnAddress(
                new XlsxSheetName(new Name(address.sheetName().toString())),
                new XlsxColumnNumber(address.columnNumber().toLong()));
        XlsxColumnProperties columnProperties = styleResolver.getColumnProperties(cell.style());

        buffer.accumulator().put(address, xlsxCell);
        for (XlsxColumnProperty columnProperty : columnProperties) {
            buffer.accumulator().putColumnProperties(columnAddress, columnProperty);
        }
        buffer.addressStack().push(address.columnNumberIncrement());
    }

    private XlsxCellValue value(Cell cell) {
        return cell.text().isVariable() ?
                buffer.variableResolver().get(cell.text().stripMarker()) :
                new Text(cell.text().toString());
    }
}
