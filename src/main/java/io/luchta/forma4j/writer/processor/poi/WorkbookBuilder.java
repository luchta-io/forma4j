package io.luchta.forma4j.writer.processor.poi;

import io.luchta.forma4j.writer.engine.model.book.XlsxBook;
import io.luchta.forma4j.writer.engine.model.cell.XlsxCell;
import io.luchta.forma4j.writer.engine.model.row.XlsxRow;
import io.luchta.forma4j.writer.engine.model.sheet.XlsxSheet;
import io.luchta.forma4j.writer.processor.poi.setting.StyleSetting;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;

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
     * @param model Excelへ書き込む内容
     */
    public WorkbookBuilder(XlsxBook model) {
        this.model = model;
    }

    /**
     * Excel ワークブック作成
     * @return Excel ワークブック
     */
    public Workbook build() {
        return build(new XSSFWorkbook(), false);
    }

    /**
     * Excel で作成されたテンプレートを利用したワークブック作成
     * @param in テンプレートファイル
     * @return Excel ワークブック
     * @throws IOException
     */
    public Workbook build(InputStream in) throws IOException {
        return build(WorkbookFactory.create(in), true);
    }

    /**
     * {@code model} の内容に従って Excel ワークブックを作成
     * @param workbook Excel ワークブック
     * @return
     */
    private Workbook build(Workbook workbook, boolean hasTemplate) {
        for (XlsxSheet sheetModel : model.sheets()) {
            Sheet sheet = null;
            if (hasTemplate) {
                sheet = workbook.getSheet(sheetModel.name().toString());
            } else {
                sheet = workbook.createSheet(sheetModel.name().toString());
            }

            for (XlsxRow rowModel : sheetModel.rows()) {
                Row row = null;
                if (hasTemplate) {
                    row = sheet.getRow(rowModel.rowNumber().toInt());
                } else {
                    row = sheet.createRow(rowModel.rowNumber().toInt());
                }
                for (XlsxCell cellModel : rowModel.cells()) {
                    Cell cell = row.createCell(cellModel.columnNumber().toInt());
                    cell.setCellValue(cellModel.value().toString());

                    StyleSetting styleSetting = new StyleSetting();
                    styleSetting.set(cell, cellModel.styles());
                }
            }
        }
        return workbook;
    }
}
