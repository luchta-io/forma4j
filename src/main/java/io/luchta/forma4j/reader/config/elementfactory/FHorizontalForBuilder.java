package io.luchta.forma4j.reader.config.elementfactory;

import io.luchta.forma4j.reader.config.element.Element;
import io.luchta.forma4j.reader.config.element.FHorizontalFor;
import io.luchta.forma4j.reader.config.element.property.ElementName;
import org.w3c.dom.NamedNodeMap;

public class FHorizontalForBuilder implements ElementBuilder {

    @Override
    public Element build(NamedNodeMap namedNodeMap) {
        String name = namedNodeMap.getNamedItem("name").getNodeValue();
        return new FHorizontalFor(new ElementName(name));
    }
}
