package io.luchta.forma4j.writer.processor.poi;

import io.luchta.forma4j.writer.engine.buffer.accumulater.BuildAccumulator;
import io.luchta.forma4j.writer.engine.buffer.accumulater.support.ColumnPropertyMap;
import io.luchta.forma4j.writer.engine.model.cell.XlsxCell;
import io.luchta.forma4j.writer.engine.model.cell.address.XlsxColumnNumber;
import io.luchta.forma4j.writer.engine.model.cell.address.XlsxRowNumber;
import io.luchta.forma4j.writer.engine.model.cell.address.XlsxSheetName;
import io.luchta.forma4j.writer.engine.model.cell.style.XlsxCellStyle;
import io.luchta.forma4j.writer.engine.model.column.XlsxColumnAddress;
import io.luchta.forma4j.writer.engine.model.column.XlsxColumnRange;
import io.luchta.forma4j.writer.engine.model.column.property.WidthProperty;
import io.luchta.forma4j.writer.engine.model.column.property.XlsxColumnProperties;
import io.luchta.forma4j.writer.engine.model.column.property.XlsxColumnProperty;
import io.luchta.forma4j.writer.engine.model.row.address.XlsxRowAddress;
import io.luchta.forma4j.writer.engine.model.row.property.AutoFilterProperty;
import io.luchta.forma4j.writer.engine.model.row.property.XlsxRowProperties;
import io.luchta.forma4j.writer.engine.model.row.property.XlsxRowProperty;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * ワークブックビルドクラス
 */
public class WorkbookBuilder {
    BuildAccumulator accumulator;

    public WorkbookBuilder(BuildAccumulator accumulator) {
        this.accumulator = accumulator;
    }

    public Workbook build() {
        return build(new XSSFWorkbook(), true, false);
    }

    public Workbook build(InputStream in) throws IOException {
        return build(WorkbookFactory.create(in), false, true);
    }

    private Workbook build(Workbook workbook, boolean autoSizeColumnEnabled, boolean previousCellStyle) {
        Map<XlsxCellStyle, CellStyle> styleMap = makeStyleMap(workbook);

        for (XlsxSheetName sheetName : accumulator.sheetNames()) {
            Sheet sheet = workbook.getSheet(sheetName.toString());
            if (sheet == null) {
                sheet = workbook.createSheet(sheetName.toString());
            }

            int columnSize = 0;
            Map<Integer, Integer> nonEmptyColumnNumbers = new HashMap<>();

            for (Map.Entry<XlsxRowNumber, Map<XlsxColumnNumber, XlsxCell>> rowEntry : accumulator.rows(sheetName).entrySet()) {
                XlsxRowNumber rowNumber = rowEntry.getKey();
                Row row = sheet.getRow(rowNumber.toInt());
                if (row == null) {
                    row = sheet.createRow(rowNumber.toInt());
                }

                XlsxColumnNumber firstColumnNumber = null;
                XlsxColumnNumber lastColumnNumber = null;
                for (XlsxCell cellModel : rowEntry.getValue().values()) {
                    Cell cell = row.getCell(cellModel.columnNumber().toInt());
                    if (cell == null) {
                        cell = row.createCell(cellModel.columnNumber().toInt());
                    }

                    if (previousCellStyle && cellModel.style().isEmpty()) {
                        cell.setCellStyle(cell.getCellStyle());
                    } else {
                        cell.setCellStyle(styleMap.get(cellModel.style()));
                    }
                    cellValue(cell, cellModel);
                    if (!cellModel.isEmpty()) {
                        nonEmptyColumnNumbers.put(cellModel.columnNumber().toInt(), cellModel.columnNumber().toInt());
                    }
                    if (firstColumnNumber == null) {
                        firstColumnNumber = cellModel.columnNumber();
                    }
                    lastColumnNumber = cellModel.columnNumber();
                }

                if (hasAutoFilter(sheetName, rowNumber)) {
                    XlsxColumnRange range = new XlsxColumnRange(firstColumnNumber, lastColumnNumber);
                    sheet.setAutoFilter(
                            new CellRangeAddress(
                                    rowNumber.toInt(),
                                    rowNumber.toInt(),
                                    range.firstColumnNumber().toInt(),
                                    range.lastColumnNumber().toInt()
                            )
                    );
                }
                columnSize = Math.max(columnSize, rowEntry.getValue().size());
            }

            setColumnStyle(sheetName, sheet, autoSizeColumnEnabled, columnSize, nonEmptyColumnNumbers);
        }
        return workbook;
    }

