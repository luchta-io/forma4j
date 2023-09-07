package io.luchta.forma4j.writer.engine.model.cell.style;

import org.apache.poi.ss.usermodel.CellStyle;

public interface XlsxCellStyleProperty {

    /**
     * 引数で渡された {@code CellStyle} に対して、このプロパティの値を上書きします。
     *
     * @param cellStyle 上書き対象の {@code CellStyle}
     */
    void overwriteTo(CellStyle cellStyle);
}
