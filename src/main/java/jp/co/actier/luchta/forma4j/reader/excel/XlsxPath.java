package jp.co.actier.luchta.forma4j.reader.excel;

import java.io.Serializable;
import java.util.Objects;

public class XlsxPath implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private String value;
    
    public XlsxPath() {}
    
    public XlsxPath(String value) {
        this.value = value;
    }
    
    public boolean isEmpty() {
        return this.value == null || this.value.isEmpty();
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        XlsxPath xlsxPath = (XlsxPath) o;
        return this.value.equals(xlsxPath.value);
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
