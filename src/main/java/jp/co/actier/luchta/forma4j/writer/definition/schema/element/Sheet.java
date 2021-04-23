package jp.co.actier.luchta.forma4j.writer.definition.schema.element;

import jp.co.actier.luchta.forma4j.writer.definition.schema.Element;
import jp.co.actier.luchta.forma4j.writer.definition.schema.ElementList;
import jp.co.actier.luchta.forma4j.writer.definition.schema.ElementType;
import jp.co.actier.luchta.forma4j.writer.definition.schema.attribute.Name;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
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
