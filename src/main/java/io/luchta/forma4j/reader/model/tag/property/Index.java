package io.luchta.forma4j.reader.model.tag.property;

public class Index {

    Integer value;

    public Index() {
        value = null;
    }

    public Index(Integer value) {
        this.value = value;
    }

    public Integer toInteger() {
        return value;
    }
}
