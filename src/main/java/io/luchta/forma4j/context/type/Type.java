package io.luchta.forma4j.context.type;

public interface Type {
    default boolean isStringType() {
        return false;
    }
    default boolean isNumericType() {
        return false;
    }
    default boolean isDateTimeType() {
        return false;
    }
    default boolean isMapType() {
        return false;
    }
}
