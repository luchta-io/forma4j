package io.luchta.forma4j.writer.definition.schema.attribute.loop;

import jakarta.xml.bind.annotation.XmlValue;

import java.util.Objects;

/**
 * {@code Collection} は collection プロパティを表すバリューオブジェクトです
 */
public class Collection {
    @XmlValue
    String value;

    /**
     * コンストラクタ
     * <p>
     * 値は @{code NULL} で初期化されます。
     * </p>
     */
    public Collection() {
    }

    /**
     * コンストラクタ
     * <p>
     * 値はパラメータで渡された @{code value} で初期化されます。
     * </p>
     * @param value 初期値
     */
    public Collection(String value) {
        this.value = value;
    }

    /**
     * オブジェクトが空かどうかを返すメソッドです
     * @return true: NULL または 空のとき, false: NULL でないかつ空でないとき
     */
    public boolean isEmpty() {
        return value == null || value.isEmpty();
    }

    /**
     * オブジェクトを文字列として返すメソッドです
     * <p>
     * オブジェクトが空のときは空文字が返ります。
     * </p>
     * @return オブジェクトを文字列に変換した値
     */
    @Override
    public String toString() {
        if (isEmpty()) return "";
        return value;
    }

    /**
     * オブジェクトが等価であるかどうかを返すメソッドです
     * @param o 比較するオブジェクト
     * @return true: 等価である, false: 等価ではない
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Collection)) return false;
        Collection that = (Collection) o;
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
