package jp.co.actier.luchta.forma4j.writer.engine.buffer.accumulater.support;

import jp.co.actier.luchta.forma4j.writer.engine.model.cell.address.XlsxRowNumber;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RowNumberList implements Iterable<XlsxRowNumber> {
    List<XlsxRowNumber> list = new ArrayList<>();

    public RowNumberList() {
    }

    public RowNumberList(List<XlsxRowNumber> list) {
        this.list = list;
    }

    @Override
    public Iterator<XlsxRowNumber> iterator() {
        return list.iterator();
    }
}
