package io.luchta.forma4j.reader.config.elementfactory;

import io.luchta.forma4j.reader.config.element.Element;
import org.w3c.dom.NamedNodeMap;

public interface ElementBuilder {

    Element build(NamedNodeMap namedNodeMap);
}
