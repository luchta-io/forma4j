package jp.co.actier.luchta.forma4j.reader.config.elementfactory;

import org.w3c.dom.NamedNodeMap;

import jp.co.actier.luchta.forma4j.reader.config.element.Element;
import jp.co.actier.luchta.forma4j.reader.config.element.FSheet;
import jp.co.actier.luchta.forma4j.reader.config.element.property.SheetIndex;

public class FSheetBuilder implements ElementBuilder {

    @Override
    public Element build(NamedNodeMap namedNodeMap) {
        Integer index = Integer.parseInt(namedNodeMap.getNamedItem("index").getNodeValue());
        return new FSheet(new SheetIndex(index));
    }
}
