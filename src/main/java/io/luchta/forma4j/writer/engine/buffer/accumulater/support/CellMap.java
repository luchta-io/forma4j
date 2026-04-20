package io.luchta.forma4j.writer.engine.buffer.accumulater.support;

import io.luchta.forma4j.writer.engine.model.cell.XlsxCell;
import io.luchta.forma4j.writer.engine.model.cell.XlsxCellList;
import io.luchta.forma4j.writer.engine.model.cell.address.XlsxCellAddress;
import io.luchta.forma4j.writer.engine.model.cell.address.XlsxColumnNumber;
import io.luchta.forma4j.writer.engine.model.cell.address.XlsxRowNumber;
import io.luchta.forma4j.writer.engine.model.cell.address.XlsxSheetName;
import io.luchta.forma4j.writer.engine.model.cell.style.XlsxCellStyle;
import io.luchta.forma4j.writer.engine.model.cell.style.XlsxCellStyles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class CellMap {
    Map<XlsxSheetName, Map<XlsxRowNumber, Map<XlsxColumnNumber, XlsxCell>>> sheetRowColumnMap = new HashMap<>();

    public void put(XlsxCellAddress address, XlsxCell cell) {
        Map<XlsxRowNumber, Map<XlsxColumnNumber, XlsxCell>> rowMap = sheetRowColumnMap.computeIfAbsent(
                address.sheetName(),
                key -> new TreeMap<>()
        );
        Map<XlsxColumnNumber, XlsxCell> columnMap = rowMap.computeIfAbsent(
                address.rowNumber(),
                key -> new TreeMap<>()
        );
        columnMap.put(address.columnNumber(), cell);
    }

    public Map<XlsxRowNumber, Map<XlsxColumnNumber, XlsxCell>> rows(XlsxSheetName sheetName) {
        if (!sheetRowColumnMap.containsKey(sheetName)) {
            return Collections.emptyMap();
        }
        return sheetRowColumnMap.get(sheetName);
    }

    public Iterable<XlsxCell> cells(XlsxSheetName sheetName, XlsxRowNumber rowNumber) {
        Map<XlsxRowNumber, Map<XlsxColumnNumber, XlsxCell>> rowMap = rows(sheetName);
        if (!rowMap.containsKey(rowNumber)) {
            return Collections.emptyList();
        }
        return rowMap.get(rowNumber).values();
    }

    public XlsxCellList toXlsxCellList(Map<XlsxColumnNumber, XlsxCell> rowCells) {
        List<XlsxCell> cells = new ArrayList<>(rowCells.values());
        return new XlsxCellList(cells);
    }

    public XlsxCellStyles toXlsxCellStyles() {
        Set<XlsxCellStyle> styles = new HashSet<>();
        for (Map<XlsxRowNumber, Map<XlsxColumnNumber, XlsxCell>> rowMap : sheetRowColumnMap.values()) {
            for (Map<XlsxColumnNumber, XlsxCell> columnMap : rowMap.values()) {
                for (XlsxCell cell : columnMap.values()) {
                    styles.add(cell.style());
                }
            }
        }
        return new XlsxCellStyles(styles);
    }
}
