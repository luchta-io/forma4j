package io.luchta.forma4j.writer.definition.schema;

public interface Element {
    ElementType type();

    ElementList children();

    boolean hasChildren();
}
