package io.luchta.forma4j.reader.excel.process;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Objects;

public class CurrentColNumber implements Serializable, Comparable<CurrentColNumber> {

	private static final long serialVersionUID = 1L;
    
    private Integer value;
    
    public CurrentColNumber() {
        this.value = null;
    }
    
    public CurrentColNumber(Integer value) {
        this.value = value;
    }
    
    public CurrentColNumber add(Integer value) {
    	return new CurrentColNumber(this.value + value);
    }
    
    public boolean isEmpty() {
        return this.value == null;
    }
    
    public int intValue() {
        return this.value.intValue();
    }

    @Override
    public int compareTo(CurrentColNumber o) {
        return Objects.compare(this.value, o.value, Comparator.nullsLast(Comparator.naturalOrder()));
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CurrentColNumber currentColNumber = (CurrentColNumber) o;
        return this.value == currentColNumber.value;
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
