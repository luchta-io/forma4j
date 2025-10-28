package io.luchta.forma4j.writer.definition.schema.attribute.cell;

import jakarta.xml.bind.annotation.XmlValue;

public class CellType {

	@XmlValue
	private String value;

	public CellType() {}

	public CellType(String value) {
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
