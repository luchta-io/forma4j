package jp.co.actier.luchta.forma4j.writer.definition.schema;

public interface Element {
    ElementType type();

    ElementList children();

    boolean hasChildren();
}
