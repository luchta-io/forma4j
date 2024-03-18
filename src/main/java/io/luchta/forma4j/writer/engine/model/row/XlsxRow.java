package io.luchta.forma4j.writer.engine.model.row;

import io.luchta.forma4j.writer.engine.model.cell.XlsxCell;
import io.luchta.forma4j.writer.engine.model.cell.XlsxCellList;
import io.luchta.forma4j.writer.engine.model.cell.address.XlsxColumnNumber;
import io.luchta.forma4j.writer.engine.model.cell.address.XlsxRowNumber;
import io.luchta.forma4j.writer.engine.model.column.XlsxColumnRange;
import io.luchta.forma4j.writer.engine.model.row.property.AutoFilterProperty;
import io.luchta.forma4j.writer.engine.model.row.property.XlsxRowProperties;
import io.luchta.forma4j.writer.engine.model.row.property.XlsxRowProperty;

public class XlsxRow {
    XlsxRowNumber rowNumber;
    XlsxCellList cells;
    XlsxRowProperties properties = new XlsxRowProperties();

    public XlsxRow(XlsxRowNumber rowNumber, XlsxCellList cells) {
        this.rowNumber = rowNumber;
        this.cells = cells;
    }

    public XlsxRow(XlsxRowNumber rowNumber, XlsxCellList cells, XlsxRowProperties properties) {
        this.rowNumber = rowNumber;
        this.cells = cells;
        this.properties = properties;
    }

    public XlsxRowNumber rowNumber() {
        return rowNumber;
    }

    public XlsxCellList cells() {
        return cells;
    }

    public boolean hasAutoFilter() {
        for (XlsxRowProperty property : properties) {
            if (property instanceof AutoFilterProperty) {
                return ((AutoFilterProperty) property).booleanValue();
            }
        }
        return false;
    }

    public XlsxColumnRange columnRange() {
        if (cells.size() == 0) {
            return new XlsxColumnRange();
        }

        Long firstColumnNumber = Long.MAX_VALUE;
        Long lastColumnNumber = Long.MIN_VALUE;
        for (XlsxCell cell : cells) {
            if (firstColumnNumber >= cell.columnNumber().toLong()) {
                firstColumnNumber = cell.columnNumber().toLong();
            }
            if (lastColumnNumber <= cell.columnNumber().toLong()) {
                lastColumnNumber = cell.columnNumber().toLong();
            }
        }
        return new XlsxColumnRange(new XlsxColumnNumber(firstColumnNumber), new XlsxColumnNumber(lastColumnNumber));
    }
}
