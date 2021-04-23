package jp.co.actier.luchta.forma4j.writer.engine.model.sheet;

import jp.co.actier.luchta.forma4j.writer.engine.model.cell.address.XlsxSheetName;
import jp.co.actier.luchta.forma4j.writer.engine.model.row.XlsxRowList;

public class XlsxSheet {
    XlsxSheetName name;
    XlsxRowList rows;

    public XlsxSheet(XlsxSheetName name, XlsxRowList rows) {
        this.name = name;
        this.rows = rows;
    }

    public XlsxSheetName name() {
        return name;
    }

    public XlsxRowList rows() {
        return rows;
    }
}
