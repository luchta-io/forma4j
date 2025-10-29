package io.luchta.forma4j.writer.engine.model.cell.value;

import java.time.LocalDate;

public class Date implements XlsxCellValue<LocalDate> {
    LocalDate value;

    public Date() {
    }

    public Date(LocalDate value) {
        this.value = value;
    }

    @Override
    public boolean isEmpty() {
        return value == null;
    }

    @Override
    public boolean isDate() {
        return true;
    }

    @Override
    public LocalDate toValue() {
        return value;
    }

    @Override
    public String toString() {
        if (value == null) return "";
        return value.toString();
    }
}
