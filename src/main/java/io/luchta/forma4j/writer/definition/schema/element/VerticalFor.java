package io.luchta.forma4j.writer.definition.schema.element;

import io.luchta.forma4j.writer.definition.schema.Element;
import io.luchta.forma4j.writer.definition.schema.ElementList;
import io.luchta.forma4j.writer.definition.schema.ElementType;
import io.luchta.forma4j.writer.definition.schema.attribute.index.ColumnIndex;
import io.luchta.forma4j.writer.definition.schema.attribute.index.RowIndex;
import io.luchta.forma4j.writer.definition.schema.attribute.loop.Collection;
import io.luchta.forma4j.writer.definition.schema.attribute.loop.Index;
import io.luchta.forma4j.writer.definition.schema.attribute.loop.Item;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElements;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.ArrayList;
import java.util.List;

@XmlRootElement
public class VerticalFor implements Element {
    public static final String ELEMENT_NAME = "vertical-for";

    @XmlAttribute
    Item item = new Item();
    @XmlAttribute
    Index index = new Index();
    @XmlAttribute
    Collection collection = new Collection();

    @XmlAttribute
    RowIndex startRowIndex = new RowIndex();
    @XmlAttribute
    ColumnIndex startColumnIndex = new ColumnIndex();

    @XmlElements({
            @XmlElement(name = Row.ELEMENT_NAME, type = Row.class),
            @XmlElement(name = Column.ELEMENT_NAME, type = Column.class),
            @XmlElement(name = Cell.ELEMENT_NAME, type = Cell.class),
            @XmlElement(name = HorizontalFor.ELEMENT_NAME, type = HorizontalFor.class),
            @XmlElement(name = VerticalFor.ELEMENT_NAME, type = VerticalFor.class),
    })
    List<Element> children = new ArrayList<>();

    public Item item() {
        return item;
    }

    public Index index() {
        return index;
    }

    public Collection collection() {
        return collection;
    }

    public RowIndex startRowIndex() {
        return startRowIndex;
    }

    public ColumnIndex startColumnIndex() {
        return startColumnIndex;
    }

    @Override
    public ElementType type() {
        return ElementType.VERTICAL_FOR;
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
