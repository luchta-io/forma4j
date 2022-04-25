package io.luchta.forma4j.context.type;

import java.util.HashMap;
import java.util.Map;

public class MapType implements Type {

    Map<String, Type> map;

    public MapType() {
        map = new HashMap<>();
    }

    public MapType(Map<String, Type> map) {
        this.map = map;
    }

    public int size() {
        return map.size();
    }

    public Map<String, Type> toMap() {
        return map;
    }

    @Override
    public boolean isMapType() {
        return true;
    }
}
