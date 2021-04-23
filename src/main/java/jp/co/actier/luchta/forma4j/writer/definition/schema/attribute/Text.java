package jp.co.actier.luchta.forma4j.writer.definition.schema.attribute;

import javax.xml.bind.annotation.XmlValue;

public class Text {
    @XmlValue
    String value;

    public Text() {
    }

    public Text(String value) {
        this.value = value;
    }

    public String stripMarker() {
        if (!isVariable()) return value;
        return value
                .replace("#{", "")
                .replace("}", "");
    }

    @Override
    public String toString() {
        if (value == null) return "";
        return value;
    }

    public boolean isVariable() {
        if (value == null) return false;
        return value.startsWith("#");
    }
}
