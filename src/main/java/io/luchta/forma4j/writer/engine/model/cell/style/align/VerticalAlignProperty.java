package io.luchta.forma4j.writer.engine.model.cell.style.align;

import io.luchta.forma4j.writer.engine.model.cell.style.XlsxCellStyleProperty;
import io.luchta.forma4j.writer.processor.poi.CellStyleBuilder;
import org.apache.poi.ss.usermodel.VerticalAlignment;

import java.util.Objects;

/**
 * 垂直方向の文字位置プロパティクラス
 */
public class VerticalAlignProperty implements XlsxCellStyleProperty {
    /**
     * プロパティ名
     */
    public static final String NAME = "V-ALIGN";

    /**
     * プロパティの設定値
     */
    private String value;

    /**
     * コンストラクタ
     * @param value プロパティの設定値
     */
    public VerticalAlignProperty(String value) {
        this.value = value;
    }

    /**
     * プロパティの設定値を設定する
     * <ul>
     * <li>top: 上詰め</li>
     * <li>center: 中央揃え</li>
     * <li>bottom: 下詰め</li>
     * <li>justify: 両端揃え</li>
     * </ul>
     * <p>
     * 設定値がtop, center, bottom, justify以外の場合は何もしない
     * </p>
     * @param builder CellStyleBuilder
     */
    @Override
    public void accept(CellStyleBuilder builder) {
        switch (value.toLowerCase()) {
            case "top": builder.setVerticalAlign(VerticalAlignment.TOP); break;
            case "center": builder.setVerticalAlign(VerticalAlignment.CENTER); break;
            case "bottom": builder.setVerticalAlign(VerticalAlignment.BOTTOM); break;
            case "justify": builder.setVerticalAlign(VerticalAlignment.JUSTIFY); break;
            default: break;
        }
    }

    /**
     * 同値比較
     * @param o 比較するオブジェクト
     * @return true: 同値, false: 同値でない
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VerticalAlignProperty that = (VerticalAlignProperty) o;
        return Objects.equals(value, that.value);
    }

    /**
     * ハッシュコード
     * @return ハッシュコード
     */
    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
