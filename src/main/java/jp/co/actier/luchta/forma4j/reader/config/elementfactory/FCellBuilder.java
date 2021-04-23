package jp.co.actier.luchta.forma4j.reader.config.elementfactory;

import org.w3c.dom.NamedNodeMap;

import jp.co.actier.luchta.forma4j.reader.config.element.FCell;
import jp.co.actier.luchta.forma4j.reader.config.element.Element;
import jp.co.actier.luchta.forma4j.reader.config.element.property.ColNumber;
import jp.co.actier.luchta.forma4j.reader.config.element.property.ElementName;
import jp.co.actier.luchta.forma4j.reader.config.element.property.RowNumber;

public class FCellBuilder implements ElementBuilder {

    @Override
    public Element build(NamedNodeMap namedNodeMap) {
        Integer rowNumber = Integer.parseInt(namedNodeMap.getNamedItem("row").getNodeValue());
        Integer colNumber = Integer.parseInt(namedNodeMap.getNamedItem("col").getNodeValue());
        String name = namedNodeMap.getNamedItem("name").getNodeValue();
        return new FCell(new RowNumber(rowNumber), new ColNumber(colNumber), new ElementName(name));
    }
}
