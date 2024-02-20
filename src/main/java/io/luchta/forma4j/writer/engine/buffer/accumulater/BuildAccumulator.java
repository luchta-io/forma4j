package io.luchta.forma4j.writer.engine.buffer.accumulater;

import io.luchta.forma4j.writer.engine.buffer.accumulater.support.RowPropertyMap;
import io.luchta.forma4j.writer.engine.buffer.accumulater.support.SheetNameList;
import io.luchta.forma4j.writer.engine.model.book.XlsxBook;
import io.luchta.forma4j.writer.engine.model.cell.XlsxCell;
import io.luchta.forma4j.writer.engine.model.cell.XlsxCellList;
import io.luchta.forma4j.writer.engine.model.cell.address.XlsxCellAddress;
import io.luchta.forma4j.writer.engine.model.cell.address.XlsxRowNumber;
import io.luchta.forma4j.writer.engine.model.cell.address.XlsxSheetName;
import io.luchta.forma4j.writer.engine.model.row.XlsxRow;
import io.luchta.forma4j.writer.engine.model.row.XlsxRowList;
import io.luchta.forma4j.writer.engine.model.row.address.XlsxRowAddress;
import io.luchta.forma4j.writer.engine.model.row.property.XlsxRowProperties;
import io.luchta.forma4j.writer.engine.model.row.property.XlsxRowProperty;
import io.luchta.forma4j.writer.engine.model.sheet.XlsxSheet;
import io.luchta.forma4j.writer.engine.model.sheet.XlsxSheetList;
import io.luchta.forma4j.writer.engine.buffer.accumulater.support.CellMap;

import java.util.ArrayList;
import java.util.List;

public class BuildAccumulator {
    SheetNameList sheetNameList = new SheetNameList();
    RowPropertyMap rowPropertyMap = new RowPropertyMap();
    CellMap cells = new CellMap();

    public void add(XlsxSheetName sheetName) {
        sheetNameList.add(sheetName);
    }

    public void put(XlsxCellAddress address, XlsxCell cell) {
        cells.put(address, cell);
    }

    public void putRowProperty(XlsxRowAddress address, XlsxRowProperty property) {
        XlsxRowProperties properties = new XlsxRowProperties();
        if (rowPropertyMap.containsKey(address)) {
            properties = rowPropertyMap.get(address);
        }
        properties.add(property);
        rowPropertyMap.put(address, properties);
    }

    public XlsxBook toXlsxBook() {
        return new XlsxBook(toSheetList());
    }

    private XlsxSheetList toSheetList() {
        List<XlsxSheet> sheetList = new ArrayList<>();
        for (XlsxSheetName sheetName : sheetNameList) {
            XlsxRowList rowList = toRowList(sheetName);
            sheetList.add(new XlsxSheet(sheetName, rowList));
        }
        return new XlsxSheetList(sheetList);
    }

    private XlsxRowList toRowList(XlsxSheetName sheetName) {
        CellMap thisSheetCells = cells.filterBy(sheetName);
        List<XlsxRow> list = new ArrayList<>();
        for (XlsxRowNumber rowNumber : thisSheetCells.rowNumberList()) {
            XlsxCellList thisRowCellList = thisSheetCells
                    .filterBy(rowNumber)
                    .toXlsxCellList();

            XlsxRowAddress rowAddress = new XlsxRowAddress(sheetName, rowNumber);
            if (rowPropertyMap.containsKey(rowAddress)) {
                XlsxRowProperties properties = rowPropertyMap.get(rowAddress);
                list.add(new XlsxRow(rowNumber, thisRowCellList, properties));
                continue;
            }
            list.add(new XlsxRow(rowNumber, thisRowCellList));
        }
        return new XlsxRowList(list);
    }
}
