package io.luchta.forma4j.writer.processor.poi;

import io.luchta.forma4j.writer.engine.model.book.XlsxBook;
import io.luchta.forma4j.writer.engine.model.cell.XlsxCell;
import io.luchta.forma4j.writer.engine.model.cell.style.XlsxCellStyle;
import io.luchta.forma4j.writer.engine.model.row.XlsxRow;
import io.luchta.forma4j.writer.engine.model.sheet.XlsxSheet;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * {@code WorkbookBuilder} は EXCEL のワークシートを作成するクラスです。
 *
 * <p>
 * {@link XlsxBook} の内容に従って EXCEL に書き込みを行います。
 * </p>
 *
 * @since 0.1.0
 */
public class WorkbookBuilder {
    /**
     * Excelへ書き込む内容
     */
    XlsxBook model;

    /**
     * コンストラクタ
     *
     * @param model Excelへ書き込む内容
     */
    public WorkbookBuilder(XlsxBook model) {
        this.model = model;
    }

    /**
     * Excel ワークブック作成
     *
     * @return Excel ワークブック
     */
    public Workbook build() {
        return build(new XSSFWorkbook());
    }

    /**
     * Excel で作成されたテンプレートを利用したワークブック作成
     *
     * @param in テンプレートファイル
     * @return Excel ワークブック
     */
    public Workbook build(InputStream in) throws IOException {
        return build(WorkbookFactory.create(in));
    }

    private Workbook build(Workbook workbook) {
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
