package io.luchta.forma4j.writer.definition.schema.attribute.loop;

import javax.xml.bind.annotation.XmlValue;
import java.util.Objects;

public class Item {
    @XmlValue
    String value;

    public Item() {
    }

    public Item(String value) {
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
        if (!(o instanceof Item)) return false;
        Item item = (Item) o;
        return Objects.equals(value, item.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
