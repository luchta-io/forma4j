package io.luchta.forma4j.writer.processor.poi;

import io.luchta.forma4j.writer.engine.model.book.XlsxBook;
import io.luchta.forma4j.writer.engine.model.cell.XlsxCell;
import io.luchta.forma4j.writer.engine.model.cell.style.XlsxCellStyle;
import io.luchta.forma4j.writer.engine.model.column.XlsxColumnRange;
import io.luchta.forma4j.writer.engine.model.row.XlsxRow;
import io.luchta.forma4j.writer.engine.model.sheet.XlsxSheet;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class WorkbookBuilder {
    XlsxBook model;

    public WorkbookBuilder(XlsxBook model) {
        this.model = model;
    }

    public Workbook build() {
        return build(new XSSFWorkbook(), true);
    }

    public Workbook build(InputStream in) throws IOException {
        return build(WorkbookFactory.create(in), false);
    }

    private Workbook build(Workbook workbook, boolean autoSizeColumnEnabled) {
        Map<XlsxCellStyle, CellStyle> styleMap = makeStyleMap(workbook);

        for (XlsxSheet sheetModel : model.sheets()) {
            Sheet sheet = workbook.getSheet(sheetModel.name().toString());
            if (sheet == null) {
                sheet = workbook.createSheet(sheetModel.name().toString());
            }

            for (XlsxRow rowModel : sheetModel.rows()) {
                Row row = sheet.getRow(rowModel.rowNumber().toInt());
                if (row == null) {
                    row = sheet.createRow(rowModel.rowNumber().toInt());
                }

                for (XlsxCell cellModel : rowModel.cells()) {
                    Cell cell = row.getCell(cellModel.columnNumber().toInt());
                    if (cell == null) {
                        cell = row.createCell(cellModel.columnNumber().toInt());
                    }

                    cell.setCellValue(cellModel.value().toString());
                    cell.setCellStyle(styleMap.get(cellModel.style()));
                }

                if (rowModel.hasAutoFilter()) {
                    XlsxColumnRange range = rowModel.columnRange();
                    sheet.setAutoFilter(
                            new CellRangeAddress(
                                    rowModel.rowNumber().toInt(),
                                    rowModel.rowNumber().toInt(),
                                    range.firstColumnNumber().toInt(),
                                    range.lastColumnNumber().toInt()
                            )
                    );
                }
            }

            if (!autoSizeColumnEnabled) {
                continue;
            }

            for (int i = 0; i < sheetModel.columnSize(); i++) {
                sheet.autoSizeColumn(i);
            }
        }
        return workbook;
    }

    private Map<XlsxCellStyle, CellStyle> makeStyleMap(Workbook workbook) {
        Map<XlsxCellStyle, CellStyle> map = new HashMap<>();
        for (XlsxCellStyle style : model.styles()) {
            CellStyleBuilder builder = CellStyleBuilder.of(style, workbook);
            CellStyle cellStyle = builder.build();
            map.put(style, cellStyle);
        }
        return map;
    }
}
