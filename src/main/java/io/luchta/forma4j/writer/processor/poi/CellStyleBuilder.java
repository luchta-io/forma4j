package io.luchta.forma4j.writer.processor.poi;

import io.luchta.forma4j.writer.engine.model.cell.style.XlsxCellStyle;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;

public class CellStyleBuilder {
    XlsxCellStyle definition;

    CellStyle targetStyle;
    Font targetFont;

    private CellStyleBuilder(XlsxCellStyle definition, CellStyle targetStyle, Font targetFont) {
        this.definition = definition;
        this.targetStyle = targetStyle;
        this.targetFont = targetFont;
    }

    static CellStyleBuilder of(XlsxCellStyle style, Workbook workbook) {
        CellStyle cellStyle = workbook.createCellStyle();
        Font font = workbook.createFont();
        return new CellStyleBuilder(style, cellStyle, font);
    }

    public CellStyle build() {
        definition.accept(this);
        targetStyle.setFont(targetFont);
        return targetStyle;
    }

    public void setWrapText(boolean wrapped) {
        targetStyle.setWrapText(wrapped);
    }

    public void setFontHeightInPoints(short points) {
        targetFont.setFontHeightInPoints(points);
    }

    public void setBorder(BorderStyle borderStyle) {
        setBorderLeft(borderStyle);
        setBorderTop(borderStyle);
        setBorderRight(borderStyle);
        setBorderBottom(borderStyle);
    }

    public void setBorderLeft(BorderStyle borderStyle) {
        targetStyle.setBorderLeft(borderStyle);
    }

    public void setBorderRight(BorderStyle borderStyle) {
        targetStyle.setBorderRight(borderStyle);
    }

    public void setBorderTop(BorderStyle borderStyle) {
        targetStyle.setBorderTop(borderStyle);
    }

    public void setBorderBottom(BorderStyle borderStyle) {
        targetStyle.setBorderBottom(borderStyle);
    }

    public void setBold(boolean bold) {
        targetFont.setBold(bold);
    }

    public void setItalic(boolean italic) {
        targetFont.setItalic(italic);
    }

    public void setBackGroundColor(String argb) {
        XSSFColor color = new XSSFColor();
        color.setARGBHex(argb);
        targetStyle.setFillForegroundColor(color);
        targetStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    }

    public void setColor(String argb) {
        XSSFColor color = new XSSFColor();
        color.setARGBHex(argb);
        if (targetFont instanceof XSSFFont) {
            ((XSSFFont) targetFont).setColor(color);
        }
    }
}
