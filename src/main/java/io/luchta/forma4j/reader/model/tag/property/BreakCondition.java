package io.luchta.forma4j.reader.model.tag.property;

/**
 * {@code Break} はタグのプロパティを表すバリューオブジェクトです
 * <p>
 * {@code Break} に設定された条件に従ってループ処理のブレイクを行います。
 * </p>
 * <p>
 * {@code Break} は {@link io.luchta.forma4j.reader.model.tag.VForTag}、{@link io.luchta.forma4j.reader.model.tag.HForTag} に設定することができます。
 * </p>
 *
 * @since 1.8.0
 */
public class BreakCondition {
    /** 設定ファイルに記述されるプロパティの名称です */
    public static final String PROPERTY_NAME = "break";

    /** 値 */
    private String value;

    /** コンストラクタ */
    public BreakCondition() {
        value = "";
    }

    /**
     * コンストラクタ
     * @param value 設定値
     */
    public BreakCondition(String value) {
        this.value = value;
    }

    /**
     * 値がかどうかを返します
     * @return true: 空, false: 空ではない
     */
    public boolean isEmpty() {
        return value == null || value.equals("");
    }

    /**
     * ブレイク種別を取得
     * @return ブレイク種別
     */
    public BreakType breakType() {
        if (value == null) return BreakType.NONE;

        switch (value.trim()) {
            case "all_blank": return BreakType.ALL_BLANK;
            default: return BreakType.NONE;
        }
    }
}
