package io.luchta.forma4j.writer.engine.model.cell.value;

public class Bool implements XlsxCellValue<Boolean> {
    Boolean value;

    public Bool() {
    }

    public Bool(Boolean value) {
        this.value = value;
    }

    public Bool(String value) {
        if (value == null || value.isEmpty()) {
            this.value = null;
        } else {
            this.value = Boolean.valueOf(value.trim());
        }
    }

    public Boolean getValue() {
        return value;
    }

    @Override
    public boolean isEmpty() {
        return value == null;
    }

    @Override
    public boolean isBoolean() {
        return true;
    }

    @Override
    public Boolean toValue() {
        return value;
    }

    @Override
    public String toString() {
        if (isEmpty()) return "";
        return value.toString();
    }
}
