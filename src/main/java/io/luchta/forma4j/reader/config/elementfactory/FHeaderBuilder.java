package io.luchta.forma4j.reader.config.elementfactory;

import io.luchta.forma4j.reader.config.element.Element;
import io.luchta.forma4j.reader.config.element.FHeader;
import io.luchta.forma4j.reader.config.element.property.RowNumber;
import org.w3c.dom.NamedNodeMap;

public class FHeaderBuilder implements ElementBuilder {

    @Override
    public Element build(NamedNodeMap namedNodeMap) {
        Integer startRow = Integer.parseInt(namedNodeMap.getNamedItem("startRow").getNodeValue());
        Integer endRow = Integer.parseInt(namedNodeMap.getNamedItem("endRow").getNodeValue());
        return new FHeader(new RowNumber(startRow), new RowNumber(endRow));
    }
}
