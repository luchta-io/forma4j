package jp.co.actier.luchta.forma4j.writer.definition.schema.element;

import jp.co.actier.luchta.forma4j.writer.definition.schema.Element;
import jp.co.actier.luchta.forma4j.writer.definition.schema.ElementList;
import jp.co.actier.luchta.forma4j.writer.definition.schema.ElementType;
import jp.co.actier.luchta.forma4j.writer.definition.schema.attribute.index.ColumnIndex;
import jp.co.actier.luchta.forma4j.writer.definition.schema.attribute.index.RowIndex;
import jp.co.actier.luchta.forma4j.writer.definition.schema.attribute.loop.Collection;
import jp.co.actier.luchta.forma4j.writer.definition.schema.attribute.loop.Index;
import jp.co.actier.luchta.forma4j.writer.definition.schema.attribute.loop.Item;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement
public class HorizontalFor implements Element {
    public static final String ELEMENT_NAME = "horizontal-for";

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
        return ElementType.HORIZONTAL_FOR;
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
