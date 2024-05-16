package io.luchta.forma4j.writer.engine.model.cell.style;

import io.luchta.forma4j.writer.processor.poi.CellStyleBuilder;

import java.util.Objects;

public class FontFamilyProperty implements XlsxCellStyleProperty {
    public static final String NAME = "FONT-FAMILY";
    String value;

    public FontFamilyProperty(String value) {
        this.value = value;
    }

    @Override
    public void accept(CellStyleBuilder builder) {
        builder.setFontFamily(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FontFamilyProperty that = (FontFamilyProperty) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
