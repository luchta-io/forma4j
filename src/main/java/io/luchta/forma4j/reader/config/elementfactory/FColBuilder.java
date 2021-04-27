package io.luchta.forma4j.reader.config.elementfactory;

import io.luchta.forma4j.reader.config.element.Element;
import io.luchta.forma4j.reader.config.element.FCol;
import io.luchta.forma4j.reader.config.element.property.ColNumber;
import io.luchta.forma4j.reader.config.element.property.ElementName;
import org.w3c.dom.NamedNodeMap;

public class FColBuilder implements ElementBuilder {

    @Override
    public Element build(NamedNodeMap namedNodeMap) {
        Integer num = Integer.parseInt(namedNodeMap.getNamedItem("num").getNodeValue());
        String name = namedNodeMap.getNamedItem("name").getNodeValue();
        return new FCol(new ColNumber(num), new ElementName(name));
    }
}
