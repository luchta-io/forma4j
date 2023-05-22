package io.luchta.forma4j.reader.excel;

import io.luchta.forma4j.reader.model.excel.Address;
import io.luchta.forma4j.reader.model.excel.CellValue;
import io.luchta.forma4j.reader.model.excel.Index;
import io.luchta.forma4j.reader.model.excel.SheetName;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler;
import org.apache.poi.xssf.usermodel.XSSFComment;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SheetHandler implements XSSFSheetXMLHandler.SheetContentsHandler {
    private Integer currentRowNumber;
    private Integer currentColumnNumber;
    private Map<String, Map<Address, CellValue>> sheet = new ConcurrentHashMap<>();
    private SheetName sheetName;

    public SheetHandler(SheetName sheetName) {
        this.sheetName = sheetName;
    }

    @Override
    public void startRow(int rowNum) {
        currentRowNumber = rowNum;
        currentColumnNumber = -1;
    }

    @Override
    public void endRow(int rowNum) {

    }

    @Override
    public void cell(String cellReference, String formattedValue, XSSFComment comment) {
        CellReference reference = new CellReference(cellReference);
        Integer columnNumber = Integer.valueOf(reference.getCol());
        Integer unreadColumns = columnNumber - currentColumnNumber - 1;

        Map<Address, CellValue> data = new ConcurrentHashMap<>();
        if (sheet.containsKey(sheetName.toString())) {
            data = sheet.get(sheetName.toString());
        }

        for (int i = 0; i < unreadColumns; i++) {
            Address address = new Address(new Index(currentRowNumber), new Index(currentColumnNumber + 1 + i));
            CellValue cellValue = new CellValue("");
            data.put(address, cellValue);
        }

        Address address = new Address(new Index(currentRowNumber), new Index(columnNumber));
        CellValue cellValue = new CellValue(formattedValue);
        data.put(address, cellValue);

        sheet.put(sheetName.toString(), data);

        currentColumnNumber = columnNumber;
    }
}
