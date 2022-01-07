package io.luchta.forma4j.context.type;

public class StringType implements Type {

    String value;

    public StringType() {
        value = "";
    }

    public StringType(String value) {
        this.value = value;
    }

    public boolean isEmpty() {
        return value == null || value.isEmpty();
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean isStringType() {
        return true;
    }
}
