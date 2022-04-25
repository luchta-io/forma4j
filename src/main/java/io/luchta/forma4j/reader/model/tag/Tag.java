package io.luchta.forma4j.reader.model.tag;

public interface Tag {

    default boolean isFormaReader() {
        return false;
    }

    default boolean isSheet() {
        return false;
    }

    default boolean isCell() {
        return false;
    }

    default boolean isVFor() {
        return false;
    }

    default boolean isHFor() {
        return false;
    }

    default boolean isBreak() {
        return false;
    }
}
