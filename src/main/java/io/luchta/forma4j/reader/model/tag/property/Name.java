package io.luchta.forma4j.reader.model.tag.property;

public class Name {

    String value;

    public Name() {
        value = "";
    }

    public Name(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
