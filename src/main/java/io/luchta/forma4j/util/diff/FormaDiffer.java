package io.luchta.forma4j.util.diff;

import io.luchta.forma4j.context.databind.json.JsonNode;
import io.luchta.forma4j.context.databind.json.JsonNodes;
import io.luchta.forma4j.context.databind.json.JsonObject;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;

/**
 * Excelの比較を行う
 */
public class FormaDiffer {
    /**
     * 2つのExcel比較を行う
     * @param comparing
     * @param compared
     * @return 比較結果: 例）[{ "target": "セルの値", "sheet": "sheet_name", "range": "A1", "comparing_value": "hoge", "compared_value": "fuga", "description": "セルの値が異なります" }]
     */
    public JsonObject diff(InputStream comparing, InputStream compared) throws IOException {
        Workbook comparingWorkbook = new XSSFWorkbook(comparing);
        Workbook comparedWorkbook = new XSSFWorkbook(compared);

        JsonNodes jsonNodes = new JsonNodes();
        JsonObject json = new JsonObject(jsonNodes);

        this.diffWorkbooks(comparingWorkbook, comparedWorkbook, json);

        return json;
    }

    /**
     * ワークブックの比較を行う
     * @param comparing
     * @param compared
     * @param json
     */
    private void diffWorkbooks(Workbook comparing, Workbook compared, JsonObject json) {
        int numberOfComparingSheets = comparing.getNumberOfSheets();
        int numberOfComparedSheets = compared.getNumberOfSheets();
        if (numberOfComparingSheets != numberOfComparedSheets) {
            this.appendDiffResult("ワークブック", "", "", "", "", "シートの数が異なります", json);
        }

        for (int i = 0; i < numberOfComparingSheets; i++) {
            Sheet comparingWorkbookSheet = comparing.getSheetAt(i);
            if (i > numberOfComparedSheets - 1) {
                continue;
            }
            Sheet comparedWorkbookSheet = compared.getSheetAt(i);
            this.diffSheets(comparingWorkbookSheet, comparedWorkbookSheet, json);
        }
    }

    /**
     * シートの比較を行う
     * @param comparing
     * @param compared
     * @param json
     */
    private void diffSheets(Sheet comparing, Sheet compared, JsonObject json) {
        String comparingSheetName = comparing.getSheetName();
        String comparedSheetName = compared.getSheetName();
        if (!comparingSheetName.equals(comparedSheetName)) {
            this.appendDiffResult("シート", "", "", comparingSheetName, comparedSheetName, "シート名が異なります", json);
        }

        int comparingSheetLastRow = comparing.getLastRowNum();
        int comparedSheetLastRow = compared.getLastRowNum();
        if (comparingSheetLastRow != comparedSheetLastRow) {
            this.appendDiffResult("シート", comparingSheetName, "", String.valueOf(comparingSheetLastRow), String.valueOf(comparedSheetLastRow), "シートの行数が異なります", json);
        }

        // 行の比較
        for (int i = 0; i <= comparingSheetLastRow; i++) {
            Row comparingSheetRow = comparing.getRow(i);
            Row comparedSheetRow = compared.getRow(i);
            this.diffRows(comparingSheetRow, comparedSheetRow, json);
        }
    }

    /**
     * 行の比較を行う
     * @param comparing
     * @param compared
     * @param json
     */
    private void diffRows(Row comparing, Row compared, JsonObject json) {
        if (comparing == null && compared == null) {
            return;
        }

        if (comparing == null || compared == null) {
            String sheetName = comparing == null ? compared.getSheet().getSheetName() : comparing.getSheet().getSheetName();
            int rowIndex = comparing == null ? compared.getRowNum() : comparing.getRowNum();
            this.appendDiffResult("行", sheetName, String.valueOf(rowIndex), "", "", "行が存在しません", json);
            return;
        }

        int comparingRowLastCell = comparing.getLastCellNum();
        for (int i = 0; i < comparingRowLastCell; i++) {
            Cell comparingRowCell = comparing.getCell(i);
            Cell comparedRowCell = compared.getCell(i);
            this.diffCells(comparingRowCell, comparedRowCell, json);
        }
    }

