package io.luchta.forma4j.writer.engine.model.cell.address;

import io.luchta.forma4j.writer.definition.schema.attribute.Name;

import java.util.Objects;

public class XlsxSheetName {
    String value;

    public XlsxSheetName() {
    }

    public XlsxSheetName(Name name) {
        this.value = name.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof XlsxSheetName)) return false;
        XlsxSheetName that = (XlsxSheetName) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
