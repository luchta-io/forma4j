package io.luchta.forma4j.databind.convert;

import io.luchta.forma4j.databind.json.JsonNode;
import io.luchta.forma4j.databind.json.JsonNodes;
import io.luchta.forma4j.databind.json.JsonObject;

import java.util.List;
import java.util.Map;

public class JsonSerializer {
	
	public String serializeFromJsonObject(JsonObject jsonObject) {
		
		if (jsonObject.isJsonNode()) {
			return this.serialize((JsonNode) jsonObject.getValue());
		}
		
		if (jsonObject.isJsonNodes()) {
			return this.serialize((JsonNodes) jsonObject.getValue());
		}

		if (jsonObject.isList()) {
		    return this.serialize((List) jsonObject.getValue());
        }
		
		return jsonObject.toString();
	}

	private <T> String serialize(List<T> list) {

	    if (list.size() == 0) {
	        return "[]";
        }

	    StringBuilder sb = new StringBuilder();
	    sb.append("[");
	    for (T t : list) {

	        if (t instanceof JsonNode) {
	            sb.append(this.serialize((JsonNode) t));
            } else if (t instanceof JsonNodes) {
	            sb.append(this.serialize((JsonNodes) t));
            } else if (t instanceof JsonObject) {
                sb.append(this.serializeFromJsonObject((JsonObject) t));
            } else if (t instanceof List) {
                sb.append(this.serialize((List) t));
            } else {
	            sb.append("\"");
                sb.append(escape(t.toString()));
                sb.append("\"");
            }
            sb.append(", ");
        }
        sb.delete(sb.length() - 2, sb.length());
        sb.append("]");
        return sb.toString();
    }
    
    private String serialize(JsonNode jsonNode) {
        
        if (jsonNode.size() == 0) {
            return "{}";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for (Map.Entry<String, JsonObject> e : jsonNode.entrySet()) {
            sb.append("\"");
            sb.append(e.getKey());
            sb.append("\" : ");
            if (e.getValue().getValue() instanceof JsonNodes) {
                sb.append(this.serialize((JsonNodes)e.getValue().getValue()));
            } else if (e.getValue().getValue() instanceof JsonNode) {
                sb.append(this.serialize((JsonNode)e.getValue().getValue()));
            } else if (e.getValue().getValue() instanceof List) {
                sb.append((this.serialize((List)e.getValue().getValue())));
            } else {
                sb.append("\"");
                sb.append(e.getValue().getValue() == null ? "" : escape(e.getValue().getValue().toString()));
                sb.append("\"");
            }
            sb.append(", ");
        }
        sb.delete(sb.length() - 2, sb.length());
        sb.append("}");
        return sb.toString();
    }
    
    private String serialize(JsonNodes jsonNodes) {
    
        if (jsonNodes.size() == 0) {
            return "{}";
        }
    
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (JsonNode jsonNode : jsonNodes) {
            sb.append(this.serialize(jsonNode));
            sb.append(",");
        }
        sb.delete(sb.length() - 1, sb.length());
        sb.append("]");
        return sb.toString();
    }

    private String escape(String value) {
        String result = value;
        result = result.replaceAll("\\\\", "\\\\\\\\");
        result = result.replaceAll("\"", "\\\\\"");
        result = result.replaceAll("\b", "\\\\\b");
        result = result.replaceAll("\r", "\\\\\r");
        result = result.replaceAll("\n", "\\\\\n");
        result = result.replaceAll("\t", "\\\\\t");
        return result;
    }
}
