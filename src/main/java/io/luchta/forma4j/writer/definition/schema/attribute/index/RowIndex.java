package io.luchta.forma4j.writer.definition.schema.attribute.index;

import jakarta.xml.bind.annotation.XmlValue;

public class RowIndex {
    @XmlValue
    Long value;

    public RowIndex() {
    }

    public RowIndex(Long value) {
        this.value = value;
    }

    public Long value() {
        return value;
    }

    public boolean isEmpty() {
        return value == null;
    }
}
