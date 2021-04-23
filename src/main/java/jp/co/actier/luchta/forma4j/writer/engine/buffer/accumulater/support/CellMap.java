package jp.co.actier.luchta.forma4j.writer.engine.buffer.accumulater.support;

import jp.co.actier.luchta.forma4j.writer.engine.model.cell.XlsxCell;
import jp.co.actier.luchta.forma4j.writer.engine.model.cell.XlsxCellList;
import jp.co.actier.luchta.forma4j.writer.engine.model.cell.address.XlsxCellAddress;
import jp.co.actier.luchta.forma4j.writer.engine.model.cell.address.XlsxRowNumber;
import jp.co.actier.luchta.forma4j.writer.engine.model.cell.address.XlsxSheetName;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CellMap {
    Map<XlsxCellAddress, XlsxCell> map = new HashMap<>();

    public CellMap() {
    }

    public CellMap(Map<XlsxCellAddress, XlsxCell> map) {
        this.map = map;
    }

    public void put(XlsxCellAddress address, XlsxCell cell) {
        map.put(address, cell);
    }

    public CellMap filterBy(XlsxSheetName sheetName) {
        Map<XlsxCellAddress, XlsxCell> filtered = map.entrySet().stream()
                .filter(entry -> entry.getKey().is(sheetName))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        return new CellMap(filtered);
    }

    public CellMap filterBy(XlsxRowNumber rowNumber) {
        Map<XlsxCellAddress, XlsxCell> filtered = map.entrySet().stream()
                .filter(entry -> entry.getKey().is(rowNumber))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        return new CellMap(filtered);
    }

    public RowNumberList rowNumberList() {
        List<XlsxRowNumber> sorted = map.keySet().stream()
                .map(XlsxCellAddress::rowNumber)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
        return new RowNumberList(sorted);
    }

    public XlsxCellList toXlsxCellList() {
        List<XlsxCell> sorted = map.values().stream()
                .sorted(Comparator.comparing(o -> o.address().columnNumber()))
                .collect(Collectors.toList());
        return new XlsxCellList(sorted);
    }
}
