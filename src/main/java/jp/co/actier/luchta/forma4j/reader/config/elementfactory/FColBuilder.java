package jp.co.actier.luchta.forma4j.reader.config.elementfactory;

import org.w3c.dom.NamedNodeMap;

import jp.co.actier.luchta.forma4j.reader.config.element.FCol;
import jp.co.actier.luchta.forma4j.reader.config.element.Element;
import jp.co.actier.luchta.forma4j.reader.config.element.property.ColNumber;
import jp.co.actier.luchta.forma4j.reader.config.element.property.ElementName;

public class FColBuilder implements ElementBuilder {

    @Override
    public Element build(NamedNodeMap namedNodeMap) {
        Integer num = Integer.parseInt(namedNodeMap.getNamedItem("num").getNodeValue());
        String name = namedNodeMap.getNamedItem("name").getNodeValue();
        return new FCol(new ColNumber(num), new ElementName(name));
    }
}
