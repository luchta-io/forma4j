package io.luchta.forma4j.writer.processor.poi;

import io.luchta.forma4j.writer.engine.model.cell.style.XlsxCellStyle;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;

/**
 * セルスタイルの構築クラス
 */
public class CellStyleBuilder {
    /**
     * スタイル定義
     */
    private XlsxCellStyle definition;

    /**
     * ターゲットスタイル
     */
    private CellStyle targetStyle;

    /**
     * ターゲットフォント
     */
    private Font targetFont;

    /**
     * コンストラクタ
     * @param definition
     * @param targetStyle
     * @param targetFont
     */
    private CellStyleBuilder(XlsxCellStyle definition, CellStyle targetStyle, Font targetFont) {
        this.definition = definition;
        this.targetStyle = targetStyle;
        this.targetFont = targetFont;
    }

    /**
     * CellStyleBuilder生成
     * @param style
     * @param workbook
     * @return CellStyleBuilder
     */
    static CellStyleBuilder of(XlsxCellStyle style, Workbook workbook) {
        CellStyle cellStyle = workbook.createCellStyle();
        Font font = workbook.createFont();
        return new CellStyleBuilder(style, cellStyle, font);
    }

    /**
     * セルスタイル構築
     * @return セルスタイル
     */
    public CellStyle build() {
        definition.accept(this);
        targetStyle.setFont(targetFont);
        return targetStyle;
    }

    /**
     * 背景色を設定する
     * @param argb ARGB
     */
    public void setBackGroundColor(String argb) {
        XSSFColor color = new XSSFColor();
        color.setARGBHex(argb);

        ((XSSFCellStyle) targetStyle).setFillForegroundColor(color);
        targetStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    }

    /**
     * 文字の太字を設定する
     * @param bold true: 太字にする, false: 太字にしない
     */
    public void setBold(boolean bold) {
        targetFont.setBold(bold);
    }

    /**
     * 四隅すべてに同じ罫線を設定する
     * @param borderStyle 罫線の種類
     */
    public void setBorder(BorderStyle borderStyle) {
        setBorderLeft(borderStyle);
        setBorderTop(borderStyle);
        setBorderRight(borderStyle);
        setBorderBottom(borderStyle);
    }

    /**
     * 左罫線を設定する
     * @param borderStyle 罫線の種類
     */
    public void setBorderLeft(BorderStyle borderStyle) {
        targetStyle.setBorderLeft(borderStyle);
    }

    /**
     * 右罫線を設定する
     * @param borderStyle 罫線の種類
     */
    public void setBorderRight(BorderStyle borderStyle) {
        targetStyle.setBorderRight(borderStyle);
    }

    /**
     * 上罫線を設定する
     * @param borderStyle 罫線の種類
     */
    public void setBorderTop(BorderStyle borderStyle) {
        targetStyle.setBorderTop(borderStyle);
    }

    /**
     * 下罫線を設定する
     * @param borderStyle 罫線の種類
     */
    public void setBorderBottom(BorderStyle borderStyle) {
        targetStyle.setBorderBottom(borderStyle);
    }

    /**
     * 文字色を設定する
     * @param argb ARGB
     */
    public void setColor(String argb) {
        XSSFColor color = new XSSFColor();
        color.setARGBHex(argb);
        if (targetFont instanceof XSSFFont) {
            ((XSSFFont) targetFont).setColor(color);
        }
    }

    /**
     * フォントファミリーを設定する
     * @param fontFamily フォントファミリー名
     */
    public void setFontFamily(String fontFamily) {
        targetFont.setFontName(fontFamily);
    }

    /**
     * フォントサイズを設定する
     * @param points フォントサイズ
     */
    public void setFontHeightInPoints(short points) {
        targetFont.setFontHeightInPoints(points);
    }

    /**
     * 水平方向の文字位置を設定する
     * @param horizontalAlignment 文字位置
     */
    public void setHorizontalAlign(HorizontalAlignment horizontalAlignment) {
        targetStyle.setAlignment(horizontalAlignment);
    }

    /**
     * 文字の斜体を設定する
     * @param italic true: 斜体にする, false: 斜体にしない
     */
    public void setItalic(boolean italic) {
        targetFont.setItalic(italic);
    }

    /**
     * 垂直方向の文字位置を設定する
     * @param verticalAlignment 文字位置
     */
    public void setVerticalAlign(VerticalAlignment verticalAlignment) {
        targetStyle.setVerticalAlignment(verticalAlignment);
    }

    /**
     * テキストの折り返しを設定する
     * @param wrapped true: 折り返す, false: 折り返さない
     */
    public void setWrapText(boolean wrapped) {
        targetStyle.setWrapText(wrapped);
    }
}
