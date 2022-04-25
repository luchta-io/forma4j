package io.luchta.forma4j.context.databind.convert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.luchta.forma4j.context.databind.json.JsonNodes;
import io.luchta.forma4j.context.databind.json.JsonObject;
import io.luchta.forma4j.context.databind.json.JsonNode;

public class JsonDeserializer {

	public JsonObject deserializeFromJson(String json) throws JsonMappingException, JsonProcessingException {
		
		TypeReference<HashMap<String, Object>> reference = new TypeReference<HashMap<String, Object>>() {};
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> map = mapper.readValue(json, reference);
		return this.deserializeFromMap(map);
	}
	
	public JsonObject deserializeFromMap(Map<String, Object> map) {
		
		JsonNode jsonNode = new JsonNode();
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			jsonNode.putVar(entry.getKey(), this.interfiling(entry.getValue()));
		}
		return new JsonObject(jsonNode);
	}

	public <T> JsonObject deserializeFromList(List<T> list) {

		List<JsonObject> jsonObjects = new ArrayList<>();
		JsonNodes jsonNodes = new JsonNodes();
		for (T t : list) {
			jsonObjects.add(this.interfiling(t));
		}

		return new JsonObject(jsonObjects);
	}
	
	private JsonObject interfiling(Object object) {
		
		if (object instanceof Map) {
			return this.deserializeFromMap((Map<String, Object>) object);
		}
		
		if (object instanceof List) {
			return this.deserializeFromList((List) object);
		}
		
		return new JsonObject(object.toString());
	}
}
