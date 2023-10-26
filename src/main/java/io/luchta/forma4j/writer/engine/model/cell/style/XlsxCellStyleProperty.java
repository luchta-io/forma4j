package io.luchta.forma4j.writer.engine.model.cell.style;

import io.luchta.forma4j.writer.processor.poi.CellStyleBuilder;

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
            case FontSizeProperty.NAME:
                return new FontSizeProperty(value);
            default:
                return new NotSupportProperty(name, value);
        }
    }

    /**
     * 引数で渡された {@code CellStyleBuilder} を受け入れて、
     * 各プロパティに応じたCellStyleの構築処理を実行します
     *
     * @param builder CellStyleBuilder
     */
    void accept(CellStyleBuilder builder);
}
