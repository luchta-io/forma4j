package io.luchta.forma4j.writer.engine.model.row;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class XlsxRowList implements Iterable<XlsxRow> {
    List<XlsxRow> list = new ArrayList<>();

    public XlsxRowList() {
    }

    public XlsxRowList(List<XlsxRow> list) {
        this.list = list;
    }

    @Override
    public Iterator<XlsxRow> iterator() {
        return list.iterator();
    }
}
