package io.luchta.forma4j.context.type;

import java.time.LocalDateTime;

public class DateTimeType implements Type {

    LocalDateTime value;

    public DateTimeType() {
        value = null;
    }

    public DateTimeType(LocalDateTime value) {
        this.value = value;
    }

    public boolean isEmpty() {
        return value == null;
    }

    public LocalDateTime toLocalDateTime() {
        return value;
    }

    @Override
    public boolean isDateTimeType() {
        return true;
    }
}
