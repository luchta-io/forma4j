package io.luchta.forma4j.writer.definition.schema.element;

import io.luchta.forma4j.writer.definition.schema.Element;
import io.luchta.forma4j.writer.definition.schema.ElementList;
import io.luchta.forma4j.writer.definition.schema.ElementType;
import io.luchta.forma4j.writer.definition.schema.attribute.Style;
import io.luchta.forma4j.writer.definition.schema.attribute.index.ColumnIndex;
import io.luchta.forma4j.writer.definition.schema.attribute.index.RowIndex;
import io.luchta.forma4j.writer.definition.schema.attribute.loop.Collection;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * {@code ListElement} は設定ファイルの list タグを表すクラスです
 *
 * @since 1.1.0
 */
@XmlRootElement
public class ListElement implements Element {
    /**
     * list タグのタグ名です
     */
    public static final String ELEMENT_NAME = "list";

    @XmlAttribute
    RowIndex startRowIndex = new RowIndex();
    @XmlAttribute
    ColumnIndex startColumnIndex = new ColumnIndex();
    @XmlAttribute
    Style headerStyle = new Style();
    @XmlAttribute
    Style detailStyle = new Style();
    @XmlAttribute
    Collection collection = new Collection();

    /**
     * コンストラクタです
     */
    public ListElement() {}

    /**
     * コンストラクタです
     * <p>
     * パラメータで渡された値で初期化処理を行います
     * </p>
     * @param startRowIndex 書き込みを開始する行のインデックスです
     * @param startColumnIndex 書き込みを開始する列のインデックスです
     * @param headerStyle ヘッダ行のスタイルです
     * @param detailStyle 明細行のスタイルです
     * @param collection 書き込みを行うデータのコレクション名です
     */
    public ListElement(RowIndex startRowIndex, ColumnIndex startColumnIndex, Style headerStyle, Style detailStyle, Collection collection) {
        this.startRowIndex = startRowIndex;
        this.startColumnIndex = startColumnIndex;
        this.headerStyle = headerStyle;
        this.detailStyle = detailStyle;
        this.collection = collection;
    }

    /**
     * 書き込みを開始する行のインデックスを返すメソッドです
     * @return 書き込みを開始する行のインデックスです
     */
    public RowIndex startRowIndex() {
        return startRowIndex;
    }

    /**
     * 書き込みを開始する列のインデックスを返すメソッドです
     * @return 書き込みを開始する列のインデックスです
     */
    public ColumnIndex startColumnIndex() {
        return startColumnIndex;
    }

    /**
     * ヘッダ行のスタイルを返すメソッドです
     * @return ヘッダ行のスタイルです
     */
    public Style headerStyle() {
        return headerStyle;
    }

    /**
     * 明細行のスタイルを返すメソッドです
     * @return 明細行のスタイルです
     */
    public Style detailStyle() {
        return detailStyle;
    }

    /**
     * 書き込みを行うデータのコレクション名を返すメソッドです
     * @return 書き込みを行うデータのコレクション名です
     */
    public Collection collection() {
        return collection;
    }

    /**
     * 要素種別を返すメソッドです
     * @return 要素種別です {@link ElementType#LIST}
     */
    @Override
    public ElementType type() {
        return ElementType.LIST;
    }

    /**
     * 子要素を返すメソッドです
     * <p>
     * list タグは子要素を持たないため常に空のリストが返ります。
     * </p>
     * @return 空のリストです
     */
    @Override
    public ElementList children() {
        return new ElementList();
    }

    /**
     * 子要素が存在するかどうかを返すメソッドです
     * <p>
     * list タグは子要素を持たないため常に false が返ります。
     * </p>
     * @return false
     */
    @Override
    public boolean hasChildren() {
        return false;
    }
}
