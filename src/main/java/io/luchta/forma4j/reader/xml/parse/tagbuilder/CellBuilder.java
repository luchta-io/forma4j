package io.luchta.forma4j.reader.xml.parse.tagbuilder;

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
        Integer rowNumber = convertNodeValueToInteger(rowNode);
        if (rowNumber == null) {
            errorMessages.add("row には0以上の整数を指定してください");
        }

        // col属性
        Node colNode = nodeMap.getNamedItem("col");
        boolean colIsUndefined = false;
        if (colNode == null) {
            colIsUndefined = true;
        }
        Integer colNumber = convertNodeValueToInteger(nodeMap.getNamedItem("col"));
        if (colNumber == null) {
            errorMessages.add("col には0以上の整数を指定してください");
        }

        // name属性
        String name = convertNodeValueToString(nodeMap.getNamedItem("name"));
        if (name == null) {
            errorMessages.add("name は必須入力です");
        }

        // displayValue属性
        Boolean displayValue = convertNodeValueToBoolean(nodeMap.getNamedItem(DisplayValue.PROPERTY_NAME));

        // エラーメッセージ追加
        addSyntaxErrors(errorMessages, syntaxErrors);

        return new CellTag(new Index(rowNumber), rowIsUndefined, new Index(colNumber), colIsUndefined, new Name(name), new DisplayValue(displayValue));
    }

    /**
     * Nodeから数値を取得する処理
     * @param node
     * @return Nodeの設定値
     */
    private Integer convertNodeValueToInteger(Node node) {
        Integer value = null;
        if (node == null) {
            value = 0;
        } else {
            Integer rowNumber = null;
            try {
                rowNumber = Integer.parseInt(node.getNodeValue());
            } catch (NumberFormatException e) {
                return value;
            }

            if (rowNumber >= 0) {
                value = Integer.parseInt(node.getNodeValue());
            }
        }
        return value;
    }

    /**
     * Nodeから文字列を取得する処理
     * @param node
     * @return Nodeから取得した文字列
     */
    private String convertNodeValueToString(Node node) {
        String value = "";
        if (node != null) {
            value = node.getNodeValue();
        }
        return value;
    }

    /**
     * Nodeから真理値を取得する処理
     * @param node
     * @return Nodeから取得した真理値
     */
    private Boolean convertNodeValueToBoolean(Node node) {
        Boolean value = false;
        if (node != null) {
            value = Boolean.valueOf(node.getNodeValue());
        }
        return value;
    }

    /**
     * シンタックスエラーを追加する処理
     * @param errorMessages
     * @param syntaxErrors
     */
    private void addSyntaxErrors(List<String> errorMessages, SyntaxErrors syntaxErrors) {
        for (String message : errorMessages) {
            SyntaxError syntaxError = new SyntaxError(message, new UnsupportedOperationException());
            syntaxErrors.add(syntaxError);
        }
    }
}
