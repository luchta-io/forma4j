package io.luchta.forma4j.writer.processor.poi;

import io.luchta.forma4j.writer.engine.buffer.accumulater.support.ColumnPropertyMap;
import io.luchta.forma4j.writer.engine.model.book.XlsxBook;
import io.luchta.forma4j.writer.engine.model.cell.XlsxCell;
import io.luchta.forma4j.writer.engine.model.cell.address.XlsxColumnNumber;
import io.luchta.forma4j.writer.engine.model.cell.style.XlsxCellStyle;
import io.luchta.forma4j.writer.engine.model.column.XlsxColumnAddress;
import io.luchta.forma4j.writer.engine.model.column.XlsxColumnRange;
import io.luchta.forma4j.writer.engine.model.column.property.WidthProperty;
import io.luchta.forma4j.writer.engine.model.column.property.XlsxColumnProperties;
import io.luchta.forma4j.writer.engine.model.column.property.XlsxColumnProperty;
import io.luchta.forma4j.writer.engine.model.row.XlsxRow;
import io.luchta.forma4j.writer.engine.model.sheet.XlsxSheet;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * ワークブックビルドクラス
 * <p>
 * {@link XlsxBook} の内容を読み取ってExcelワークブック作成を行います。
 * </p>
 */
public class WorkbookBuilder {
    /** モデル */
    XlsxBook model;

    /**
     * コンストラクタ
     * @param model
     */
    public WorkbookBuilder(XlsxBook model) {
        this.model = model;
    }

    /**
     * 列幅の自動調整ありでExcelワークブック作成
     * @return ワークブック
     */
    public Workbook build() {
        return build(new XSSFWorkbook(), true);
    }

    /**
     * 列幅の自動調整なしでExcelワークブック作成
     * @param in テンプレート
     * @return ワークブック
     * @throws IOException
     */
    public Workbook build(InputStream in) throws IOException {
        return build(WorkbookFactory.create(in), false);
    }

    /**
     * Excelワークブック作成
     * @param workbook ワークブック
     * @param autoSizeColumnEnabled true: 列幅自動調整あり, false: 列幅自動調整なし
     * @return ワークブック
     */
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

                    cellValue(cell, cellModel);
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

            setColumnStyle(sheetModel, sheet, autoSizeColumnEnabled);
        }
        return workbook;
    }

    /**
     * スタイル作成
     * <p>
     * 作成したスタイルを{@link Map}にプットして同じスタイルを複数作成しないようにします。
     * </p>
     * @param workbook
     * @return スタイル
     */
    private Map<XlsxCellStyle, CellStyle> makeStyleMap(Workbook workbook) {
        Map<XlsxCellStyle, CellStyle> map = new HashMap<>();
        for (XlsxCellStyle style : model.styles()) {
            CellStyleBuilder builder = CellStyleBuilder.of(style, workbook);
            CellStyle cellStyle = builder.build();
            map.put(style, cellStyle);
        }
        return map;
    }

    /**
     * セルの値をセット
     * <p>
     * 先頭が = で始まるときは計算式として扱います
     * </p>
     * @param cell
     * @param cellModel
     */
    private void cellValue(Cell cell, XlsxCell cellModel) {
        String value = cellModel.value().toString();
        if (value.startsWith("=")) {
            cell.setCellFormula(value.substring(1));
        } else {
            cell.setCellValue(value);
        }
    }

    /**
     * 列のスタイル設定
     * @param sheetModel
     * @param sheet
     * @param autoSizeColumnEnabled
     */
    private void setColumnStyle(XlsxSheet sheetModel, Sheet sheet, boolean autoSizeColumnEnabled) {
        ColumnPropertyMap map = sheetModel.columnPropertyMap();
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

        if (autoSizeColumnEnabled) {
            for (int i = 0; i < sheetModel.columnSize(); i++) {
                if (skipAutoSizeColumnNumberMap.containsKey(i) || sheetModel.isEmptyColumn(i)) {
                    continue;
                }
                sheet.autoSizeColumn(i);
            }
        }
    }
}
