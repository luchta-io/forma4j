package io.luchta.forma4j.reader.config.element.property;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Objects;

public class SheetIndex implements Serializable, Comparable<SheetIndex> {

    private static final long serialVersionUID = 1L;
    
    private Integer value;
    
    public SheetIndex() {
        this.value = null;
    }
    
    public SheetIndex(Integer value) {
        this.value = value;
    }
    
    public boolean isEmpty() {
        return this.value == null;
    }
    
    public int intValue() {
        return this.value.intValue();
    }

    @Override
    public int compareTo(SheetIndex o) {
        return Objects.compare(this.value, o.value, Comparator.nullsLast(Comparator.naturalOrder()));
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SheetIndex sheetIndex = (SheetIndex) o;
        return this.value == sheetIndex.value;
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
