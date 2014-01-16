package in.live.at.vigneshchennai.syncedDB;

import java.util.HashMap;
import java.util.Map;

public class SimpleJson {
	private Map<String, String> json = new HashMap<>();
	
	private String individualValue(Object value) {
		if(value instanceof Float
				|| value instanceof Integer
				|| value instanceof Double
				|| value instanceof Short
				|| value instanceof SimpleJson) {
			return String.valueOf(value);
		} else {
			return "\"" + String.valueOf(value) + "\"";
		}
	}
	
	public <T> void putArray(String name, T[] values) {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for(T value: values) {
			String v = individualValue(value);
			sb.append(v);
			sb.append(",");
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.append("]");
		json.put("\"" + name + "\"", sb.toString());
	}
	
	public void put(String name, Object value) {
		String v = individualValue(value);
		json.put("\"" + name + "\"", v);
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		for(Map.Entry<String, String> entry: json.entrySet()) {
			sb.append(entry.getKey());
			sb.append(": ").append(entry.getValue());
			sb.append(",");
		}
		sb.append("}");
		return null;
	}
}
