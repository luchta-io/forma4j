package io.luchta.forma4j.writer.definition.schema.element;

import io.luchta.forma4j.writer.definition.schema.Element;
import io.luchta.forma4j.writer.definition.schema.ElementList;
import io.luchta.forma4j.writer.definition.schema.ElementType;
import io.luchta.forma4j.writer.definition.schema.attribute.index.ColumnIndex;
import io.luchta.forma4j.writer.definition.schema.attribute.index.RowIndex;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ListElement implements Element {
    public static final String ELEMENT_NAME = "list";

    @XmlAttribute
    RowIndex startRowIndex = new RowIndex();
    @XmlAttribute
    ColumnIndex startColumnIndex = new ColumnIndex();

    public RowIndex startRowIndex() {
        return startRowIndex;
    }

    public ColumnIndex startColumnIndex() {
        return startColumnIndex;
    }

    @Override
    public ElementType type() {
        return ElementType.LIST;
    }

    @Override
    public ElementList children() {
        return new ElementList();
    }

    @Override
    public boolean hasChildren() {
        return false;
    }
}
