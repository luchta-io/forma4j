package io.luchta.forma4j.reader.compile.parse.tagbuilder;

import io.luchta.forma4j.context.syntax.SyntaxError;
import io.luchta.forma4j.context.syntax.SyntaxErrors;
import io.luchta.forma4j.reader.model.tag.CellTag;
import io.luchta.forma4j.reader.model.tag.Tag;
import io.luchta.forma4j.reader.model.tag.property.Index;
import io.luchta.forma4j.reader.model.tag.property.Name;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.List;

public class CellBuilder implements TagBuilder {

    @Override
    public Tag build(NamedNodeMap nodeMap, SyntaxErrors syntaxErrors) {

        List<String> errorMessages = new ArrayList<>();

        Node rowNode = nodeMap.getNamedItem("row");
        boolean rowIsUndefined = false;
        if (rowNode == null) {
            rowIsUndefined = true;
        }
        Integer rowNumber = convertNodeValueToInteger(rowNode);
        if (rowNumber == null) {
            errorMessages.add("row には0以上の整数を指定してください");
        }

        Node colNode = nodeMap.getNamedItem("col");
        boolean colIsUndefined = false;
        if (colNode == null) {
            colIsUndefined = true;
        }
        Integer colNumber = convertNodeValueToInteger(nodeMap.getNamedItem("col"));
        if (colNumber == null) {
            errorMessages.add("col には0以上の整数を指定してください");
        }

        String name = convertNodeValueToString(nodeMap.getNamedItem("name"));
        if (name == null) {
            errorMessages.add("name は必須入力です");
        }

        addSyntaxErrors(errorMessages, syntaxErrors);

        return new CellTag(new Index(rowNumber), rowIsUndefined, new Index(colNumber), colIsUndefined, new Name(name));
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
