package io.luchta.forma4j.writer.engine.model.cell.style;

import io.luchta.forma4j.writer.processor.poi.CellStyleBuilder;

import java.util.Objects;

public class BackGroundColorProperty implements XlsxCellStyleProperty {
    public static final String NAME = "BACKGROUND-COLOR";
    String value;

    public BackGroundColorProperty(String value) {
        this.value = value;
    }

    @Override
    public void accept(CellStyleBuilder builder) {
        builder.setBackGroundColor(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BackGroundColorProperty that = (BackGroundColorProperty) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
