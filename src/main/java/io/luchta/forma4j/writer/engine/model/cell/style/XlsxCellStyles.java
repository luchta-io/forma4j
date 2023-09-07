package io.luchta.forma4j.writer.engine.model.cell.style;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class XlsxCellStyles implements Iterable<XlsxCellStyle> {
    Set<XlsxCellStyle> set = new HashSet<>();

    public XlsxCellStyles() {
    }

    public XlsxCellStyles(Set<XlsxCellStyle> set) {
        this.set = set;
    }

    @Override
    public Iterator<XlsxCellStyle> iterator() {
        return set.iterator();
    }
}
