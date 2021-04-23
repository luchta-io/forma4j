package jp.co.actier.luchta.forma4j.reader.config.element.property;

import java.io.Serializable;
import java.util.Objects;

public class ElementName implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private String value;
    
    public ElementName() {}
    
    public ElementName(String value) {
        this.value = value;
    }
    
    public boolean isEmpty() {
        return this.value == null || this.value.isEmpty();
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        ElementName elementName = (ElementName) o;
        return this.value.equals(elementName.value);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(this.value);
    }
    
    @Override
    public String toString() {
        return this.value.toString();
    }
}
