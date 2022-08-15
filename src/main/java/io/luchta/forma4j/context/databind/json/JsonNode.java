package io.luchta.forma4j.context.databind.json;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class JsonNode {

	Map<String, JsonObject> var = new LinkedHashMap<>();
	
	public void putVar(String key, JsonObject object) {
		this.var.put(key, object);
	}
	
	public JsonObject getVar(String key) {
        return this.var.get(key);
    }

    public Set<String> getKeys() {
        return this.var.keySet();
    }
    
    public int size() {
    	return this.var.size();
    }
    
    public Set<Map.Entry<String, JsonObject>> entrySet() {
    	return this.var.entrySet();
    }
}
