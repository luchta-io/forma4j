package io.luchta.forma4j.reader.compile.parse.tagbuilder;

import io.luchta.forma4j.context.syntax.SyntaxErrors;
import io.luchta.forma4j.reader.model.tag.ListTag;
import io.luchta.forma4j.reader.model.tag.Tag;
import io.luchta.forma4j.reader.model.tag.property.Index;
import io.luchta.forma4j.reader.model.tag.property.Name;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.List;

public class ListBuilder implements TagBuilder {
    @Override
    public Tag build(NamedNodeMap nodeMap, SyntaxErrors syntaxErrors) {
        List<String> errorMessages = new ArrayList<>();

        Node headerStartRowNode = nodeMap.getNamedItem("headerStartRow");
        Integer headerStartRow = 0;
        if (headerStartRowNode != null) {
            try {
                headerStartRow = Integer.parseInt(headerStartRowNode.getNodeValue());
            } catch (NumberFormatException e) {
                errorMessages.add("headerStartRow には数値を指定してください");
            }
        }

        Node headerStartColNode = nodeMap.getNamedItem("headerStartCol");
        Integer headerStartCol = 0;
        if (headerStartColNode != null) {
            try {
                headerStartCol = Integer.parseInt(headerStartColNode.getNodeValue());
            } catch (NumberFormatException e) {
                errorMessages.add("headerStartCol には数値を指定してください");
            }
        }

        Node detailStartRowNode = nodeMap.getNamedItem("detailStartRow");
        Integer detailStartRow = headerStartRow + 1;
        if (detailStartRowNode != null) {
            try {
                detailStartRow = Integer.parseInt(detailStartRowNode.getNodeValue());
            } catch (NumberFormatException e) {
                errorMessages.add("detailStartRow には数値を指定してください");
            }
        }

        Node detailStartColNode = nodeMap.getNamedItem("detailStartCol");
        Integer detailStartCol = 0;
        if (detailStartColNode != null) {
            try {
                detailStartCol = Integer.parseInt(detailStartColNode.getNodeValue());
            } catch (NumberFormatException e) {
                errorMessages.add("detailStartCol には数値を指定してください");
            }
        }

        Node nameNode = nodeMap.getNamedItem("name");
        String name = "";
        if (nameNode != null) {
            name = nameNode.getNodeValue();
        } else {
            name = "list";
        }

        return new ListTag(
                new Index(headerStartRow),
                new Index(headerStartCol),
                new Index(detailStartRow),
                new Index(detailStartCol),
                new Name(name)
        );
    }
}
