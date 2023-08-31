package io.luchta.forma4j.writer.processor.poi.setting;

import io.luchta.forma4j.writer.engine.model.cell.style.XlsxBorderStyle;
import io.luchta.forma4j.writer.engine.model.cell.style.XlsxCellStyle;
import io.luchta.forma4j.writer.engine.model.cell.style.XlsxCellStyles;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;

public class StyleSetting {

    public void set(Cell cell, XlsxCellStyles styles) {
        baseStyle(cell);
        customStyle(cell, styles);
    }

    private void baseStyle(Cell cell) {
        CellStyle defaultStyle = cell.getCellStyle();
        // FIXME cellごとにCellStyleを作ると、ワークブック内のCellStyleの上限に引っかかるのでこのままではまずい refs.https://qiita.com/Ky01_dev/items/efeca05faaf08bd4fd41
        CellStyle newStyle = cell.getSheet().getWorkbook().createCellStyle();
        newStyle.cloneStyleFrom(defaultStyle);
        newStyle.setWrapText(true);
        cell.setCellStyle(newStyle);
    }

    private void customStyle(Cell cell, XlsxCellStyles styles) {
        for (XlsxCellStyle style : styles) {
            if (style.isBorder()) border(cell, (XlsxBorderStyle) style);
        }
    }

    private void border(Cell cell, XlsxBorderStyle style) {
        CellStyle cellStyle = cell.getCellStyle();

        BorderStyle borderStyle = style.getBorderStyle();
        cellStyle.setBorderLeft(borderStyle);
        cellStyle.setBorderTop(borderStyle);
        cellStyle.setBorderRight(borderStyle);
        cellStyle.setBorderBottom(borderStyle);
    }
}
