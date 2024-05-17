package io.luchta.forma4j.writer.engine.model.cell.style.border;

import io.luchta.forma4j.writer.engine.model.cell.style.XlsxCellStyleProperty;
import io.luchta.forma4j.writer.processor.poi.CellStyleBuilder;
import org.apache.poi.ss.usermodel.BorderStyle;

import java.util.Objects;

/**
 * 罫線プロパティの基底クラス
 */
public abstract class Border implements XlsxCellStyleProperty {
    /**
     * プロパティの設定値
     */
    protected String value;

    /**
     * コンストラクタ
     * @param value プロパティの設定値
     */
    public Border(String value) {
        this.value = value;
    }

    /**
     * プロパティの設定値を {@link BorderStyle} に変換する
     * @return {@link BorderStyle}
     */
    protected BorderStyle borderStyle() {
        switch (value.toUpperCase()) {
            case "THIN":
                return BorderStyle.THIN;
            case "MEDIUM":
                return BorderStyle.MEDIUM;
            case "DASHED":
                return BorderStyle.DASHED;
            case "DOTTED":
                return BorderStyle.DOTTED;
            case "THICK":
                return BorderStyle.THICK;
            case "DOUBLE":
                return BorderStyle.DOUBLE;
            case "HAIR":
                return BorderStyle.HAIR;
            case "MEDIUM_DASHED":
                return BorderStyle.MEDIUM_DASHED;
            case "DASH_DOT":
                return BorderStyle.DASH_DOT;
            case "MEDIUM_DASH_DOT":
                return BorderStyle.MEDIUM_DASH_DOT;
            case "DASH_DOT_DOT":
                return BorderStyle.DASH_DOT_DOT;
            case "MEDIUM_DASH_DOT_DOT":
                return BorderStyle.MEDIUM_DASH_DOT_DOT;
            case "SLANTED_DASH_DOT":
                return BorderStyle.SLANTED_DASH_DOT;
            default:
                return BorderStyle.NONE;
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
        Border that = (Border) o;
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
