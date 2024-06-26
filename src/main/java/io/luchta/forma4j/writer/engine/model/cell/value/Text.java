package io.luchta.forma4j.writer.engine.model.cell.value;

public class Text implements XlsxCellValue {
    String value;

    public Text() {
    }

    public Text(String value) {
        this.value = value;
    }

    @Override
    public boolean isEmpty() {
        return value == null || "".equals(value);
    }

    @Override
    public String toString() {
        if (value == null) return "";
        return value;
    }
}
