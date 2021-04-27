package io.luchta.forma4j.reader.config.elementfactory;

public class ElementBuilderFactory {

    public static ElementBuilder newElementBuilder(String nodeName) {
        if ("forma-reader".equals(nodeName)) {
            return new FRootBuilder();
        }
        if ("sheet".equals(nodeName)) {
            return new FSheetBuilder();
        }
        if ("header".equals(nodeName)) {
            return new FHeaderBuilder();
        }
        if ("cell".equals(nodeName)) {
            return new FCellBuilder();
        }
        if ("body".equals(nodeName)) {
            return new FBodyBuilder();
        }
        if ("vertical-for".equals(nodeName)) {
            return new FVerticalForBuilder();
        }
        if ("row".equals(nodeName)) {
            return new FRowBuilder();
        }
        if ("col".equals(nodeName)) {
            return new FColBuilder();
        }
        if ("horizontal-for".equals(nodeName)) {
            return new FHorizontalForBuilder();
        }
        return null;
    }
}
