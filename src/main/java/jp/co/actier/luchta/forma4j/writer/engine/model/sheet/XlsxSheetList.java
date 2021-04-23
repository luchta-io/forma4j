package jp.co.actier.luchta.forma4j.writer.engine.model.sheet;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class XlsxSheetList implements Iterable<XlsxSheet> {
    List<XlsxSheet> list = new ArrayList<>();

    public XlsxSheetList() {
    }

    public XlsxSheetList(List<XlsxSheet> list) {
        this.list = list;
    }

    @Override
    public Iterator<XlsxSheet> iterator() {
        return list.iterator();
    }
}
