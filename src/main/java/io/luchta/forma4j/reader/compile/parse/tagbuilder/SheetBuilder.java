package io.luchta.forma4j.reader.compile.parse.tagbuilder;

import io.luchta.forma4j.context.syntax.SyntaxError;
import io.luchta.forma4j.context.syntax.SyntaxErrors;
import io.luchta.forma4j.reader.model.tag.SheetTag;
import io.luchta.forma4j.reader.model.tag.Tag;
import io.luchta.forma4j.reader.model.tag.property.Name;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class SheetBuilder implements TagBuilder {

    @Override
    public Tag build(NamedNodeMap nodeMap, SyntaxErrors syntaxErrors) {

        String name = convertNodeValueToString(nodeMap.getNamedItem("name"));
        if (name == null) {
            SyntaxError syntaxError = new SyntaxError("name は必須入力です", new UnsupportedOperationException());
            syntaxErrors.add(syntaxError);
        }
        return new SheetTag(new Name(name));
    }

    private String convertNodeValueToString(Node node) {

        String value = "";
        if (node != null) {
            value = node.getNodeValue();
        }
        return value;
    }
}
