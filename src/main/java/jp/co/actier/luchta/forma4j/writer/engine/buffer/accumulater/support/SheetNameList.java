package jp.co.actier.luchta.forma4j.writer.engine.buffer.accumulater.support;

import jp.co.actier.luchta.forma4j.writer.engine.model.cell.address.XlsxSheetName;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SheetNameList implements Iterable<XlsxSheetName> {
    List<XlsxSheetName> list = new ArrayList<>();

    public void add(XlsxSheetName sheetName) {
        list.add(sheetName);
    }

    @Override
    public Iterator<XlsxSheetName> iterator() {
        return list.iterator();
    }
}
