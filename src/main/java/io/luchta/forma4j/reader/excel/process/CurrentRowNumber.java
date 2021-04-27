package io.luchta.forma4j.reader.excel.process;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Objects;

public class CurrentRowNumber implements Serializable, Comparable<CurrentRowNumber> {

	private static final long serialVersionUID = 1L;
    
    private Integer value;
    
    public CurrentRowNumber() {
        this.value = null;
    }
    
    public CurrentRowNumber(Integer value) {
        this.value = value;
    }
    
    public CurrentRowNumber add(Integer value) {
    	return new CurrentRowNumber(this.value + value);
    }
    
    public boolean isEmpty() {
        return this.value == null;
    }
    
    public int intValue() {
        return this.value.intValue();
    }

    @Override
    public int compareTo(CurrentRowNumber o) {
        return Objects.compare(this.value, o.value, Comparator.nullsLast(Comparator.naturalOrder()));
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CurrentRowNumber currentRowNumber = (CurrentRowNumber) o;
        return this.value == currentRowNumber.value;
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
