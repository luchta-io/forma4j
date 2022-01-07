package io.luchta.forma4j.reader.compile.parse.tagbuilder;

import io.luchta.forma4j.context.syntax.SyntaxErrors;
import io.luchta.forma4j.reader.model.tag.BreakTag;
import io.luchta.forma4j.reader.model.tag.Tag;
import io.luchta.forma4j.reader.model.tag.property.IF;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class BreakBuilder implements TagBuilder {

    @Override
    public Tag build(NamedNodeMap nodeMap, SyntaxErrors syntaxErrors) {
        String condition = convertNodeValueToString(nodeMap.getNamedItem("if"));
        return new BreakTag(new IF(condition));
    }

    private String convertNodeValueToString(Node node) {

        String value = "blank-all";
        if (node != null) {
            value = node.getNodeValue();
        }
        return value;
    }
}
