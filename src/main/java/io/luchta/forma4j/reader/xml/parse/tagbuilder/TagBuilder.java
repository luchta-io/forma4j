package io.luchta.forma4j.reader.xml.parse.tagbuilder;

import io.luchta.forma4j.context.syntax.SyntaxErrors;
import io.luchta.forma4j.reader.model.tag.Tag;
import org.w3c.dom.NamedNodeMap;

public interface TagBuilder {
    Tag build(NamedNodeMap nodeMap, SyntaxErrors syntaxErrors);
}
