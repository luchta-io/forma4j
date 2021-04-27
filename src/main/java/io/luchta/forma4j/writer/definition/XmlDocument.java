package io.luchta.forma4j.writer.definition;

import io.luchta.forma4j.writer.definition.schema.element.Root;

public class XmlDocument {
    Root root = new Root();

    public XmlDocument() {
    }

    public XmlDocument(Root root) {
        this.root = root;
    }

    public Root root() {
        return root;
    }
}
