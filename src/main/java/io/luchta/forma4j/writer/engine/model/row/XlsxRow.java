package io.luchta.forma4j.writer.engine.model.row;

import io.luchta.forma4j.writer.engine.model.cell.XlsxCellList;
import io.luchta.forma4j.writer.engine.model.cell.address.XlsxRowNumber;

public class XlsxRow {
    XlsxRowNumber rowNumber;
    XlsxCellList cells;

    public XlsxRow(XlsxRowNumber rowNumber, XlsxCellList cells) {
        this.rowNumber = rowNumber;
        this.cells = cells;
    }

    public XlsxRowNumber rowNumber() {
        return rowNumber;
    }

    public XlsxCellList cells() {
        return cells;
    }
}