    /**
     * セルの比較を行う
     * @param comparing
     * @param compared
     * @param json
     */
    private void diffCells(Cell comparing, Cell compared, JsonObject json) {
        if (comparing == null && compared == null) {
            return;
        }

        String sheetName = comparing == null ? compared.getSheet().getSheetName() : comparing.getSheet().getSheetName();
        String range = comparing == null ? CellReference.convertNumToColString(compared.getColumnIndex()) + (compared.getRowIndex() + 1) : CellReference.convertNumToColString(comparing.getColumnIndex()) + (comparing.getRowIndex() + 1);
        if (comparing == null || compared == null) {
            this.appendDiffResult("セル", sheetName, range, "", "", "セルが存在しません", json);
            return;
        }

        String comparingCellValue = comparing.toString();
        String comparedCellValue = compared.toString();
        if (!comparingCellValue.equals(comparedCellValue)) {
            this.appendDiffResult("セル", sheetName, range, comparingCellValue, comparedCellValue, "セルの値が異なります", json);
        }

        this.diffStyles(comparing, compared, range, json);
    }

    /**
     * スタイルの比較を行う
     * @param comparing
     * @param compared
     * @param range
     * @param json
     */
    private void diffStyles(Cell comparing, Cell compared, String range, JsonObject json) {
        Workbook comparingWorkbook = comparing.getSheet().getWorkbook();
        Workbook comparedWorkbook = compared.getSheet().getWorkbook();
        String sheetName = comparing.getSheet().getSheetName();
        CellStyle comparingCellStyle = comparing.getCellStyle();
        CellStyle comparedCellStyle = compared.getCellStyle();

        // 背景色
        String comparingForegroundRGB = convertColorToRGB((XSSFColor) comparingCellStyle.getFillForegroundColorColor());
        String comparedForegroundRGB = convertColorToRGB((XSSFColor) comparedCellStyle.getFillForegroundColorColor());
        if (comparingForegroundRGB != null && comparedForegroundRGB != null && !comparingForegroundRGB.equals(comparedForegroundRGB)) {
            this.appendDiffResult("セルスタイル", sheetName, range, comparingForegroundRGB, comparedForegroundRGB, "セルの背景色が異なります", json);
        }

        // 罫線
        if (!comparingCellStyle.getBorderTop().equals(comparedCellStyle.getBorderTop())) {
            this.appendDiffResult("セルスタイル", sheetName, range, comparingCellStyle.getBorderTop().name(), comparedCellStyle.getBorderTop().name(), "セルの上罫線が異なります", json);
        }
        if (!comparingCellStyle.getBorderBottom().equals(comparedCellStyle.getBorderBottom())) {
            this.appendDiffResult("セルスタイル", sheetName, range, comparingCellStyle.getBorderBottom().name(), comparedCellStyle.getBorderBottom().name(), "セルの下罫線が異なります", json);
        }
        if (!comparingCellStyle.getBorderLeft().equals(comparedCellStyle.getBorderLeft())) {
            this.appendDiffResult("セルスタイル", sheetName, range, comparingCellStyle.getBorderLeft().name(), comparedCellStyle.getBorderLeft().name(), "セルの左罫線が異なります", json);
        }
        if (!comparingCellStyle.getBorderRight().equals(comparedCellStyle.getBorderRight())) {
            this.appendDiffResult("セルスタイル", sheetName, range, comparingCellStyle.getBorderRight().name(), comparedCellStyle.getBorderRight().name(), "セルの右罫線が異なります", json);
        }

        // 文字色
        String comparingRGB = fontColorRGB(comparingWorkbook, comparingCellStyle);
        String comparedRGB = fontColorRGB(comparedWorkbook, comparedCellStyle);
        if (comparingRGB != null && comparedRGB != null && !comparingRGB.equals(comparedRGB)) {
            this.appendDiffResult("セルスタイル", sheetName, range, comparingRGB, comparedRGB, "セルの文字色が異なります", json);
        }

        // フォントファミリー
        Font comparingFont = comparingWorkbook.getFontAt(comparingCellStyle.getFontIndex());
        Font comparedFont = comparedWorkbook.getFontAt(comparedCellStyle.getFontIndex());
        String comparingFontName = comparingFont.getFontName();
        String comparedFontName = comparedFont.getFontName();
        if (!comparingFontName.equals(comparedFontName)) {
            this.appendDiffResult("セルスタイル", sheetName, range, comparingFontName, comparedFontName, "セルのフォントが異なります", json);
        }

        // フォントサイズ
        short comparingFontHeightInPoints = comparingFont.getFontHeightInPoints();
        short comparedFontHeightInPoints = comparedFont.getFontHeightInPoints();
        if (comparingFontHeightInPoints != comparedFontHeightInPoints) {
            this.appendDiffResult("セルスタイル", sheetName, range, String.valueOf(comparingFontHeightInPoints), String.valueOf(comparedFontHeightInPoints), "セルのフォントサイズが異なります", json);
        }

        // 垂直方向の文字位置
        if (!comparingCellStyle.getVerticalAlignment().equals(comparedCellStyle.getVerticalAlignment())) {
            this.appendDiffResult("セルスタイル", sheetName, range, comparingCellStyle.getVerticalAlignment().name(), comparedCellStyle.getVerticalAlignment().name(), "セルの垂直方向の文字位置が異なります", json);
        }

        // 水平方向の文字位置
        if (!comparingCellStyle.getAlignment().equals(comparedCellStyle.getAlignment())) {
            this.appendDiffResult("セルスタイル", sheetName, range, comparingCellStyle.getAlignment().name(), comparedCellStyle.getAlignment().name(), "セルの水平方向の文字位置が異なります", json);
        }

        // カラム幅
        int comparingColumnWidth = comparing.getSheet().getColumnWidth(comparing.getColumnIndex()) / 256;
        int comparedColumnWidth = compared.getSheet().getColumnWidth(compared.getColumnIndex()) / 256;
        if (comparingColumnWidth != comparedColumnWidth) {
            this.appendDiffResult("セルスタイル", sheetName, range, String.valueOf(comparingColumnWidth), String.valueOf(comparedColumnWidth), "セルの幅が異なります", json);
        }

        // 折り返し設定
        if (comparingCellStyle.getWrapText() != comparedCellStyle.getWrapText()) {
            this.appendDiffResult("セルスタイル", sheetName, range, String.valueOf(comparingCellStyle.getWrapText()), String.valueOf(comparedCellStyle.getWrapText()), "セルの折り返し設定が異なります", json);
        }
    }

