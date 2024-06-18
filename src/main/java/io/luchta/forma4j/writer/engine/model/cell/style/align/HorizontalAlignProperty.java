package io.luchta.forma4j.writer.engine.model.cell.style.align;

import io.luchta.forma4j.writer.engine.model.cell.style.XlsxCellStyleProperty;
import io.luchta.forma4j.writer.processor.poi.CellStyleBuilder;
import org.apache.poi.ss.usermodel.HorizontalAlignment;

import java.util.Objects;

/**
 * 水平方向の文字位置プロパティクラス
 */
public class HorizontalAlignProperty implements XlsxCellStyleProperty {
    /**
     * プロパティ名
     */
    public static final String NAME = "H-ALIGN";

    /**
     * プロパティの設定値
     */
    private String value;

    /**
     * コンストラクタ
     * @param value プロパティの設定値
     */
    public HorizontalAlignProperty(String value) {
        this.value = value;
    }

    /**
     * プロパティの設定値を設定する
     * <ul>
     * <li>left: 左詰め</li>
     * <li>center: 中央揃え</li>
     * <li>right: 右詰め</li>
     * <li>justify: 両端揃え</li>
     * <li>fill: 繰り返し</li>
     * <li>center-selection: 選択範囲内で中央</li>
     * </ul>
     * <p>
     * 設定値がleft, center, right, justify, fill, center-selection以外の場合は何もしない
     * </p>
     * @param builder CellStyleBuilder
     */
    @Override
    public void accept(CellStyleBuilder builder) {
        switch (value.toLowerCase()) {
            case "left": builder.setHorizontalAlign(HorizontalAlignment.LEFT); break;
            case "center": builder.setHorizontalAlign(HorizontalAlignment.CENTER); break;
            case "right": builder.setHorizontalAlign(HorizontalAlignment.RIGHT); break;
            case "justify": builder.setHorizontalAlign(HorizontalAlignment.JUSTIFY); break;
            case "fill": builder.setHorizontalAlign(HorizontalAlignment.FILL); break;
            case "center-selection": builder.setHorizontalAlign(HorizontalAlignment.CENTER_SELECTION); break;
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
        HorizontalAlignProperty that = (HorizontalAlignProperty) o;
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
