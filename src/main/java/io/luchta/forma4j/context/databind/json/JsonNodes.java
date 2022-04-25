package io.luchta.forma4j.context.databind.json;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class JsonNodes implements Iterable<JsonNode> {

	List<JsonNode> list = new ArrayList<>();
	
	public JsonNodes() {}
	
	public JsonNodes(List<JsonNode> list) {
		this.list = list;
	}
	
	public JsonNodes add(JsonNode jsonNode) {
		this.list.add(jsonNode);
		return new JsonNodes(this.list);
	}

	public JsonNodes addAll(JsonNodes jsonNodes) {
		this.list.addAll(jsonNodes.list);
		return new JsonNodes(this.list);
	}
	
	public JsonNode get(int index) {
		return this.list.get(index);
	}
	
	public int size() {
		return this.list.size();
	}
	
	public boolean isEmpty() {
		return this.list == null || this.size() == 0;
	}

	@Override
	public Iterator<JsonNode> iterator() {
		return this.list.iterator();
	}
}
