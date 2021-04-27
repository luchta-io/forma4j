package io.luchta.forma4j.writer.engine.model.book;

import io.luchta.forma4j.writer.engine.model.sheet.XlsxSheetList;

public class XlsxBook {
    XlsxSheetList sheets;

    public XlsxBook(XlsxSheetList sheets) {
        this.sheets = sheets;
    }

    public XlsxSheetList sheets() {
        return sheets;
    }
}
