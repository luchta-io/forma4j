package jp.co.actier.luchta.forma4j.writer.engine.resolver;

import jp.co.actier.luchta.forma4j.writer.Context;
import jp.co.actier.luchta.forma4j.writer.engine.buffer.loop.LoopContext;
import jp.co.actier.luchta.forma4j.writer.engine.model.cell.value.Text;
import jp.co.actier.luchta.forma4j.writer.engine.model.cell.value.XlsxCellValue;
import ognl.*;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Member;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class VariableResolver {
    static final OgnlContext ognlContext = new OgnlContext(
            new DefaultClassResolver(),
            new DefaultTypeConverter(),
            new AbstractMemberAccess() {
                // TODO イマイチ仕組みを理解しきれていない。やりたいのはpublic以外のfieldにもアクセスさせたい、というだけ
                // TODO mybatisの実装を参考に refs. https://github.com/mybatis/mybatis-3/issues/1258
                @Override
                public Object setup(Map context, Object target, Member member, String propertyName) {
                    AccessibleObject accessible = (AccessibleObject) member;
                    accessible.setAccessible(true);
                    return Boolean.FALSE;
                }

                @Override
                public boolean isAccessible(Map context, Object target, Member member, String propertyName) {
                    return true;
                }
            }
    );

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

    private Object getValue(String key, Context context) {
        // TODO かなりいい加減なので見直す
        Object value = context.getVar(key);
        if (value != null) return value;
        String first = key.split("\\.")[0];
        Object contextVar = context.getVar(first);
        if (contextVar == null) return null;
        try {
            String rewriteKey = key.replaceFirst(first, "");
            return Ognl.getValue(rewriteKey, ognlContext, contextVar);
        } catch (OgnlException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Object getValue(String key, LoopContext loopContext) {
        // TODO かなりいい加減なので見直す
        Object value = loopContext.getItem(key);
        if (value != null) return value;
        String first = key.split("\\.")[0];
        Object loopContextVar = loopContext.getItem(first);
        if (loopContextVar == null) return null;
        try {
            String rewriteKey = key.replaceFirst(first + ".", "");
            return Ognl.getValue(rewriteKey, ognlContext, loopContextVar);
        } catch (OgnlException e) {
            e.printStackTrace();
        }
        return null;
    }
}
