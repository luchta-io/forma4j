package jp.co.actier.luchta.forma4j.reader.config.elementfactory;

import org.w3c.dom.NamedNodeMap;

import jp.co.actier.luchta.forma4j.reader.config.element.Element;
import jp.co.actier.luchta.forma4j.reader.config.element.FHorizontalFor;
import jp.co.actier.luchta.forma4j.reader.config.element.property.ElementName;

public class FHorizontalForBuilder implements ElementBuilder {

    @Override
    public Element build(NamedNodeMap namedNodeMap) {
        String name = namedNodeMap.getNamedItem("name").getNodeValue();
        return new FHorizontalFor(new ElementName(name));
    }
}
