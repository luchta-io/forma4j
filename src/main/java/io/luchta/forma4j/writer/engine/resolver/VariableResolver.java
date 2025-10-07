package io.luchta.forma4j.writer.engine.resolver;

import io.luchta.forma4j.writer.Context;
import io.luchta.forma4j.writer.engine.buffer.loop.LoopContext;
import io.luchta.forma4j.writer.engine.model.cell.value.Bool;
import io.luchta.forma4j.writer.engine.model.cell.value.Numeric;
import io.luchta.forma4j.writer.engine.model.cell.value.Text;
import io.luchta.forma4j.writer.engine.model.cell.value.XlsxCellValue;

import java.math.BigDecimal;
import java.util.*;

/**
 * 変数解決クラス
 * <p>
 * 書き込み定義が記述されたXMLファイルの内容に従って変数の値を解決する
 * </p>
 */
public class VariableResolver {

    /** コンテキスト */
    private Context context;
    /** ループ処理用のコンテキスト */
    private LoopContext loopContext;

    /**
     * コンストラクタ
     * @param context
     * @param loopContext
     */
    public VariableResolver(Context context, LoopContext loopContext) {
        this.context = context;
        this.loopContext = loopContext;
    }

    /**
     * 変数の値を取得する処理
     * @param key
     * @return 変数の値（すべてText型として返る）
     */
    public XlsxCellValue get(String key) {
        Object contextVar = getValue(key, context);
        if (contextVar != null) return toXlsxCellValue(contextVar);
        Object loopContextVar = getValue(key, loopContext);
        if (loopContextVar != null) return toXlsxCellValue(loopContextVar);
        return new Text();
    }

    /**
     * 変数の値をListで取得する処理
     * @param key
     * @return 変数の値
     */
    public List<Object> getList(String key) {
        Object contextVar = getValue(key, context);
        if (contextVar != null) return (List<Object>) contextVar;
        Object loopContextVar = getValue(key, loopContext);
        if (loopContextVar != null) return (List<Object>) loopContextVar;
        return Collections.emptyList();
    }

    /**
     * contextに格納されているkeysetを返す
     * @return keyset
     */
    public Set<String> getKeySet() {
        return context.getKeys();
    }

    /**
     * 変数の値を取得する処理
     * @param key
     * @param context
     * @return 変数の値
     */
    private Object getValue(String key, Context context) {
        // 値が取得できたらそのまま返す
        Object value = context.getVar(key);
        if (value != null) return value;

        // 値が取得できない場合はドット区切りでkeyを分割してcontextの中を確認する
        String first = key.split("\\.")[0];
        Object contextVar = context.getVar(first);
        if (contextVar == null) return null;

        String rewriteKey = key.replaceFirst(first + ".", "");

        if (contextVar instanceof Map) {
            String[] rewriteKeys = rewriteKey.split("\\.", 255);
            return getValueFromVariable(rewriteKeys, (Map<?, ?>) contextVar);
        }

        return null;
    }

    /**
     * ループ処理の内部にある変数の値を取得する処理
     * @param key
     * @param loopContext
     * @return 変数の値
     */
    private Object getValue(String key, LoopContext loopContext) {
        Object value = loopContext.getItem(key);
        if (value != null) return value;
        String first = key.split("\\.")[0];
        Object loopContextVar = loopContext.getItem(first);
        if (loopContextVar == null) return null;

        String rewriteKey = key.replaceFirst(first + ".", "");

        if (loopContextVar instanceof Map) {
            String[] rewriteKeys = rewriteKey.split("\\.", 255);
            return getValueFromVariable(rewriteKeys, (Map<?, ?>) loopContextVar);
        }
        return null;
    }

    /**
     * 変数テーブルから値を取得する
     * @param keys
     * @param map
     * @return 値
     */
    private Object getValueFromVariable(String[] keys, Map<?, ?> map) {
        Object contextVar = null;
        Map<?, ?> current = map;
        for (int i = 0; i < keys.length; i ++) {
            String key = keys[i];
            contextVar = current.get(key);
            if (contextVar == null) {
                return null;
            }

            if (contextVar instanceof Map) {
                current = (Map<?, ?>) contextVar;
                continue;
            }
            return contextVar;
        }
        return null;
    }

    private XlsxCellValue<?> toXlsxCellValue(Object obj) {
        if (obj == null) return new Text();
        if (obj instanceof Number) return new Numeric(new BigDecimal(obj.toString()));
        if (obj instanceof Boolean) return new Bool((Boolean) obj);
        return new Text(String.valueOf(obj));
    }
}
