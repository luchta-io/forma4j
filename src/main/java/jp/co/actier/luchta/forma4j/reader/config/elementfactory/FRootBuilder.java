package jp.co.actier.luchta.forma4j.reader.config.elementfactory;

import org.w3c.dom.NamedNodeMap;

import jp.co.actier.luchta.forma4j.reader.config.element.Element;
import jp.co.actier.luchta.forma4j.reader.config.element.FRoot;

public class FRootBuilder implements ElementBuilder {

    @Override
    public Element build(NamedNodeMap namedNodeMap) {
        return new FRoot();
    }
}
