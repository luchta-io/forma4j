package io.luchta.forma4j.writer.engine.model.cell.style;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;

import java.util.Objects;

public class BorderProperty implements XlsxCellStyleProperty {
    public static final String NAME = "BORDER";
    String value;

    public BorderProperty(String value) {
        this.value = value;
    }

    private BorderStyle borderStyle() {
        switch (value.toUpperCase()) {
            case "THIN":
                return BorderStyle.THIN;
            case "MEDIUM":
                return BorderStyle.MEDIUM;
            case "DASHED":
                return BorderStyle.DASHED;
            case "DOTTED":
                return BorderStyle.DOTTED;
            case "THICK":
                return BorderStyle.THICK;
            case "DOUBLE":
                return BorderStyle.DOUBLE;
            case "HAIR":
                return BorderStyle.HAIR;
            case "MEDIUM_DASHED":
                return BorderStyle.MEDIUM_DASHED;
            case "DASH_DOT":
                return BorderStyle.DASH_DOT;
            case "MEDIUM_DASH_DOT":
                return BorderStyle.MEDIUM_DASH_DOT;
            case "DASH_DOT_DOT":
                return BorderStyle.DASH_DOT_DOT;
            case "MEDIUM_DASH_DOT_DOT":
                return BorderStyle.MEDIUM_DASH_DOT_DOT;
            case "SLANTED_DASH_DOT":
                return BorderStyle.SLANTED_DASH_DOT;
            default:
                return BorderStyle.NONE;
        }
    }

    @Override
    public void overwriteTo(CellStyle cellStyle) {
        BorderStyle style = borderStyle();
        cellStyle.setBorderLeft(style);
        cellStyle.setBorderTop(style);
        cellStyle.setBorderRight(style);
        cellStyle.setBorderBottom(style);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BorderProperty that = (BorderProperty) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
