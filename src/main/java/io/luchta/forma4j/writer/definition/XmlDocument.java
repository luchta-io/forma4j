package io.luchta.forma4j.writer.definition;

import io.luchta.forma4j.writer.definition.schema.Element;
import io.luchta.forma4j.writer.definition.schema.attribute.Name;
import io.luchta.forma4j.writer.definition.schema.attribute.Style;
import io.luchta.forma4j.writer.definition.schema.attribute.index.ColumnIndex;
import io.luchta.forma4j.writer.definition.schema.attribute.index.RowIndex;
import io.luchta.forma4j.writer.definition.schema.element.ListElement;
import io.luchta.forma4j.writer.definition.schema.element.Root;
import io.luchta.forma4j.writer.definition.schema.element.Sheet;

import java.util.ArrayList;
import java.util.List;

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

    public static XmlDocument defaultXmlDocument() {
        ListElement listElement = new ListElement(new RowIndex(0L), new ColumnIndex(0L), new Style(), new Style());
        List<Element> listElements = new ArrayList<>();
        listElements.add(listElement);

        Sheet sheet = new Sheet(new Name("result"), listElements);
        List<Element> sheets = new ArrayList<>();
        sheets.add(sheet);

        Root root = new Root(sheets);

        return new XmlDocument(root);
    }
}
