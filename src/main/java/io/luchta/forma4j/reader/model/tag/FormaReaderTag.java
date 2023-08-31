package io.luchta.forma4j.reader.model.tag;

/**
 * {@code FormaReaderTag} は forma-reader タグを扱うためのクラスです
 */
public class FormaReaderTag implements Tag {
    /**
     * 設定ファイルに記述されるタグの名称です
     */
    public static final String TAG_NAME = "forma";

    /**
     * タグが forma-reader タグかどうかを返します
     * @return true: forma-reader タグ, false: forma-reader タグではない
     */
    @Override
    public boolean isFormaReader() {
        return true;
    }
}
