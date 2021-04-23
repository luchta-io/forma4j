package jp.co.actier.luchta.forma4j.writer.definition.schema.attribute;

import javax.xml.bind.annotation.XmlValue;

public class Name {
    @XmlValue
    String value;

    public Name() {
    }

    public Name(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        if (value == null) return "";
        return value;
    }
}