    private Map<XlsxCellStyle, CellStyle> makeStyleMap(Workbook workbook) {
        Map<XlsxCellStyle, CellStyle> map = new HashMap<>();
        for (XlsxCellStyle style : accumulator.styles()) {
            CellStyleBuilder builder = CellStyleBuilder.of(style, workbook);
            CellStyle cellStyle = builder.build();
            map.put(style, cellStyle);
        }
        return map;
    }

    private void cellValue(Cell cell, XlsxCell cellModel) {
        try {
            if (cellModel.isEmpty()) {
                cell.setCellValue("");
                return;
            }

            if (cellModel.isFormula()) {
                cell.setCellFormula(cellModel.toFormula().substring(1));
                return;
            }

            if (cellModel.isBoolean()) {
                cell.setCellValue(cellModel.toBoolean());
                return;
            }

            if (cellModel.isDate()) {
                cell.setCellValue(cellModel.toDate());
                return;
            }

            if (cellModel.isDateTime()) {
                cell.setCellValue(cellModel.toDateTime());
                return;
            }

            if (cellModel.isNumeric()) {
                cell.setCellValue(cellModel.toNumeric());
                return;
            }

            cell.setCellValue(cellModel.toText());
        } catch (Exception e) {
            cell.setCellValue(cellModel.toText());
        }
    }

    private void setColumnStyle(
            XlsxSheetName sheetName,
            Sheet sheet,
            boolean autoSizeColumnEnabled,
            int columnSize,
            Map<Integer, Integer> nonEmptyColumnNumbers
    ) {
        ColumnPropertyMap map = accumulator.columnProperties(sheetName);
        Map<Integer, Integer> skipAutoSizeColumnNumberMap = new HashMap<>();
        for (Map.Entry<XlsxColumnAddress, XlsxColumnProperties> entry : map.entrySet()) {
            XlsxColumnNumber columnNumber = entry.getKey().columnNumber();
            for (XlsxColumnProperty property : entry.getValue()) {
                if (property instanceof WidthProperty) {
                    sheet.setColumnWidth(columnNumber.toInt(), ((WidthProperty) property).intValue() * 256);
                    skipAutoSizeColumnNumberMap.put(columnNumber.toInt(), columnNumber.toInt());
                }
            }
        }

        boolean resolvedAutoSizeColumnEnabled = autoSizeColumnEnabled;
        if (accumulator.autoSizeColumnEnabled(sheetName) != null) {
            resolvedAutoSizeColumnEnabled = accumulator.autoSizeColumnEnabled(sheetName);
        }

        if (resolvedAutoSizeColumnEnabled) {
            for (int i = 0; i < columnSize; i++) {
                if (skipAutoSizeColumnNumberMap.containsKey(i) || !nonEmptyColumnNumbers.containsKey(i)) {
                    continue;
                }
                sheet.autoSizeColumn(i);
            }
        }
    }

    private boolean hasAutoFilter(XlsxSheetName sheetName, XlsxRowNumber rowNumber) {
        XlsxRowAddress rowAddress = new XlsxRowAddress(sheetName, rowNumber);
        if (!accumulator.hasRowProperties(rowAddress)) {
            return false;
        }

        XlsxRowProperties properties = accumulator.rowProperties(rowAddress);
        for (XlsxRowProperty property : properties) {
            if (property instanceof AutoFilterProperty) {
                return ((AutoFilterProperty) property).booleanValue();
            }
        }
        return false;
    }
}
