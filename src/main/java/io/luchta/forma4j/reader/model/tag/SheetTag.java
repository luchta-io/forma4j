package io.luchta.forma4j.reader.model.tag;

import io.luchta.forma4j.reader.model.tag.property.AllSheets;
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
     * 設定ファイルに記述されるプロパティの名称です
     */
    public static final String TAG_NAME = "sheet";

    private Name name;
    private AllSheets allSheets;

    private SheetTag(Name name, AllSheets allSheets) {
        this.name = name;
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
        private AllSheets allSheets;

        /**
         * コンストラクタ
         */
        public SheetTagBuilder() {}

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
         * name プロパティに値が設定されない場合は allSheets プロパティが true でないとエラーとなります。
         * </p>
         * @return {@link SheetTag}
         * @exception IllegalArgumentException
         */
        public SheetTag build() {
            if (name.isEmpty() && (allSheets.isEmpty() || !allSheets.getValue())) {
                throw new IllegalArgumentException("sheet タグに name プロパティを設定しない場合は allSheets プロパティを true に設定してください");
            }

            SheetTag sheetTag = new SheetTag(name, allSheets);
            return sheetTag;
        }
    }
}
