package io.luchta.forma4j.writer.definition.schema.attribute;

import javax.xml.bind.annotation.XmlValue;

public class Style {

	@XmlValue
	private String value;
	
	public Style() {}
	
	public Style(String value) {
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
