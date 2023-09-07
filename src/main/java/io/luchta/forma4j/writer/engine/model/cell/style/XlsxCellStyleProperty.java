package io.luchta.forma4j.writer.engine.model.cell.style;

import org.apache.poi.ss.usermodel.CellStyle;

public interface XlsxCellStyleProperty {
    void overwriteTo(CellStyle cellStyle);
}
