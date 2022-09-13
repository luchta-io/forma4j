package io.luchta.forma4j.reader.compile.parse.tagbuilder;

import io.luchta.forma4j.context.syntax.SyntaxError;
import io.luchta.forma4j.context.syntax.SyntaxErrors;
import io.luchta.forma4j.reader.model.tag.SheetTag;
import io.luchta.forma4j.reader.model.tag.Tag;
import io.luchta.forma4j.reader.model.tag.property.AllSheets;
import io.luchta.forma4j.reader.model.tag.property.Name;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * {@code SheetBuilder} は {@link SheetTag} を生成します
 *
 * @since 0.1.0
 */
public class SheetBuilder implements TagBuilder {
    /**
     * {@link SheetTag} を生成するメソッドです
     * <p>
     * エラーが発生すると設定ファイルの構文エラーとして {@code syntaxErrors} にエラー内容が追加されます。
     * </p>
     * @param nodeMap 設定ファイルのノードです
     * @param syntaxErrors 設定ファイルの構文エラーです
     * @return オブジェクトの生成に成功したとき: {@link SheetTag}, 失敗したとき: {@link Tag}
     */
    @Override
    public Tag build(NamedNodeMap nodeMap, SyntaxErrors syntaxErrors) {
        SheetTag.SheetTagBuilder builder = new SheetTag.SheetTagBuilder();

        String name = convertNodeValueToString(nodeMap.getNamedItem(Name.PROPERTY_NAME));
        builder.name(new Name(name));

        String allSheets = convertNodeValueToString(nodeMap.getNamedItem(AllSheets.PROPERTY_NAME));
        builder.allSheets(new AllSheets(Boolean.valueOf(allSheets)));

        try {
            SheetTag sheetTag = builder.build();
            return sheetTag;
        } catch (IllegalArgumentException e) {
            SyntaxError syntaxError = new SyntaxError(e.getMessage());
            syntaxErrors.add(syntaxError);
        }
        return new Tag() {};
    }

    private String convertNodeValueToString(Node node) {
        String value = "";
        if (node != null) {
            value = node.getNodeValue();
        }
        return value;
    }
}
