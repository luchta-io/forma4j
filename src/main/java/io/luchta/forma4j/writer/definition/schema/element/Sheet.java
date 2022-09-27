package io.luchta.forma4j.writer.definition.schema.element;

import io.luchta.forma4j.writer.definition.schema.Element;
import io.luchta.forma4j.writer.definition.schema.ElementList;
import io.luchta.forma4j.writer.definition.schema.ElementType;
import io.luchta.forma4j.writer.definition.schema.attribute.Name;
import io.luchta.forma4j.writer.definition.schema.attribute.loop.Collection;
import io.luchta.forma4j.writer.definition.schema.attribute.loop.Item;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElements;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.ArrayList;
import java.util.List;

/**
 * {@code Sheet} は設定ファイルの sheet タグを表すクラスです
 * <p>
 * JAXB が読み込んだ設定ファイルの定義情報がセットされます。
 * </p>
 *
 * @since 0.1.0
 */
@XmlRootElement
public class Sheet implements Element {
    /**
     * 設定ファイルに記述されるタグ名です
     */
    public static final String ELEMENT_NAME = "sheet";

    @XmlAttribute
    Name name = new Name();
    @XmlAttribute
    Item item = new Item();
    @XmlAttribute
    Collection collection = new Collection();
    @XmlElements({
            @XmlElement(name = Row.ELEMENT_NAME, type = Row.class),
            @XmlElement(name = Column.ELEMENT_NAME, type = Column.class),
            @XmlElement(name = Cell.ELEMENT_NAME, type = Cell.class),
            @XmlElement(name = HorizontalFor.ELEMENT_NAME, type = HorizontalFor.class),
            @XmlElement(name = VerticalFor.ELEMENT_NAME, type = VerticalFor.class),
            @XmlElement(name = ListElement.ELEMENT_NAME, type = ListElement.class)
    })
    List<Element> children = new ArrayList<>();

    /**
     * コンストラクタです
     */
    public Sheet() {}

    /**
     * コンストラクタです
     * <p>
     * パラメータで渡された値で初期化されます
     * </p>
     * @param name name プロパティの設定値です
     * @param collection collection プロパティの設定値です
     * @param children 子要素です
     */
    public Sheet(Name name, Collection collection, List<Element> children) {
        this.name = name;
        this.collection = collection;
        this.children = children;
    }

    /**
     * name プロパティの設定値を取得するメソッドです
     * @return name プロパティの設定値です
     */
    public Name name() {
        return name;
    }

    /**
     * item プロパティの設定値を取得するメソッドです
     * @return item プロパティの設定値です
     */
    public Item item() {
        return item;
    }

    /**
     * collection プロパティの設定値を取得するメソッドです
     * @return collection プロパティの設定値です
     */
    public Collection collection() {
        return collection;
    }

    /**
     * 要素種別を返すメソッドです
     * @return 要素種別: {@link ElementType#SHEET}
     */
    @Override
    public ElementType type() {
        return ElementType.SHEET;
    }

    /**
     * 子要素を返すメソッドです
     * @return 子要素です
     */
    @Override
    public ElementList children() {
        return new ElementList(children);
    }

    /**
     * 子要素を持っているかどうかを返すメソッドです
     * @return true: 子要素を持っている, false: 子要素を持っていない
     */
    @Override
    public boolean hasChildren() {
        return !children.isEmpty();
    }
}
