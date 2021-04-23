package jp.co.actier.luchta.forma4j.reader.config.elementfactory;

import org.w3c.dom.NamedNodeMap;

import jp.co.actier.luchta.forma4j.reader.config.element.FBody;
import jp.co.actier.luchta.forma4j.reader.config.element.Element;
import jp.co.actier.luchta.forma4j.reader.config.element.property.RowNumber;

public class FBodyBuilder implements ElementBuilder {

    @Override
    public Element build(NamedNodeMap namedNodeMap) {
        Integer startRow = Integer.parseInt(namedNodeMap.getNamedItem("startRow").getNodeValue());
        return new FBody(new RowNumber(startRow));
    }
}
