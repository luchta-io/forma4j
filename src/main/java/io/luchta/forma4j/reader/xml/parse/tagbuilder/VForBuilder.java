package io.luchta.forma4j.reader.xml.parse.tagbuilder;

import io.luchta.forma4j.context.syntax.SyntaxError;
import io.luchta.forma4j.context.syntax.SyntaxErrors;
import io.luchta.forma4j.reader.model.tag.Tag;
import io.luchta.forma4j.reader.model.tag.VForTag;
import io.luchta.forma4j.reader.model.tag.property.BreakCondition;
import io.luchta.forma4j.reader.model.tag.property.Index;
import io.luchta.forma4j.reader.model.tag.property.Name;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.List;

/**
 * {@code VForBuilder} はv-forタグの内容を読み取ってオブジェクト化するクラスです
 *
 * @since 0.1.0
 */
public class VForBuilder implements TagBuilder {
    /**
     * v-forタグのオブジェクト化を行います
     * @param nodeMap
     * @param syntaxErrors
     * @return オブジェクト化されたv-forタグ。{@link io.luchta.forma4j.reader.model.tag.VForTag} に変換されます。
     */
    @Override
    public Tag build(NamedNodeMap nodeMap, SyntaxErrors syntaxErrors) {
        List<String> errorMessages = new ArrayList<>();

        // fromプロパティの読み込み
        Node fromNode = nodeMap.getNamedItem("from");
        boolean fromIsUndefined = false;
        if (fromNode == null) {
            fromIsUndefined = true;
        }
        Integer fromNumber = convertNodeValueToInteger(fromNode);
        if (fromNumber == null) {
            errorMessages.add("from には0以上の整数を指定してください");
        }

        // toプロパティの読み込み
        Node toNode = nodeMap.getNamedItem("to");
        boolean toIsUndefined = false;
        if (toNode == null) {
            toIsUndefined = true;
        }
        Integer toNumber = convertNodeValueToInteger(nodeMap.getNamedItem("to"));
        if (toNumber == null) {
            errorMessages.add("to には0以上の整数を指定してください");
        }

        // nameプロパティの読み込み
        String name = convertNodeValueToString(nodeMap.getNamedItem("name"));
        if (name == null) {
            errorMessages.add("name は必須入力です");
        }

        // breakプロパティの読み込み
        String breakCondition = convertNodeValueToString(nodeMap.getNamedItem("break"));

        addSyntaxErrors(errorMessages, syntaxErrors);

        return new VForTag(new Index(fromNumber), fromIsUndefined, new Index(toNumber), toIsUndefined, new Name(name), new BreakCondition(breakCondition));
    }

    /**
     * XMLから取得した値を数値に変換します
     * @param node
     * @return 取得値を数値に変換した値
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
     * XMLから取得した値を文字列に変換します
     * @param node
     * @return 取得値を文字列に変換した値
     */
    private String convertNodeValueToString(Node node) {
        String value = "";
        if (node != null) {
            value = node.getNodeValue();
        }
        return value;
    }

    /**
     * {@link io.luchta.forma4j.context.syntax.SyntaxErrors} へエラーを追加します
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
