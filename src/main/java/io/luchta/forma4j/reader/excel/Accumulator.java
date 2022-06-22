package io.luchta.forma4j.reader.excel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Accumulator {
    private static final Map<String, Object> data = new ConcurrentHashMap<>();

    public static void setData(String key, Object value) {
        data.put(key, value);
    }

    public static Object getData(String key) {
        return data.get(key);
    }

    public static boolean containsKey(String key) {
        return data.containsKey(key);
    }
}
