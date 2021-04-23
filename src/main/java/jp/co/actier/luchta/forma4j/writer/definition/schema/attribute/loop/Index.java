package jp.co.actier.luchta.forma4j.writer.definition.schema.attribute.loop;

import javax.xml.bind.annotation.XmlValue;
import java.util.Objects;

public class Index {
    @XmlValue
    String value;

    public Index() {
    }

    public Index(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        if (value == null) return "";
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Index)) return false;
        Index item = (Index) o;
        return Objects.equals(value, item.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
