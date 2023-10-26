package io.luchta.forma4j.writer.engine.model.cell.style;

import io.luchta.forma4j.writer.processor.poi.CellStyleBuilder;

import java.util.Objects;

public class FontStyleProperty implements XlsxCellStyleProperty {
    public static final String NAME = "FONT-STYLE";
    String value;

    public FontStyleProperty(String value) {
        this.value = value;
    }

    public boolean isBold() {
        return value.toUpperCase().contains("BOLD");
    }

    public boolean isItalic() {
        return value.toUpperCase().contains("ITALIC");
    }

    @Override
    public void accept(CellStyleBuilder builder) {
        if (isBold()) builder.setBold(true);
        if (isItalic()) builder.setItalic(true);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FontStyleProperty that = (FontStyleProperty) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
