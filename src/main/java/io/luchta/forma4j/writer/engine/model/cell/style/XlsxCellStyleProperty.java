package io.luchta.forma4j.writer.engine.model.cell.style;

import org.apache.poi.ss.usermodel.CellStyle;

public interface XlsxCellStyleProperty {

    /**
     * 引数で渡された {@code name} と {@code value} に応じて、
     * 適切な {@code XlsxCellStyleProperty} のインスタンスを生成して返します。
     *
     * @param name  プロパティの名前
     * @param value プロパティの値
     * @return 適切なXlsxCellStylePropertyのインスタンス
     */
    static XlsxCellStyleProperty of(String name, String value) {
        switch (name) {
            case BorderProperty.NAME:
                return new BorderProperty(value);
            default:
                return new NotSupportProperty(name, value);
        }
    }

    /**
     * 引数で渡された {@code CellStyle} に対して、このプロパティの値を上書きします。
     *
     * @param cellStyle 上書き対象の {@code CellStyle}
     */
    void overwriteTo(CellStyle cellStyle);
}
