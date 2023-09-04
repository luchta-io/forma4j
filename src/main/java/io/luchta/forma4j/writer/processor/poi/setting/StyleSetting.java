package io.luchta.forma4j.writer.processor.poi.setting;

import io.luchta.forma4j.writer.engine.model.cell.style.XlsxBorderStyleProperty;
import io.luchta.forma4j.writer.engine.model.cell.style.XlsxCellStyleProperty;
import io.luchta.forma4j.writer.engine.model.cell.style.XlsxCellStyle;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;

// TODO このクラスの内容はXlsxCellStyle, XlsxCellStylePropertyの実装クラスに移動するのが良さそう
public class StyleSetting {

    public void set(Cell cell, XlsxCellStyle styles) {
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

    private void customStyle(Cell cell, XlsxCellStyle style) {
        for (XlsxCellStyleProperty property : style) {
            if (property.isBorder()) border(cell, (XlsxBorderStyleProperty) property);
        }
    }

    private void border(Cell cell, XlsxBorderStyleProperty style) {
        CellStyle cellStyle = cell.getCellStyle();

        BorderStyle borderStyle = style.getBorderStyle();
        cellStyle.setBorderLeft(borderStyle);
        cellStyle.setBorderTop(borderStyle);
        cellStyle.setBorderRight(borderStyle);
        cellStyle.setBorderBottom(borderStyle);
    }
}
