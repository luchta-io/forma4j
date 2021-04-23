package jp.co.actier.luchta.forma4j.reader.config.elementfactory;

import org.w3c.dom.NamedNodeMap;

import jp.co.actier.luchta.forma4j.reader.config.element.Element;
import jp.co.actier.luchta.forma4j.reader.config.element.FHeader;
import jp.co.actier.luchta.forma4j.reader.config.element.property.RowNumber;

public class FHeaderBuilder implements ElementBuilder {

    @Override
    public Element build(NamedNodeMap namedNodeMap) {
        Integer startRow = Integer.parseInt(namedNodeMap.getNamedItem("startRow").getNodeValue());
        Integer endRow = Integer.parseInt(namedNodeMap.getNamedItem("endRow").getNodeValue());
        return new FHeader(new RowNumber(startRow), new RowNumber(endRow));
    }
}
