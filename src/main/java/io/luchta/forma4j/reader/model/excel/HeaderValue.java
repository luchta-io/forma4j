package io.luchta.forma4j.reader.model.excel;

public class HeaderValue {
    String value;

    public HeaderValue() {
        value = "";
    }

    public HeaderValue(String value) {
        this.value = value;
    }

    public boolean isEmpty() {
        return value == null || "".equals(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
