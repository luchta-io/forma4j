package io.luchta.forma4j.reader.model.tag.property;

import java.util.Objects;

/**
 * {@code AllSheets} は {@link io.luchta.forma4j.reader.model.tag.SheetTag} のプロパティを表すバリューオブジェクトです
 * <p>
 * {@code AllSheets} が true のとき、EXCEL ファイルに含まれる全てのシートを読み込むことを示します。false のときは指定されたシートのみを読み込みます。デフォルトは false です。
 * </p>
 *
 * @since 1.2.0
 */
public class AllSheets {
    /**
     * 設定ファイルに記述されるプロパティの名称です
     */
    public static final String PROPERTY_NAME = "allSheets";

    private Boolean value;

    /**
     * コンストラクタ
     * <p>
     * 値を {@code NULL} で初期化されます。
     * </p>
     */
    public AllSheets() {
        value = false;
    }

    /**
     * コンストラクタ
     * <p>
     * パラメータで受け取った値で初期化されます。
     * </p>
     * @param value true: すべてのシートを読み込む, false: 指定されたシートのみを読み込む
     */
    public AllSheets(Boolean value) {
        this.value = value;
    }

    /**
     * 値が {@code NULL} かどうかを返します
     * @return true: {@code NULL}, false: {@code NULL} ではない
     */
    public boolean isEmpty() {
        return value == null;
    }

    /**
     * オブジェクトを {@code Boolean} で返します
     * @return オブジェクトの値
     */
    public Boolean getValue() {
        return value;
    }

    /**
     * オブジェクトが等価かどうかを返します
     * @param o 等価かどうか比較するオブジェクト
     * @return true: 等価, false: 等価ではない
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AllSheets allSheets = (AllSheets) o;
        return Objects.equals(value, allSheets.value);
    }

    /**
     * オブジェクトのハッシュ値を返します
     * @return ハッシュ値
     */
    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    /**
     * オブジェクトを文字列に変換して返します
     * <p>
     * オブジェクトの値が {@code NULL} のときは空文字を返します。
     * </p>
     * @return オブジェクトを文字列に変換した値
     */
    @Override
    public String toString() {
        if (isEmpty()) {
            return "";
        }
        return value.toString();
    }
}
