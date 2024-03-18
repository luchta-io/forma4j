package io.luchta.forma4j.writer.definition.schema.element;

import io.luchta.forma4j.writer.definition.schema.Element;
import io.luchta.forma4j.writer.definition.schema.ElementList;
import io.luchta.forma4j.writer.definition.schema.ElementType;
import io.luchta.forma4j.writer.definition.schema.attribute.index.ColumnIndex;
import io.luchta.forma4j.writer.definition.schema.attribute.index.RowIndex;
import io.luchta.forma4j.writer.definition.schema.attribute.row.AutoFilter;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElements;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.ArrayList;
import java.util.List;

@XmlRootElement
public class Row implements Element {
    public static final String ELEMENT_NAME = "row";

    @XmlAttribute
    RowIndex rowIndex = new RowIndex();
    @XmlAttribute
    ColumnIndex startColumnIndex = new ColumnIndex();
    @XmlAttribute
    AutoFilter autoFilter = new AutoFilter();

    @XmlElements({
            @XmlElement(name = Cell.ELEMENT_NAME, type = Cell.class),
            @XmlElement(name = HorizontalFor.ELEMENT_NAME, type = HorizontalFor.class),
            @XmlElement(name = VerticalFor.ELEMENT_NAME, type = VerticalFor.class),
    })
    List<Element> children = new ArrayList<>();

    public RowIndex rowIndex() {
        return rowIndex;
    }

    public ColumnIndex startColumnIndex() {
        return startColumnIndex;
    }

    public AutoFilter autoFilter() {
        return autoFilter;
    }

    @Override
    public ElementType type() {
        return ElementType.ROW;
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
