package io.luchta.forma4j.writer.definition.schema.attribute.loop;

import jakarta.xml.bind.annotation.XmlValue;

import java.util.Objects;

public class Collection {
    @XmlValue
    String value;

    public Collection() {
    }

    public Collection(String value) {
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
        if (!(o instanceof Collection)) return false;
        Collection that = (Collection) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
