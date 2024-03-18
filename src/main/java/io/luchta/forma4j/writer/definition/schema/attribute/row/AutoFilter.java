package io.luchta.forma4j.writer.definition.schema.attribute.row;

import jakarta.xml.bind.annotation.XmlValue;

public class AutoFilter {
    @XmlValue
    String value;

    public AutoFilter() {
    }

    public AutoFilter(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    public boolean isEmpty() {
        return value == null;
    }
}
