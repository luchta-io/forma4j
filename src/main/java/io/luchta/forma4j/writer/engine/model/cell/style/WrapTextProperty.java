package io.luchta.forma4j.writer.engine.model.cell.style;

import io.luchta.forma4j.writer.processor.poi.CellStyleBuilder;

import java.util.Objects;

public class WrapTextProperty implements XlsxCellStyleProperty {
    public static final String NAME = "WRAPTEXT";
    String value;

    public WrapTextProperty(String value) {
        this.value = value;
    }

    @Override
    public void accept(CellStyleBuilder builder) {
        if ("TRUE".equals(value.toUpperCase())) {
            builder.setWrapText(true);
            return;
        }
        builder.setWrapText(false);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WrapTextProperty that = (WrapTextProperty) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
