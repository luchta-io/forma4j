package io.luchta.forma4j.reader.compile.parse.tagbuilder;

import io.luchta.forma4j.context.syntax.SyntaxError;
import io.luchta.forma4j.context.syntax.SyntaxErrors;
import io.luchta.forma4j.reader.model.tag.HeaderTag;
import io.luchta.forma4j.reader.model.tag.Tag;
import io.luchta.forma4j.reader.model.tag.property.Index;
import io.luchta.forma4j.reader.model.tag.property.Name;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.List;

public class HeaderBuilder implements TagBuilder {

    @Override
    public Tag build(NamedNodeMap nodeMap, SyntaxErrors syntaxErrors) {

        List<String> errorMessages = new ArrayList<>();

        Node rowNode = nodeMap.getNamedItem("row");
        if (rowNode == null) {
            errorMessages.add("row は必須入力です。0 以上の整数を指定してください");
        }
        Integer rowNumber = convertNodeValueToInteger(rowNode);
        if (rowNumber == null) {
            errorMessages.add("row には 0 以上の整数を指定してください");
        }

        Node colNode = nodeMap.getNamedItem("col");
        if (colNode == null) {
            errorMessages.add("col は必須入力です。0 以上の整数を指定してください");
        }
        Integer colNumber = convertNodeValueToInteger(colNode);
        if (colNumber == null) {
            errorMessages.add("col には 0 以上の整数を指定してください");
        }

        String name = convertNodeValueToString(nodeMap.getNamedItem("name"));
        if (name == null) {
            errorMessages.add("name は必須入力です");
        }

        for (String message : errorMessages) {
            SyntaxError syntaxError = new SyntaxError(message, new UnsupportedOperationException());
            syntaxErrors.add(syntaxError);
        }

        return new HeaderTag(
                new Index(rowNumber),
                new Index(colNumber),
                new Name(name)
        );
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
}
