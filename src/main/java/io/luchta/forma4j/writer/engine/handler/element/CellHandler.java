package io.luchta.forma4j.writer.engine.handler.element;

import io.luchta.forma4j.writer.definition.schema.element.Cell;
import io.luchta.forma4j.writer.engine.buffer.BuildBuffer;
import io.luchta.forma4j.writer.engine.model.cell.XlsxCell;
import io.luchta.forma4j.writer.engine.model.cell.address.XlsxCellAddress;
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
        buffer.accumulator().put(address, xlsxCell);
        buffer.addressStack().push(address.columnNumberIncrement());
    }

    private XlsxCellValue value(Cell cell) {
        return cell.text().isVariable() ?
                buffer.variableResolver().get(cell.text().stripMarker()) :
                new Text(cell.text().toString());
    }
}
