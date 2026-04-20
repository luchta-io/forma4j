package io.luchta.forma4j.writer.definition.schema.attribute.sheet;

import jakarta.xml.bind.annotation.XmlValue;

public class AutoSizeColumn {
    @XmlValue
    String value;

    public AutoSizeColumn() {
    }

    public AutoSizeColumn(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    public boolean isEmpty() {
        return value == null;
    }
}
