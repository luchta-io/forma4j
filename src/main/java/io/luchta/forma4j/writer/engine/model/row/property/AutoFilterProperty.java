package io.luchta.forma4j.writer.engine.model.row.property;

import java.util.Objects;
import java.util.logging.Logger;

public class AutoFilterProperty implements XlsxRowProperty {
    private Logger logger = Logger.getLogger(AutoFilterProperty.class.getName());

    private Boolean value;

    public AutoFilterProperty(Boolean value) {
        this.value = value;
    }

    public static AutoFilterProperty create(String value) {
        if (value == null) {
            return new AutoFilterProperty(false);
        }

        String lowerValue = value.toLowerCase();
        if ("true".equals(lowerValue)) {
            return new AutoFilterProperty(true);
        }
        return new AutoFilterProperty(false);
    }

    public boolean booleanValue() {
        return value.booleanValue();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AutoFilterProperty)) return false;
        AutoFilterProperty that = (AutoFilterProperty) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
