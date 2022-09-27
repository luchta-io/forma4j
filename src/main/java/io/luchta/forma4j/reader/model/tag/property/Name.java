package io.luchta.forma4j.reader.model.tag.property;

/**
 * {@code Name} はタグの name プロパティの設定値を表すバリューオブジェクトです
 *
 * @since 0.1.0
 */
public class Name {
    /**
     * 設定ファイルに記述されるプロパティの名称です
     */
    public static final String PROPERTY_NAME = "name";

    private String value;

    /**
     * コンストラクタ
     * <p>
     * 値は空文字で初期化されます。
     * </p>
     */
    public Name() {
        value = "";
    }

    /**
     * コンストラクタ
     * <p>
     * パラメータで受け取った値で初期化されます。
     * </p>
     * @param value 値
     */
    public Name(String value) {
        this.value = value;
    }

    /**
     * 値が空かどうかを返します
     * @return true: 空文字または {@code NULL}, false: 空文字でないかつ {@code NULL}でない
     */
    public boolean isEmpty() {
        return value == null || value.isEmpty();
    }

    /**
     * オブジェクトを文字列として返します
     * <p>
     * 値が空の場合は空文字を返します。
     * </p>
     * @return オブジェクトを文字列にした値
     */
    @Override
    public String toString() {
        if (isEmpty()) {
            return "";
        }
        return value;
    }
}
