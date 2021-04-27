package io.luchta.forma4j.reader.config.elementfactory;

import io.luchta.forma4j.reader.config.element.Element;
import io.luchta.forma4j.reader.config.element.FSheet;
import io.luchta.forma4j.reader.config.element.property.SheetIndex;
import org.w3c.dom.NamedNodeMap;

public class FSheetBuilder implements ElementBuilder {

    @Override
    public Element build(NamedNodeMap namedNodeMap) {
        Integer index = Integer.parseInt(namedNodeMap.getNamedItem("index").getNodeValue());
        return new FSheet(new SheetIndex(index));
    }
}
