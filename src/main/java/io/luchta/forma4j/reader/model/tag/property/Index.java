package io.luchta.forma4j.reader.model.tag.property;

/**
 * {@code Index} は0始まりのインデックスを表すバリューオブジェクトです
 */
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

    public boolean isEmpty() {
        return value == null;
    }
}
