package io.luchta.forma4j.reader.excel.objectreader;

import io.luchta.forma4j.context.databind.json.JsonNode;
import io.luchta.forma4j.context.databind.json.JsonNodes;
import io.luchta.forma4j.context.databind.json.JsonObject;
import io.luchta.forma4j.reader.excel.property.handler.BreakConditionHandler;
import io.luchta.forma4j.reader.model.excel.Index;
import io.luchta.forma4j.reader.model.tag.Tag;
import io.luchta.forma4j.reader.model.tag.TagTree;
import io.luchta.forma4j.reader.model.tag.TagTrees;
import io.luchta.forma4j.reader.model.tag.VForTag;
import io.luchta.forma4j.reader.model.tag.property.BreakCondition;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * {@code VForReader} はv-forタグのExcel読み込みを実行するクラスです
 *
 * @since 0.1.0
 */
public class VForReader implements ObjectReader {
    /** シート */
    private Sheet sheet;
    /** 行番号 */
    private Index rowIndex;
    /** 列番号 */
    private Index colIndex;
    /** XML定義をツリー構造化したオブジェクト */
    private TagTree tagTree;

    /**
     * コンストラクタ
     * @param sheet
     * @param rowIndex
     * @param colIndex
     * @param tagTree
     */
    public VForReader(Sheet sheet, Index rowIndex, Index colIndex, TagTree tagTree) {
        this.sheet = sheet;
        this.rowIndex = rowIndex;
        this.colIndex = colIndex;
        this.tagTree = tagTree;
    }

    /**
     * 読み込み処理
     * @return 読み込み結果
     */
    @Override
    public JsonNode read() {
        VForTag vForTag = (VForTag) tagTree.getTag();

        // v-forタグのプロパティの設定値を取得する
        Integer fromRow = vForTag.fromIsUndefined() ? rowIndex.toInteger() : vForTag.from().toInteger();
        Integer toRow = vForTag.to().toInteger();
        Integer col = colIndex.toInteger();

        // 読み込み
        ObjectReaderFactory factory = new ObjectReaderFactory();
        List<JsonNode> nodeList = new ArrayList<>();
        Integer i = fromRow;
        do {
            // 子要素をループしながら縦方向にExcelを読み込む
            JsonNode childrenNode = new JsonNode();
            TagTrees children = tagTree.getChildren();
            for (TagTree child : children) {
                Tag tag = child.getTag();
                ObjectReaderFactoryParameter param = new ObjectReaderFactoryParameter(
                        sheet, new Index(i), colIndex, child, tag
                );
                ObjectReader reader = factory.create(param);
                JsonNode node = reader.read();
                for (Map.Entry<String, JsonObject> entry : node.entrySet()) {
                    childrenNode.putVar(entry.getKey(), entry.getValue());
                }
            }

            // ブレイク判定
            BreakConditionHandler handler = new BreakConditionHandler(vForTag.breakCondition());
            if (handler.shouldBreak(childrenNode)) {
                break;
            }

            if (childrenNode.size() != 0) {
                nodeList.add(childrenNode);
            }

            // ループの完了判定
            if (!vForTag.toIsUndefined() && toRow < i) {
                break;
            } else if (vForTag.toIsUndefined() && sheet.getLastRowNum() < i) {
                break;
            } else if (sheet.getLastRowNum() == i) {
                break;
            }
            i++;
        } while (true);

        JsonNode node = new JsonNode();
        node.putVar(vForTag.name().toString(), new JsonObject(new JsonNodes(nodeList)));

        return node;
    }
}
