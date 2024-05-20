package io.luchta.forma4j.writer.engine.model.cell.style;

import io.luchta.forma4j.writer.processor.poi.CellStyleBuilder;

import java.util.Objects;

public class ColorProperty implements XlsxCellStyleProperty {
    public static final String NAME = "COLOR";
    String value;

    public ColorProperty(String value) {
        this.value = value;
    }

    @Override
    public void accept(CellStyleBuilder builder) {
        builder.setColor(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ColorProperty that = (ColorProperty) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
