package io.luchta.forma4j.writer.processor.poi.setting;

import io.luchta.forma4j.writer.engine.model.cell.style.XlsxBorderStyle;
import io.luchta.forma4j.writer.engine.model.cell.style.XlsxCellStyle;
import io.luchta.forma4j.writer.engine.model.cell.style.XlsxCellStyles;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.util.CellUtil;

public class StyleSetting {
    public void set(Cell cell, XlsxCellStyles styles) {
        CellUtil.setCellStyleProperty(cell, CellUtil.WRAP_TEXT, true);

        for (XlsxCellStyle style : styles) {
            if (style.isBorder()) {
                border(cell, (XlsxBorderStyle) style);
            }
        }
    }

    private void border(Cell cell, XlsxBorderStyle style) {
        BorderStyle borderStyle = style.getBorderStyle();
        CellUtil.setCellStyleProperty(cell, CellUtil.BORDER_LEFT, borderStyle);
        CellUtil.setCellStyleProperty(cell, CellUtil.BORDER_TOP, borderStyle);
        CellUtil.setCellStyleProperty(cell, CellUtil.BORDER_RIGHT, borderStyle);
        CellUtil.setCellStyleProperty(cell, CellUtil.BORDER_BOTTOM, borderStyle);
    }
}
