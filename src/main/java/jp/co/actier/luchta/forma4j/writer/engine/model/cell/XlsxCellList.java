package jp.co.actier.luchta.forma4j.writer.engine.model.cell;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class XlsxCellList implements Iterable<XlsxCell> {
    List<XlsxCell> list = new ArrayList<>();

    public XlsxCellList() {
    }

    public XlsxCellList(List<XlsxCell> list) {
        this.list = list;
    }

    @Override
    public Iterator<XlsxCell> iterator() {
        return list.iterator();
    }
}
