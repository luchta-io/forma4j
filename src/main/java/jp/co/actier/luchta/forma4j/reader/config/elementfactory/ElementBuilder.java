package jp.co.actier.luchta.forma4j.reader.config.elementfactory;

import org.w3c.dom.NamedNodeMap;

import jp.co.actier.luchta.forma4j.reader.config.element.Element;

public interface ElementBuilder {

    Element build(NamedNodeMap namedNodeMap);
}
