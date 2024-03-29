package io.luchta.forma4j.reader.model.tag;

import io.luchta.forma4j.reader.model.tag.property.AllSheets;
import io.luchta.forma4j.reader.model.tag.property.Index;
import io.luchta.forma4j.reader.model.tag.property.Name;

/**
 * {@code SheetTag} は設定ファイルの sheet タグを扱うためのクラスです
 * <p>
 * 設定ファイルに記述された sheet タグの内容がセットされます。
 * </p>
 * @since 0.1.0
 */
public class SheetTag implements Tag {
    /**
     * 設定ファイルに記述されるタグの名称です
     */
    public static final String TAG_NAME = "sheet";

    private Name name;
    private Index index;
    private AllSheets allSheets;

    public SheetTag(Name name, Index index, AllSheets allSheets) {
        this.name = name;
        this.index = index;
        this.allSheets = allSheets;
    }

    /**
     * sheet タグに記述された name プロパティの設定値です
     * @return name プロパティの設定値
     */
    public Name name() {
        return name;
    }

    /**
     * sheet タグに記述された allSheets プロパティの設定値です
     * @return allSheets プロパティの設定値
     */
    public AllSheets allSheets() {
        return allSheets;
    }


     /**
     * sheet タグに記述された index プロパティの設定値です
     * @return index プロパティの設定値
     */
    public Index index() {
        return index;
    }

    /**
     * sheet タグに name プロパティが設定されているかどうかを返します
     *
     * @return true: name プロパティが設定されている, false: name プロパティが設定されていない
     */
    public boolean hasName() {
        return !name.isEmpty();
    }

    /**
     * すべてのシートを読み込むかを設定したフラグ
     * @return true: すべてのシートを読み込む, false: 指定されたシートのみを読み込む
     */
    public boolean readAllSheets() {
        return allSheets.getValue();
    }

    /**
     * タグが sheet タグかどうかを返します
     * @return true: sheet タグ, false: sheet タグではない
     */
    @Override
    public boolean isSheet() {
        return true;
    }

    /**
     * {@code SheetTagBuilder} は {@link SheetTag} を生成するためのビルダークラスです
     * <p>
     * プロパティにセットした内容に従って {@link SheetTag} を生成します。
     * </p>
     *
     * @since 1.2.0
     */
    public static class SheetTagBuilder {
        private Name name;
        private Index index;
        private AllSheets allSheets;

        /**
         * コンストラクタ
         */
        public SheetTagBuilder() {
            name = new Name();
            index = new Index();
            allSheets = new AllSheets();
        }

        /**
         * name プロパティを設定するメソッドです
         * @param name name プロパティの設定値
         * @return {@link SheetTagBuilder}
         */
        public SheetTagBuilder name(Name name) {
            this.name = name;
            return this;
        }

        /**
         * index プロパティを設定するメソッドです
         * @param index index プロパティの設定値
         * @return {@link SheetTagBuilder}
         */
        public SheetTagBuilder index(Index index) {
            this.index = index;
            return this;
        }

        /**
         * allSheets プロパティを設定するメソッドです
         * @param allSheets allSheets プロパティの設定値
         * @return {@link SheetTagBuilder}
         */
        public SheetTagBuilder allSheets(AllSheets allSheets) {
            this.allSheets = allSheets;
            return this;
        }

        /**
         * {@link SheetTag} を生成するメソッドです
         * <p>
         * index, name, allSheets のいずれかが設定されていない場合は例外をスローします。
         * </p>
         * @return {@link SheetTag}
         * @exception IllegalArgumentException
         */
        public SheetTag build() {
            if (index.isEmpty() && name.isEmpty() && allSheets.isFalsy()) {
                throw new IllegalArgumentException("sheet タグのプロパティにはindex, name, allSheets のいずれかのプロパティを設定してください");
            }
            return new SheetTag(name, index, allSheets);
        }
    }
}
