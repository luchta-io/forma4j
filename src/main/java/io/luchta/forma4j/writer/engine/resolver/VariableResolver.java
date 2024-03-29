package io.luchta.forma4j.writer.engine.resolver;

import io.luchta.forma4j.writer.Context;
import io.luchta.forma4j.writer.engine.buffer.loop.LoopContext;
import io.luchta.forma4j.writer.engine.model.cell.value.Text;
import io.luchta.forma4j.writer.engine.model.cell.value.XlsxCellValue;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class VariableResolver {

    Context context;
    LoopContext loopContext;

    public VariableResolver(Context context, LoopContext loopContext) {
        this.context = context;
        this.loopContext = loopContext;
    }

    public XlsxCellValue get(String key) {
        // TODO とりあえず全部Text型にしてるのでちゃんと直す
        Object contextVar = getValue(key, context);
        if (contextVar != null) return new Text((String) contextVar);
        Object loopContextVar = getValue(key, loopContext);
        if (loopContextVar != null) return new Text((String) loopContextVar);
        return new Text();
    }

    public List<Object> getList(String key) {
        Object contextVar = getValue(key, context);
        if (contextVar != null) return (List<Object>) contextVar;
        Object loopContextVar = getValue(key, loopContext);
        if (loopContextVar != null) return (List<Object>) loopContextVar;
        return Collections.emptyList();
    }

    public Set<String> getKeySet() {
        return context.getKeys();
    }

    private Object getValue(String key, Context context) {
        Object value = context.getVar(key);
        if (value != null) return value;
        String first = key.split("\\.")[0];
        Object contextVar = context.getVar(first);
        if (contextVar == null) return null;

        String rewriteKey = key.replaceFirst(first + ".", "");

        if (contextVar instanceof Map) {
            Map<?, ?> map = (Map<?, ?>) contextVar;
            return map.get(rewriteKey);
        }
        return null;
    }

    private Object getValue(String key, LoopContext loopContext) {
        Object value = loopContext.getItem(key);
        if (value != null) return value;
        String first = key.split("\\.")[0];
        Object loopContextVar = loopContext.getItem(first);
        if (loopContextVar == null) return null;

        String rewriteKey = key.replaceFirst(first + ".", "");

        if (loopContextVar instanceof Map) {
            Map<?, ?> map = (Map<?, ?>) loopContextVar;
            return map.get(rewriteKey);
        }
        return null;
    }
}
