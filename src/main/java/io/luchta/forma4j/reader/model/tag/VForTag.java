package io.luchta.forma4j.reader.model.tag;

import io.luchta.forma4j.reader.model.tag.property.BreakCondition;
import io.luchta.forma4j.reader.model.tag.property.Index;
import io.luchta.forma4j.reader.model.tag.property.Name;

/**
 * {@code VForTag} はv-forタグを表すクラスです
 *
 * @since 0.1.0
 */
public class VForTag implements Tag {
    /** v-forタグのfromプロパティ */
    private Index from;
    /** v-forタグのfromプロパティが定義されているかどうかを示すフラグ */
    private Boolean fromIsUndefined;
    /** v-forタグのtoプロパティ */
    private Index to;
    /** v-forタグのtoプロパティが定義されているかどうかを示すフラグ */
    private Boolean toIsUndefined;
    /** v-forタグのnameプロパティ */
    private Name name;
    /** v-forタグのbreakプロパティ */
    private BreakCondition breakCondition;

    /**
     * コンストラクタ
     * @param from
     * @param fromIsUndefined
     * @param to
     * @param toIsUndefined
     * @param name
     * @param breakCondition
     */
    public VForTag(Index from, Boolean fromIsUndefined, Index to, Boolean toIsUndefined, Name name, BreakCondition breakCondition) {
        this.from = from;
        this.fromIsUndefined = fromIsUndefined;
        this.to = to;
        this.toIsUndefined = toIsUndefined;
        this.name = name;
        this.breakCondition = breakCondition;
    }

    /**
     * fromプロパティの値を返す
     * @return fromプロパティの値
     */
    public Index from() {
        return from;
    }

    /**
     * v-forタグのfromプロパティが定義されているかどうかを返す
     * @return true: 定義されている, false: 定義されていない
     */
    public boolean fromIsUndefined() {
        return fromIsUndefined.booleanValue();
    }

    /**
     * toプロパティの値を返す
     * @return toプロパティの値
     */
    public Index to() {
        return to;
    }

    /**
     * v-forタグのtoプロパティが定義されているかどうかを返す
     * @return true: 定義されている, false: 定義されていない
     */
    public boolean toIsUndefined() {
        return toIsUndefined.booleanValue();
    }

    /**
     * nameプロパティの値を返す
     * @return nameプロパティの値
     */
    public Name name() {
        return name;
    }

    /**
     * breakプロパティの値を返す
     * @return breakプロパティの値
     */
    public BreakCondition breakCondition() {
        return breakCondition;
    }

    /**
     * v-forタグかどうかを返す
     * @return true: v-for, false f-forではない
     */
    @Override
    public boolean isVFor() {
        return true;
    }
}
