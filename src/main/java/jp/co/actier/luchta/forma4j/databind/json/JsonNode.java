package jp.co.actier.luchta.forma4j.databind.json;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class JsonNode {

	Map<String, JsonObject> var = new HashMap<>();
	
	public void putVar(String key, JsonObject object) {
		this.var.put(key, object);
	}
	
	public Object getVar(String key) {
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
