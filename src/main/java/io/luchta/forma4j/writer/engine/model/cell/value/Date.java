package io.luchta.forma4j.writer.engine.model.cell.value;

import java.time.LocalDate;

public class Date implements XlsxCellValue {
    LocalDate value;

    public Date() {
    }

    public Date(LocalDate value) {
        this.value = value;
    }

    @Override
    public String toString() {
        if (value == null) return "";
        return value.toString();
    }
}
