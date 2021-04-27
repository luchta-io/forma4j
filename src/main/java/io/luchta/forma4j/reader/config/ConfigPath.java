package io.luchta.forma4j.reader.config;

import java.io.Serializable;
import java.util.Objects;

public class ConfigPath implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private String value;
    
    public ConfigPath() {}
    
    public ConfigPath(String value) {
        this.value = value;
    }
    
    public boolean isEmpty() {
        return this.value == null || this.value.isEmpty();
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        ConfigPath configPath = (ConfigPath) o;
        return this.value.equals(configPath.value);
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
