package io.luchta.forma4j.reader.compile.parse.tagbuilder;

import org.w3c.dom.Node;

public class TagBuilderFactory {

    public TagBuilder create(Node node) {

        String nodeName = node.getNodeName();

        if ("forma-reader".equals(nodeName)) {
            return new FormaReaderBuilder();
        }

        if ("sheet".equals(nodeName)) {
            return new SheetBuilder();
        }

        if ("cell".equals(nodeName)) {
            return new CellBuilder();
        }

        if ("v-for".equals(nodeName)) {
            return new VForBuilder();
        }

        if ("h-for".equals(nodeName)) {
            return new HForBuilder();
        }

        if ("list".equals(nodeName)) {
            return new ListBuilder();
        }

        return null;
    }
}
