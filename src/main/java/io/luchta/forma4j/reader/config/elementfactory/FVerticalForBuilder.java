package io.luchta.forma4j.reader.config.elementfactory;

import io.luchta.forma4j.reader.config.element.Element;
import io.luchta.forma4j.reader.config.element.FVerticalFor;
import io.luchta.forma4j.reader.config.element.property.ElementName;
import org.w3c.dom.NamedNodeMap;

public class FVerticalForBuilder implements ElementBuilder {

    @Override
    public Element build(NamedNodeMap namedNodeMap) {
        String name = namedNodeMap.getNamedItem("name").getNodeValue();
        return new FVerticalFor(new ElementName(name));
    }
}
