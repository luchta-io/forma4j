package io.luchta.forma4j.writer.definition.schema;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ElementList implements Iterable<Element> {
    List<Element> list = new ArrayList<>();

    public ElementList() {
    }

    public ElementList(List<Element> list) {
        this.list = list;
    }

    @Override
    public Iterator<Element> iterator() {
        return list.iterator();
    }
}
