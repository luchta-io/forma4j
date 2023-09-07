package io.luchta.forma4j.writer.engine.model.book;

import io.luchta.forma4j.writer.engine.model.cell.XlsxCell;
import io.luchta.forma4j.writer.engine.model.cell.style.XlsxCellStyle;
import io.luchta.forma4j.writer.engine.model.row.XlsxRow;
import io.luchta.forma4j.writer.engine.model.sheet.XlsxSheet;
import io.luchta.forma4j.writer.engine.model.sheet.XlsxSheetList;

import java.util.HashSet;
import java.util.Set;

public class XlsxBook {
    XlsxSheetList sheets;

    public XlsxBook(XlsxSheetList sheets) {
        this.sheets = sheets;
    }

    public XlsxSheetList sheets() {
        return sheets;
    }

    public Set<XlsxCellStyle> styles() {
        // TODO XlsxBookを作るときにこのSetを作って保持しておく方が効率がよいので改善する
        Set<XlsxCellStyle> set = new HashSet<>();
        for (XlsxSheet sheet : sheets) {
            for (XlsxRow row : sheet.rows()) {
                for (XlsxCell cell : row.cells()) {
                    XlsxCellStyle style = cell.style();
                    set.add(style);
                }
            }
        }
        return set;
    }
}
