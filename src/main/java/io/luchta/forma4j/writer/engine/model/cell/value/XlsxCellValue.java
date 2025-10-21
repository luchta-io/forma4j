package io.luchta.forma4j.writer.engine.model.cell.value;

public interface XlsxCellValue<T> {
    boolean isEmpty();
    T toValue();

    default boolean isBoolean() {
        return false;
    }

    default boolean isNumeric() {
        return false;
    }

    default boolean isDate() {
        return false;
    }

    default boolean isDateTime() {
        return false;
    }

    default boolean isText() {
        return false;
    }
}
