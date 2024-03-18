package io.luchta.forma4j.writer.engine.model.column.property;

/**
 * {@code WidthProperty} はカラム幅に関するクラスです
 */
public class WidthProperty implements XlsxColumnProperty {
    /**
     * スタイル名
     */
    public static final String NAME = "WIDTH";
    String value;

    /**
     * コンストラクタ
     * @param value
     */
    public WidthProperty(String value) {
        this.value = value;
    }

    public int intValue() {
        return Integer.valueOf(value);
    }
}
