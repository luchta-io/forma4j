package io.luchta.forma4j.writer.engine.model.cell.address;

import io.luchta.forma4j.writer.definition.schema.attribute.index.ColumnIndex;

import java.util.Objects;

public class XlsxColumnNumber implements Comparable<XlsxColumnNumber> {
    Long value;

    public XlsxColumnNumber() {
    }

    public XlsxColumnNumber(Long value) {
        this.value = value;
    }

    public XlsxColumnNumber(ColumnIndex columnIndex) {
        this.value = columnIndex.value();
    }

    public static XlsxColumnNumber init() {
        return new XlsxColumnNumber(0L);
    }

    public XlsxColumnNumber increment() {
        return new XlsxColumnNumber(value + 1);
    }

    public int toInt() {
        return value.intValue();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof XlsxColumnNumber)) return false;
        XlsxColumnNumber that = (XlsxColumnNumber) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public int compareTo(XlsxColumnNumber o) {
        return value.compareTo(o.value);
    }
}
