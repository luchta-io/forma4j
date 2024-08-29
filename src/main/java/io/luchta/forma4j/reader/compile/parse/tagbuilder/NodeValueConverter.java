package io.luchta.forma4j.reader.compile.parse.tagbuilder;

import org.w3c.dom.Node;

/**
 * XMLのノード値変換クラス
 * @since v1.8.0
 */
public class NodeValueConverter {
    /**
     * {@link org.w3c.dom.Node} の値を {@link Integer} に変換します
     * <p>
     * 値を取得できないときは {@code defaultValue} に変換されます
     * </p>
     * @param node
     * @param defaultValue
     * @return {@link Integer}
     */
    public static Integer toInteger(Node node, Integer defaultValue) {
        Integer value = defaultValue;
        if (node == null) {
            return value;
        }

        Integer rowNumber = null;
        try {
            rowNumber = Integer.parseInt(node.getNodeValue());
        } catch (NumberFormatException e) {
            return value;
        }

        if (rowNumber >= 0) {
            value = Integer.parseInt(node.getNodeValue());
        }

        return value;
    }

    /**
     * {@link org.w3c.dom.Node} の値を {@link String} に変換します
     * <p>
     * 値を取得できないときは {@code defaultValue} に変換されます
     * </p>
     * @param node
     * @return {@link String}
     */
    public static String toString(Node node, String defaultValue) {
        String value = defaultValue;
        if (node != null) {
            return node.getNodeValue();
        }
        return value;
    }

    /**
     * {@link org.w3c.dom.Node} の値を {@link Boolean} に変換します
     * <p>
     * 値を取得できないときは {@code defaultValue} に変換されます
     * </p>
     * @param node
     * @param defaultValue
     * @return {@link Boolean}
     */
    public static Boolean toBoolean(Node node, Boolean defaultValue) {
        Boolean value = defaultValue;

        if (node == null) {
            return value;
        }

        try {
            value = Boolean.valueOf(node.getNodeValue());
        } catch (Exception ex) {
            return value;
        }

        return value;
    }
}
