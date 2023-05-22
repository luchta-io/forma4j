package io.luchta.forma4j.reader.model.excel;

public class SheetName {
    private String value;

    public SheetName() {}

    public SheetName(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
