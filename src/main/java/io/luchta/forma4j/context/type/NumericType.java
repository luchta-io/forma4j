package io.luchta.forma4j.context.type;

import java.math.BigDecimal;

public class NumericType implements Type {

    BigDecimal value;

    public NumericType() {
        value = null;
    }

    public NumericType(BigDecimal value) {
        this.value = value;
    }

    public boolean isEmpty() {
        return value == null;
    }

    public BigDecimal toBigDecimal() {
        return value;
    }

    @Override
    public boolean isNumericType() {
        return true;
    }
}
