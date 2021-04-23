package jp.co.actier.luchta.forma4j.reader.config.element.property;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Objects;

public class RowNumber implements Serializable, Comparable<RowNumber> {
    
    private static final long serialVersionUID = 1L;
    
    private Integer value;
    
    public RowNumber() {
        this.value = null;
    }
    
    public RowNumber(Integer value) {
        this.value = value;
    }
    
    public boolean isEmpty() {
        return this.value == null;
    }
    
    public int intValue() {
        return this.value.intValue();
    }

    @Override
    public int compareTo(RowNumber o) {
        return Objects.compare(this.value, o.value, Comparator.nullsLast(Comparator.naturalOrder()));
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RowNumber rowNumber = (RowNumber) o;
        return this.value == rowNumber.value;
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
