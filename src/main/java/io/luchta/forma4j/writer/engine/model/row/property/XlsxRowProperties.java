package io.luchta.forma4j.writer.engine.model.row.property;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class XlsxRowProperties  implements Iterable<XlsxRowProperty> {
    List<XlsxRowProperty> list = new ArrayList<>();

    public XlsxRowProperties() {
    }

    public XlsxRowProperties(List<XlsxRowProperty> list) {
        this.list = list;
    }

    public int size() {
        return list.size();
    }

    public XlsxRowProperties add(XlsxRowProperty property) {
        list.add(property);
        return new XlsxRowProperties(list);
    }

    @Override
    public Iterator<XlsxRowProperty> iterator() {
        return list.iterator();
    }
}
