package io.luchta.forma4j.writer.engine.model.cell.style;

import io.luchta.forma4j.writer.engine.model.cell.style.align.HorizontalAlignProperty;
import io.luchta.forma4j.writer.engine.model.cell.style.align.VerticalAlignProperty;
import io.luchta.forma4j.writer.engine.model.cell.style.border.*;
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
        String upperName = name.toUpperCase();
        switch (upperName) {
            case BackGroundColorProperty.NAME:
                return new BackGroundColorProperty(value);
            case BorderProperty.NAME:
                return new BorderProperty(value);
            case BorderLeftProperty.NAME:
                return new BorderLeftProperty(value);
            case BorderRightProperty.NAME:
                return new BorderRightProperty(value);
            case BorderTopProperty.NAME:
                return new BorderTopProperty(value);
            case BorderBottomProperty.NAME:
                return new BorderBottomProperty(value);
            case ColorProperty.NAME:
                return new ColorProperty(value);
            case FontFamilyProperty.NAME:
                return new FontFamilyProperty(value);
            case FontSizeProperty.NAME:
                return new FontSizeProperty(value);
            case FontStyleProperty.NAME:
                return new FontStyleProperty(value);
            case HorizontalAlignProperty.NAME:
                return new HorizontalAlignProperty(value);
            case VerticalAlignProperty.NAME:
                return new VerticalAlignProperty(value);
            case WrapTextProperty.NAME:
                return new WrapTextProperty(value);
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
