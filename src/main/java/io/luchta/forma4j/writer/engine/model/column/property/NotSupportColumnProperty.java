package io.luchta.forma4j.writer.engine.model.column.property;

import java.util.Objects;
import java.util.logging.Logger;

public class NotSupportColumnProperty implements XlsxColumnProperty {
    Logger logger = Logger.getLogger(NotSupportColumnProperty.class.getName());

    String name;
    String value;

    public NotSupportColumnProperty(String name, String value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NotSupportColumnProperty)) return false;
        NotSupportColumnProperty that = (NotSupportColumnProperty) o;
        return Objects.equals(name, that.name) && Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, value);
    }
}
