package jp.co.actier.luchta.forma4j.writer.definition.schema.element;

import jp.co.actier.luchta.forma4j.writer.definition.schema.Element;
import jp.co.actier.luchta.forma4j.writer.definition.schema.ElementList;
import jp.co.actier.luchta.forma4j.writer.definition.schema.ElementType;
import jp.co.actier.luchta.forma4j.writer.definition.schema.attribute.Style;
import jp.co.actier.luchta.forma4j.writer.definition.schema.attribute.Text;
import jp.co.actier.luchta.forma4j.writer.definition.schema.attribute.index.ColumnIndex;
import jp.co.actier.luchta.forma4j.writer.definition.schema.attribute.index.RowIndex;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

@XmlRootElement
public class Cell implements Element {
    public static final String ELEMENT_NAME = "cell";

    @XmlAttribute
    ColumnIndex columnIndex = new ColumnIndex();
    @XmlAttribute
    RowIndex rowIndex = new RowIndex();
    @XmlAttribute
    Style style = new Style();
    @XmlValue
    Text text = new Text();

    public ColumnIndex columnIndex() {
        return columnIndex;
    }

    public RowIndex rowIndex() {
        return rowIndex;
    }
    
    public Style style() {
    	return style;
    }

    public Text text() {
        return text;
    }

    @Override
    public ElementType type() {
        return ElementType.CELL;
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
