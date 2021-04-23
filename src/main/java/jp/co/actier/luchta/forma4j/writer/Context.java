package jp.co.actier.luchta.forma4j.writer;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Context {
    Map<String, Object> vars = new HashMap<>();

    public Context() {}

    Context(Map<String, Object> vars) {
        this.vars = vars;
    }

    public void putVar(String key, Object value) {
        vars.put(key, value);
    }

    public Object getVar(String key) {
        return vars.get(key);
    }

    public Set<String> getKeys() {
        return vars.keySet();
    }
}
