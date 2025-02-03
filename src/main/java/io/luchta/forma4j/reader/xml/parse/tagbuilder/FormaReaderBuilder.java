package io.luchta.forma4j.reader.xml.parse.tagbuilder;

import io.luchta.forma4j.context.syntax.SyntaxErrors;
import io.luchta.forma4j.reader.model.tag.FormaReaderTag;
import io.luchta.forma4j.reader.model.tag.Tag;
import org.w3c.dom.NamedNodeMap;

public class FormaReaderBuilder implements TagBuilder {

    @Override
    public Tag build(NamedNodeMap nodeMap, SyntaxErrors syntaxErrors) {
        return new FormaReaderTag();
    }
}
