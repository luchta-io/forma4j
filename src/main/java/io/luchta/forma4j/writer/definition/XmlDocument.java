package io.luchta.forma4j.writer.definition;

import io.luchta.forma4j.writer.definition.schema.Element;
import io.luchta.forma4j.writer.definition.schema.attribute.Name;
import io.luchta.forma4j.writer.definition.schema.attribute.Style;
import io.luchta.forma4j.writer.definition.schema.attribute.index.ColumnIndex;
import io.luchta.forma4j.writer.definition.schema.attribute.index.RowIndex;
import io.luchta.forma4j.writer.definition.schema.attribute.loop.Collection;
import io.luchta.forma4j.writer.definition.schema.element.ListElement;
import io.luchta.forma4j.writer.definition.schema.element.Root;
import io.luchta.forma4j.writer.definition.schema.element.Sheet;

import java.util.ArrayList;
import java.util.List;

/**
 * {@code XmlDocument} は設定ファイルを読み込んだ結果を格納するクラスです
 * <p>
 * 設定ファイルの読み込みは JAXB を利用して行われることを想定します。
 * </p>
 * <p>
 * 設定ファイルを使用しない場合はデフォルトの定義生成を行うことができます。
 * </p>
 * @since 0.1.0
 */
public class XmlDocument {
    Root root = new Root();

    /**
     * コンストラクタ
     */
    public XmlDocument() {
    }

    /**
     * コンストラクタ
     * @param root
     */
    public XmlDocument(Root root) {
        this.root = root;
    }

    /**
     * 設定ファイルの定義情報を返すメソッドです
     * @return 設定ファイルの定義情報です
     */
    public Root root() {
        return root;
    }

    /**
     * デフォルトの定義情報を生成するメソッドです
     * <p>
     * 設定ファイル未定義で EXCEL の書き込みを行う場合に使用することを想定しています
     * </p>
     * @return デフォルトの定義情報です
     */
    public static XmlDocument defaultXmlDocument() {
        ListElement listElement = new ListElement(new RowIndex(0L), new ColumnIndex(0L), new Style(), new Style(), new Collection());
        List<Element> listElements = new ArrayList<>();
        listElements.add(listElement);

        Sheet sheet = new Sheet(new Name("result"), new Collection(), listElements);
        List<Element> sheets = new ArrayList<>();
        sheets.add(sheet);

        Root root = new Root(sheets);

        return new XmlDocument(root);
    }
}
