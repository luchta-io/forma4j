package io.luchta.forma4j.writer.engine.model.cell.style;

import io.luchta.forma4j.writer.processor.poi.CellStyleBuilder;

import java.util.Objects;

/**
 * 背景色プロパティクラス
 * <p>
 * ARGBで指定された色をセルの背景色に設定します
 * </p>
 * @since 1.6.0
 */
public class BackGroundColorProperty implements XlsxCellStyleProperty {
    /** プロパティ名 */
    public static final String NAME = "BACKGROUND-COLOR";
    /** 値 */
    String value;

    /**
     * コンストラクタ
     * @param value
     */
    public BackGroundColorProperty(String value) {
        this.value = value;
    }

    /**
     * CellStyle構築
     * <p>
     * ARGBで指定された色をセルの背景色として設定します
     * </p>
     * @param builder CellStyleBuilder
     */
    @Override
    public void accept(CellStyleBuilder builder) {
        String argb = value;
        if (argb.startsWith("#")) {
            argb = value.substring(1);
        }
        builder.setBackGroundColor(argb);
    }

    /**
     * オブジェクトが等価であるかどうかを返すメソッドです
     * @param o 比較するオブジェクト
     * @return true: 等価である, false: 等価ではない
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BackGroundColorProperty that = (BackGroundColorProperty) o;
        return Objects.equals(value, that.value);
    }

    /**
     * オブジェクトのハッシュ値を返すメソッドです
     * @return ハッシュ値
     */
    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
