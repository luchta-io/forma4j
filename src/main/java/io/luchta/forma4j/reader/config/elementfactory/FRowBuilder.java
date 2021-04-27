package io.luchta.forma4j.reader.config.elementfactory;

import io.luchta.forma4j.reader.config.element.Element;
import io.luchta.forma4j.reader.config.element.FRow;
import io.luchta.forma4j.reader.config.element.property.ElementName;
import org.w3c.dom.NamedNodeMap;

public class FRowBuilder implements ElementBuilder {

    @Override
    public Element build(NamedNodeMap namedNodeMap) {
        String name = namedNodeMap.getNamedItem("name").getNodeValue();
        return new FRow(new ElementName(name));
    }
}
