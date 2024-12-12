package io.luchta.forma4j.reader.model.tag.property;

import java.util.Objects;

/**
 * {@code DisplayValue} は {@link io.luchta.forma4j.reader.model.tag.CellTag} のプロパティを表すバリューオブジェクトです
 * <p>
 * {@code DisplayValue} が true のときセルに表示されている値を取得します。false のときはセルに設定されている値を取得します。デフォルト値は false です。
 * </p>
 *
 * @since 1.8.0
 */
public class DisplayValue {
    /** 設定ファイルに記述されるプロパティの名称です */
    public static final String PROPERTY_NAME = "displayValue";

    /** 値 */
    private Boolean value;

    /**
     * コンストラクタ
     * <p>
     * 値は {@code false} で初期化されます。
     * </p>
     */
    public DisplayValue() {
        value = false;
    }

    /**
     * コンストラクタ
     * <p>
     * パラメータで受け取った値で初期化されます。
     * </p>
     * @param value true: セルの表示値を取得する, false: 表示値ではなく設定されている値を取得する
     */
    public DisplayValue(Boolean value) {
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
     * オブジェクトが {@code true} とみなせるかどうかを返します
     * @return true: {@code true} とみなせる, false: {@code true} とみなせない
     */
    public boolean truthy() {
        if(isEmpty()) return false;
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
        DisplayValue displayValue = (DisplayValue) o;
        return Objects.equals(value, displayValue.value);
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
