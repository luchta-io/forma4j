package io.luchta.forma4j.writer.engine.model.sheet;

import io.luchta.forma4j.writer.engine.buffer.accumulater.support.ColumnPropertyMap;
import io.luchta.forma4j.writer.engine.model.cell.XlsxCell;
import io.luchta.forma4j.writer.engine.model.cell.address.XlsxSheetName;
import io.luchta.forma4j.writer.engine.model.row.XlsxRow;
import io.luchta.forma4j.writer.engine.model.row.XlsxRowList;

import java.util.HashSet;
import java.util.Set;

public class XlsxSheet {
    XlsxSheetName name;
    XlsxRowList rows;
    ColumnPropertyMap columnPropertyMap;
    Boolean autoSizeColumnEnabled;
    int columnSize;
    Set<Integer> nonEmptyColumnNumbers;

    public XlsxSheet(XlsxSheetName name, XlsxRowList rows, ColumnPropertyMap columnPropertyMap) {
        this(name, rows, columnPropertyMap, null);
    }

    public XlsxSheet(XlsxSheetName name, XlsxRowList rows, ColumnPropertyMap columnPropertyMap, Boolean autoSizeColumnEnabled) {
        this.name = name;
        this.rows = rows;
        this.columnPropertyMap = columnPropertyMap;
        this.autoSizeColumnEnabled = autoSizeColumnEnabled;
        this.columnSize = columnSize(rows);
        this.nonEmptyColumnNumbers = nonEmptyColumnNumbers(rows);
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

    public Boolean autoSizeColumnEnabled() {
        return autoSizeColumnEnabled;
    }

    public int columnSize() {
        return columnSize;
    }

    public boolean isEmptyColumn(int index) {
        return !nonEmptyColumnNumbers.contains(index);
    }

    private int columnSize(XlsxRowList rows) {
        int maxColumnNumber = -1;
        for (XlsxRow row : rows) {
            for (XlsxCell cell : row.cells()) {
                maxColumnNumber = Math.max(maxColumnNumber, cell.columnNumber().toInt());
            }
        }
        return maxColumnNumber + 1;
    }

    private Set<Integer> nonEmptyColumnNumbers(XlsxRowList rows) {
        Set<Integer> columnNumbers = new HashSet<>();
        for (XlsxRow row : rows) {
            for (XlsxCell cell : row.cells()) {
                if (!cell.isEmpty()) {
                    columnNumbers.add(cell.columnNumber().toInt());
                }
            }
        }
        return columnNumbers;
    }
}
