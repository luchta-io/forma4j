package io.luchta.forma4j.reader.compile.parse.tagbuilder;

import io.luchta.forma4j.context.syntax.SyntaxError;
import io.luchta.forma4j.context.syntax.SyntaxErrors;
import io.luchta.forma4j.reader.model.tag.Tag;
import io.luchta.forma4j.reader.model.tag.VForTag;
import io.luchta.forma4j.reader.model.tag.property.Index;
import io.luchta.forma4j.reader.model.tag.property.Name;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.List;

public class VForBuilder implements TagBuilder {

    @Override
    public Tag build(NamedNodeMap nodeMap, SyntaxErrors syntaxErrors) {

        List<String> errorMessages = new ArrayList<>();

        Node fromNode = nodeMap.getNamedItem("from");
        boolean fromIsUndefined = false;
        if (fromNode == null) {
            fromIsUndefined = true;
        }
        Integer fromNumber = convertNodeValueToInteger(fromNode);
        if (fromNumber == null) {
            errorMessages.add("from には0以上の整数を指定してください");
        }

        Node toNode = nodeMap.getNamedItem("to");
        boolean toIsUndefined = false;
        if (toNode == null) {
            toIsUndefined = true;
        }
        Integer toNumber = convertNodeValueToInteger(nodeMap.getNamedItem("to"));
        if (toNumber == null) {
            errorMessages.add("to には0以上の整数を指定してください");
        }

        String name = convertNodeValueToString(nodeMap.getNamedItem("name"));
        if (name == null) {
            errorMessages.add("name は必須入力です");
        }

        String header = convertNodeValueToString(nodeMap.getNamedItem("header"));

        addSyntaxErrors(errorMessages, syntaxErrors);

        return new VForTag(new Index(fromNumber), fromIsUndefined, new Index(toNumber), toIsUndefined, new Name(name));
    }

    private Integer convertNodeValueToInteger(Node node) {

        Integer value = null;
        if (node == null) {
            value = 0;
        } else {
            Integer rowNumber = null;
            try {
                rowNumber = Integer.parseInt(node.getNodeValue());
            } catch (NumberFormatException e) {
                return value;
            }

            if (rowNumber >= 0) {
                value = Integer.parseInt(node.getNodeValue());
            }
        }
        return value;
    }

    private String convertNodeValueToString(Node node) {

        String value = "";
        if (node != null) {
            value = node.getNodeValue();
        }
        return value;
    }

    private void addSyntaxErrors(List<String> errorMessages, SyntaxErrors syntaxErrors) {

        for (String message : errorMessages) {
            SyntaxError syntaxError = new SyntaxError(message, new UnsupportedOperationException());
            syntaxErrors.add(syntaxError);
        }
    }
}
