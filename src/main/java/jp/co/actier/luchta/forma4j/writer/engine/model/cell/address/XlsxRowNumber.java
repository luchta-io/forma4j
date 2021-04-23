package jp.co.actier.luchta.forma4j.writer.engine.model.cell.address;

import jp.co.actier.luchta.forma4j.writer.definition.schema.attribute.index.RowIndex;

import java.util.Objects;

public class XlsxRowNumber implements Comparable<XlsxRowNumber> {
    Long value;

    public XlsxRowNumber() {
    }

    public XlsxRowNumber(Long value) {
        this.value = value;
    }

    public XlsxRowNumber(RowIndex rowIndex) {
        this.value = rowIndex.value();
    }

    public static XlsxRowNumber init() {
        return new XlsxRowNumber(0L);
    }

    public XlsxRowNumber increment() {
        return new XlsxRowNumber(value + 1);
    }

    public int toInt() {
        return value.intValue();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof XlsxRowNumber)) return false;
        XlsxRowNumber that = (XlsxRowNumber) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public int compareTo(XlsxRowNumber o) {
        return value.compareTo(o.value);
    }
}
