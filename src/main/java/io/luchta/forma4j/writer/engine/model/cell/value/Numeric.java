package io.luchta.forma4j.writer.engine.model.cell.value;

import java.math.BigDecimal;

public class Numeric implements XlsxCellValue<BigDecimal> {
    BigDecimal value;

    public Numeric() {
    }

    public Numeric(BigDecimal value) {
        this.value = value;
    }

    public Numeric(String value) {
        if (value == null || value.isEmpty()) {
            this.value = null;
        } else {
            this.value = new BigDecimal(value.trim());
        }
    }

    public BigDecimal getValue() {
        return value;
    }

    @Override
    public boolean isEmpty() {
        return value == null;
    }

    @Override
    public BigDecimal toValue() {
        return value;
    }

    @Override
    public String toString() {
        if (isEmpty()) return "";
        return value.toString();
    }
}
