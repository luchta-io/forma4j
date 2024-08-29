package io.luchta.forma4j.reader.compile.parse.tagbuilder;

import io.luchta.forma4j.context.syntax.SyntaxError;
import io.luchta.forma4j.context.syntax.SyntaxErrors;
import io.luchta.forma4j.reader.model.tag.CellTag;
import io.luchta.forma4j.reader.model.tag.Tag;
import io.luchta.forma4j.reader.model.tag.property.DisplayValue;
import io.luchta.forma4j.reader.model.tag.property.Index;
import io.luchta.forma4j.reader.model.tag.property.Name;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.List;

/**
 * セルタグ生成クラス
 */
public class CellBuilder implements TagBuilder {
    /**
     * ビルド
     * @param nodeMap
     * @param syntaxErrors
     * @return {@link io.luchta.forma4j.reader.model.tag.CellTag}
     */
    @Override
    public Tag build(NamedNodeMap nodeMap, SyntaxErrors syntaxErrors) {
        List<String> errorMessages = new ArrayList<>();

        // row属性
        Node rowNode = nodeMap.getNamedItem("row");
        boolean rowIsUndefined = false;
        if (rowNode == null) {
            rowIsUndefined = true;
        }
        Integer rowNumber = NodeValueConverter.toInteger(rowNode, 0);
        if (rowNumber == null) {
            errorMessages.add("row には0以上の整数を指定してください");
        }

        // col属性
        Node colNode = nodeMap.getNamedItem("col");
        boolean colIsUndefined = false;
        if (colNode == null) {
            colIsUndefined = true;
        }
        Integer colNumber = NodeValueConverter.toInteger(nodeMap.getNamedItem("col"), 0);
        if (colNumber == null) {
            errorMessages.add("col には0以上の整数を指定してください");
        }

        // name属性
        String name = NodeValueConverter.toString(nodeMap.getNamedItem("name"), null);
        if (name == null) {
            errorMessages.add("name は必須入力です");
        }

        // displayValue属性
        Boolean displayValue = NodeValueConverter.toBoolean(nodeMap.getNamedItem(DisplayValue.PROPERTY_NAME), false);

        // エラーメッセージ追加
        AddBuilderSyntaxErrors.run(errorMessages, syntaxErrors);

        return new CellTag(new Index(rowNumber), rowIsUndefined, new Index(colNumber), colIsUndefined, new Name(name), new DisplayValue(displayValue));
    }
}
