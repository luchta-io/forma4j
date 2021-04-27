package io.luchta.forma4j.reader.config.elementfactory;

import io.luchta.forma4j.reader.config.element.Element;
import io.luchta.forma4j.reader.config.element.FBody;
import io.luchta.forma4j.reader.config.element.property.RowNumber;
import org.w3c.dom.NamedNodeMap;

public class FBodyBuilder implements ElementBuilder {

    @Override
    public Element build(NamedNodeMap namedNodeMap) {
        Integer startRow = Integer.parseInt(namedNodeMap.getNamedItem("startRow").getNodeValue());
        return new FBody(new RowNumber(startRow));
    }
}
