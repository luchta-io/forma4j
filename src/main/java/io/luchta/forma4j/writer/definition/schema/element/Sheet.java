package io.luchta.forma4j.writer.definition.schema.element;

import io.luchta.forma4j.writer.definition.schema.Element;
import io.luchta.forma4j.writer.definition.schema.ElementList;
import io.luchta.forma4j.writer.definition.schema.ElementType;
import io.luchta.forma4j.writer.definition.schema.attribute.Name;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElements;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.ArrayList;
import java.util.List;

@XmlRootElement
public class Sheet implements Element {
    public static final String ELEMENT_NAME = "sheet";

    @XmlAttribute
    Name name = new Name();

    @XmlElements({
            @XmlElement(name = Row.ELEMENT_NAME, type = Row.class),
            @XmlElement(name = Column.ELEMENT_NAME, type = Column.class),
            @XmlElement(name = Cell.ELEMENT_NAME, type = Cell.class),
            @XmlElement(name = HorizontalFor.ELEMENT_NAME, type = HorizontalFor.class),
            @XmlElement(name = VerticalFor.ELEMENT_NAME, type = VerticalFor.class),
            @XmlElement(name = ListElement.ELEMENT_NAME, type = ListElement.class)
    })
    List<Element> children = new ArrayList<>();

    public Name name() {
        return name;
    }

    @Override
    public ElementType type() {
        return ElementType.SHEET;
    }

    @Override
    public ElementList children() {
        return new ElementList(children);
    }

    @Override
    public boolean hasChildren() {
        return !children.isEmpty();
    }
}
