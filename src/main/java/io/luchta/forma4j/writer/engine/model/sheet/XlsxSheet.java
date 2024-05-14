package io.luchta.forma4j.writer.engine.model.sheet;

import io.luchta.forma4j.writer.engine.buffer.accumulater.support.ColumnPropertyMap;
import io.luchta.forma4j.writer.engine.model.cell.XlsxCell;
import io.luchta.forma4j.writer.engine.model.cell.XlsxCellList;
import io.luchta.forma4j.writer.engine.model.column.property.XlsxColumnProperties;
import io.luchta.forma4j.writer.engine.model.row.XlsxRow;
import io.luchta.forma4j.writer.engine.model.row.XlsxRowList;
import io.luchta.forma4j.writer.engine.model.cell.address.XlsxSheetName;

public class XlsxSheet {
    XlsxSheetName name;
    XlsxRowList rows;
    ColumnPropertyMap columnPropertyMap;

    public XlsxSheet(XlsxSheetName name, XlsxRowList rows, ColumnPropertyMap columnPropertyMap) {
        this.name = name;
        this.rows = rows;
        this.columnPropertyMap = columnPropertyMap;
    }

    public XlsxSheetName name() {
        return name;
    }

    public XlsxRowList rows() {
        return rows;
    }

    public ColumnPropertyMap columnPropertyMap() {
        return columnPropertyMap;
    }

    public int columnSize() {
        XlsxRow longestRow = rows.getLongest();
        return longestRow.cells().size();
    }

    public boolean isEmptyColumn(int index) {
        for (XlsxRow row : rows) {
            XlsxCellList cells = row.cells();
            for (XlsxCell cell : cells) {
                if (cell.columnNumber().toInt() == index && cell.isEmpty()) {
                    return true;
                }
            }
        }
        return false;
    }
}
