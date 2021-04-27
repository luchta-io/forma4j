package io.luchta.forma4j.reader.config.elementfactory;

import io.luchta.forma4j.reader.config.element.Element;
import io.luchta.forma4j.reader.config.element.FCell;
import io.luchta.forma4j.reader.config.element.property.ColNumber;
import io.luchta.forma4j.reader.config.element.property.ElementName;
import io.luchta.forma4j.reader.config.element.property.RowNumber;
import org.w3c.dom.NamedNodeMap;

public class FCellBuilder implements ElementBuilder {

    @Override
    public Element build(NamedNodeMap namedNodeMap) {
        Integer rowNumber = Integer.parseInt(namedNodeMap.getNamedItem("row").getNodeValue());
        Integer colNumber = Integer.parseInt(namedNodeMap.getNamedItem("col").getNodeValue());
        String name = namedNodeMap.getNamedItem("name").getNodeValue();
        return new FCell(new RowNumber(rowNumber), new ColNumber(colNumber), new ElementName(name));
    }
}
