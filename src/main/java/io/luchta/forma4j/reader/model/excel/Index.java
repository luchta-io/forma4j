package io.luchta.forma4j.reader.model.excel;

import java.util.Objects;

public class Index {

    Integer value;

    public Index() {
        value = null;
    }

    public Index(Integer value) {
        this.value = value;
    }

    public Integer toInteger() {
        return value;
    }

    public boolean isEmpty() {
        return value == null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Index index = (Index) o;
        return Objects.equals(value, index.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
