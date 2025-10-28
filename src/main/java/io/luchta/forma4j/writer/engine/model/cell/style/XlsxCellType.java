package io.luchta.forma4j.writer.engine.model.cell.style;

import io.luchta.forma4j.writer.engine.model.cell.value.XlsxCellValue;

public enum XlsxCellType {
    NUMERIC,
    DATE,
    DATETIME,
    STRING,
    FORMULA,
    BOOLEAN,
    BLANK;

    public String defaultFormat() {
        if (this == NUMERIC) {
            return "#,##0";
        } else if (this == DATE) {
            return "yyyy/mm/dd";
        } else if (this == DATETIME) {
            return "yyyy/mm/dd hh:mm:ss";
        } else if (this == STRING) {
            return "@";
        }
        return null;
    }
}
