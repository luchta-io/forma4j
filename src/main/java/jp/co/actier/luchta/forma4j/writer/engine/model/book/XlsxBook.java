package jp.co.actier.luchta.forma4j.writer.engine.model.book;

import jp.co.actier.luchta.forma4j.writer.engine.model.sheet.XlsxSheetList;

public class XlsxBook {
    XlsxSheetList sheets;

    public XlsxBook(XlsxSheetList sheets) {
        this.sheets = sheets;
    }

    public XlsxSheetList sheets() {
        return sheets;
    }
}
