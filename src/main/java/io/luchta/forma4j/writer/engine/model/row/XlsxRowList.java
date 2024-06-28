package io.luchta.forma4j.writer.engine.model.row;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class XlsxRowList implements Iterable<XlsxRow> {
    List<XlsxRow> list = new ArrayList<>();

    public XlsxRowList() {
    }

    public XlsxRowList(List<XlsxRow> list) {
        this.list = list;
    }

    public XlsxRow getLongest() {
        return list.stream()
                .max(Comparator.comparingInt(row -> row.cells().size()))
                .orElseThrow();
    }

    @Override
    public Iterator<XlsxRow> iterator() {
        return list.iterator();
    }
}
