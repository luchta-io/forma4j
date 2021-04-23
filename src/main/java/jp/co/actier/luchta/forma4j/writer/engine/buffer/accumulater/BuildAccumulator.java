package jp.co.actier.luchta.forma4j.writer.engine.buffer.accumulater;

import jp.co.actier.luchta.forma4j.writer.engine.buffer.accumulater.support.CellMap;
import jp.co.actier.luchta.forma4j.writer.engine.buffer.accumulater.support.SheetNameList;
import jp.co.actier.luchta.forma4j.writer.engine.model.book.XlsxBook;
import jp.co.actier.luchta.forma4j.writer.engine.model.cell.XlsxCell;
import jp.co.actier.luchta.forma4j.writer.engine.model.cell.XlsxCellList;
import jp.co.actier.luchta.forma4j.writer.engine.model.cell.address.XlsxCellAddress;
import jp.co.actier.luchta.forma4j.writer.engine.model.cell.address.XlsxRowNumber;
import jp.co.actier.luchta.forma4j.writer.engine.model.cell.address.XlsxSheetName;
import jp.co.actier.luchta.forma4j.writer.engine.model.row.XlsxRow;
import jp.co.actier.luchta.forma4j.writer.engine.model.row.XlsxRowList;
import jp.co.actier.luchta.forma4j.writer.engine.model.sheet.XlsxSheet;
import jp.co.actier.luchta.forma4j.writer.engine.model.sheet.XlsxSheetList;

import java.util.ArrayList;
import java.util.List;

public class BuildAccumulator {
    SheetNameList sheetNameList = new SheetNameList();
    CellMap cells = new CellMap();

    public void add(XlsxSheetName sheetName) {
        sheetNameList.add(sheetName);
    }

    public void put(XlsxCellAddress address, XlsxCell cell) {
        cells.put(address, cell);
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
            list.add(new XlsxRow(rowNumber, thisRowCellList));
        }
        return new XlsxRowList(list);
    }
}
