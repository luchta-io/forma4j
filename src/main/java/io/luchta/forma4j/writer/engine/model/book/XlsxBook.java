package io.luchta.forma4j.writer.engine.model.book;

import io.luchta.forma4j.writer.engine.model.cell.style.XlsxCellStyles;
import io.luchta.forma4j.writer.engine.model.sheet.XlsxSheetList;

public class XlsxBook {
    XlsxSheetList sheets;
    XlsxCellStyles styles;

    public XlsxBook(XlsxSheetList sheets, XlsxCellStyles styles) {
        this.sheets = sheets;
        this.styles = styles;
    }

    public XlsxSheetList sheets() {
        return sheets;
    }

    public XlsxCellStyles styles() {
        return styles;
    }
}
