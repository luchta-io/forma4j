package io.luchta.forma4j.writer;

import io.luchta.forma4j.context.databind.json.JsonNode;
import io.luchta.forma4j.context.databind.json.JsonNodes;
import io.luchta.forma4j.context.databind.json.JsonObject;
import io.luchta.forma4j.writer.stream.SequenceSource;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class Context {
    Map<String, Object> vars = new LinkedHashMap<>();

    public Context() {}

    Context(Map<String, Object> vars) {
        this.vars = vars;
    }

    public static Context from(JsonObject jsonObject) {
        if (jsonObject == null || jsonObject.isEmpty()) {
            return new Context();
        }

        if (!jsonObject.isJsonNode()) {
            throw new IllegalArgumentException("ルート要素には JsonNode を指定してください。");
        }

        return new Context(toMap((JsonNode) jsonObject.getValue()));
    }

    public void putVar(String key, Object value) {
        vars.put(key, value);
    }

    /**
     * 名前付きのストリーミングコレクションを登録します。
     *
     * @param key XML の collection 属性から参照する名前
     * @param source コレクションを開くSource
     */
    public void putSequence(String key, SequenceSource<?> source) {
        vars.put(key, Objects.requireNonNull(source, "source"));
    }

    public Object getVar(String key) {
        return vars.get(key);
    }

    public Set<String> getKeys() {
        return vars.keySet();
    }

    private static Map<String, Object> toMap(JsonNode jsonNode) {
        Map<String, Object> map = new LinkedHashMap<>();
        for (Map.Entry<String, JsonObject> entry : jsonNode.entrySet()) {
            map.put(entry.getKey(), toValue(entry.getValue()));
        }
        return map;
    }

    private static List<Object> toList(JsonNodes jsonNodes) {
        List<Object> list = new ArrayList<>();
        for (JsonNode jsonNode : jsonNodes) {
            list.add(toMap(jsonNode));
        }
        return list;
    }

    private static List<Object> toList(List<?> values) {
        List<Object> list = new ArrayList<>();
        for (Object value : values) {
            if (value instanceof JsonObject) {
                list.add(toValue((JsonObject) value));
            } else if (value instanceof JsonNode) {
                list.add(toMap((JsonNode) value));
            } else if (value instanceof JsonNodes) {
                list.add(toList((JsonNodes) value));
            } else if (value instanceof List) {
                list.add(toList((List<?>) value));
            } else {
                list.add(value);
            }
        }
        return list;
    }

    private static Object toValue(JsonObject jsonObject) {
        if (jsonObject == null || jsonObject.isEmpty()) {
            return null;
        }

        Object value = jsonObject.getValue();
        if (value instanceof JsonNode) {
            return toMap((JsonNode) value);
        }
        if (value instanceof JsonNodes) {
            return toList((JsonNodes) value);
        }
        if (value instanceof List) {
            return toList((List<?>) value);
        }
        return value;
    }
}
