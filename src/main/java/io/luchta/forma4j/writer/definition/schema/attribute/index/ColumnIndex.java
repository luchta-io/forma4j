package io.luchta.forma4j.writer.definition.schema.attribute.index;

import javax.xml.bind.annotation.XmlValue;

public class ColumnIndex {
    @XmlValue
    Long value;

    public ColumnIndex() {
    }

    public ColumnIndex(Long value) {
        this.value = value;
    }

    public Long value() {
        return value;
    }

    public boolean isEmpty() {
        return value == null;
    }
}
