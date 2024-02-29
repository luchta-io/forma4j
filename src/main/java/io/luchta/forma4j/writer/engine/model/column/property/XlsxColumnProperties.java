package io.luchta.forma4j.writer.engine.model.column.property;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class XlsxColumnProperties implements Iterable<XlsxColumnProperty> {
    private List<XlsxColumnProperty> list = new ArrayList<>();

    public XlsxColumnProperties() {
    }

    public XlsxColumnProperties(List<XlsxColumnProperty> list) {
        this.list = list;
    }

    public void add(XlsxColumnProperty property) {
        list.add(property);
    }

    public void addAll(XlsxColumnProperties properties) {
        list.addAll(properties.list);
    }

    public boolean hasProperties() {
        return size() > 0;
    }

    public int size() {
        return list.size();
    }

    @Override
    public Iterator<XlsxColumnProperty> iterator() {
        return list.iterator();
    }
}