    /**
     * 比較結果をJSONに追加する
     * @param target
     * @param sheetName
     * @param range
     * @param comparingValue
     * @param comparedValue
     * @param description
     * @param json
     */
    private void appendDiffResult(String target, String sheetName, String range, String comparingValue, String comparedValue, String description, JsonObject json) {
        JsonNode node = new JsonNode();
        node.putVar("target", new JsonObject(target));
        node.putVar("sheet", new JsonObject(sheetName));
        node.putVar("range", new JsonObject(range));
        node.putVar("comparingValue", new JsonObject(comparingValue));
        node.putVar("comparedValue", new JsonObject(comparedValue));
        node.putVar("description", new JsonObject(description));
        ((JsonNodes) json.getValue()).add(node);
    }

    /**
     * 文字色のRGBを取得
     * @param workbook
     * @param cellStyle
     * @return RGB
     */
    private String fontColorRGB(Workbook workbook, CellStyle cellStyle) {
        String rgbHex = null;
        if (workbook instanceof XSSFWorkbook) {
            XSSFFont font = (XSSFFont) workbook.getFontAt(cellStyle.getFontIndexAsInt());
            rgbHex = convertColorToRGB(font.getXSSFColor());
        }
        return rgbHex;
    }

    /**
     * ColorをRBGに変換
     * @param color
     * @return RGB
     */
    private String convertColorToRGB(XSSFColor color) {
        if (color == null) {
            return null;
        }

        byte[] rgb = color.getRGB();
        if (rgb == null) {
            return null;
        }

        return String.format("%02X%02X%02X", rgb[0], rgb[1], rgb[2]);
    }
}
