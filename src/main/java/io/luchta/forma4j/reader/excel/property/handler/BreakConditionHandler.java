package io.luchta.forma4j.reader.excel.property.handler;

import io.luchta.forma4j.context.databind.json.JsonNode;
import io.luchta.forma4j.context.databind.json.JsonNodes;
import io.luchta.forma4j.context.databind.json.JsonObject;
import io.luchta.forma4j.reader.model.tag.property.BreakCondition;
import io.luchta.forma4j.reader.model.tag.property.BreakType;

import java.util.Map;

/**
 * {@code BreakConditionHandler} は {@code break} プロパティのハンドラです
 * <p>
 *  {@link io.luchta.forma4j.reader.model.tag.property.BreakCondition} に設定された条件に従ってブレイクの判定を行います。
 * </p>
 *
 * @since 1.8.0
 */
public class BreakConditionHandler {
    /** break条件 */
    private BreakCondition condition;

    /**
     * コンストラクタ
     * @param condition break条件
     */
    public BreakConditionHandler(BreakCondition condition) {
        this.condition = condition;
    }

    /**
     * breakするかをチェック
     * @param node
     * @return true: breakする, false: breakしない
     */
    public boolean shouldBreak(JsonNode node) {
        BreakType type = condition.breakType();
        switch(type) {
            case ALL_BLANK: return allBlank(node);
            default: return false;
        }
    }

    /**
     * break条件がall_breakのとき
     * @param node
     * @return true: breakする, false: breakしない
     */
    private boolean allBlank(JsonNode node) {
        boolean shouldBreak = true;
        for (Map.Entry<String, JsonObject> entry : node.entrySet()) {
            JsonObject obj = entry.getValue();
            if (obj.isJsonNodes()) {
                for (JsonNode n : (JsonNodes) obj.getValue()) {
                    shouldBreak = allBlank(n);
                }
            } else {
                shouldBreak = obj.isEmpty() || obj.getValue().toString().equals("");
            }
        }
        return shouldBreak;
    }
}
