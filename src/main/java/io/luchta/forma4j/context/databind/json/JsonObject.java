package io.luchta.forma4j.context.databind.json;

import java.time.LocalDateTime;
import java.util.List;

public class JsonObject {

	private Object value;
	
	public JsonObject() {
		this.value = null;
	}
	
	public JsonObject(int value) {
		this.value = value;
	}
	
	public JsonObject(double value) {
		this.value = value;
	}

	public JsonObject(boolean value) {
		this.value = value;
	}
	
	public JsonObject(String value) {
		this.value = value;
	}
	
	public JsonObject(LocalDateTime value) {
		this.value = value;
	}
	
	public <T> JsonObject(List<T> list) {
		this.value = list;
	}
	
	public JsonObject(JsonNode value) {
		this.value = value;
	}
	
	public JsonObject(JsonNodes value) {
		this.value = value;
	}
	
	public boolean isJsonNode() {
		return this.value instanceof JsonNode;
	}
	
	public boolean isJsonNodes() {
		return this.value instanceof JsonNodes;
	}
	
	public boolean isList() {
		return this.value instanceof List;
	}
	
	public boolean isValue() {
		return !this.isEmpty() && !this.isList() && !this.isJsonNode() && !this.isJsonNodes();
	}
	
	public boolean isEmpty() {
		return this.value == null;
	}
	
	public Object getValue() {
		return this.value;
	}
	
	@Override
	public String toString() {
		if (isEmpty()) return "";
		return this.value.toString();
	}
}
