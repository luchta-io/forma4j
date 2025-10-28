package io.luchta.forma4j.writer.definition.schema.attribute.cell;

import jakarta.xml.bind.annotation.XmlValue;

public class DataFormat {

	@XmlValue
	private String value;

	public DataFormat() {}

	public DataFormat(String value) {
		this.value = value;
	}
	
	public boolean isEmpty() {
		return this.value == null || "".equals(this.value);
	}
	
	@Override
    public String toString() {
        if (value == null) return "";
        return value;
    }
}
