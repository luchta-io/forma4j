package io.luchta.forma4j.writer.engine.resolver;

import io.luchta.forma4j.writer.Context;
import io.luchta.forma4j.writer.engine.buffer.loop.LoopContext;
import io.luchta.forma4j.writer.engine.model.cell.value.*;
import io.luchta.forma4j.writer.engine.model.cell.value.Date;
import io.luchta.forma4j.writer.stream.CloseableIterator;
import io.luchta.forma4j.writer.stream.SequenceSource;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    /** コレクションを開く処理 */
    private SequenceOpener sequenceOpener;

    /**
     * コンストラクタ
     * @param context
     * @param loopContext
     */
    public VariableResolver(Context context, LoopContext loopContext) {
        this(context, loopContext, VariableResolver::openDirect);
    }

    public VariableResolver(
            Context context,
            LoopContext loopContext,
            SequenceOpener sequenceOpener
    ) {
        this.context = context;
        this.loopContext = loopContext;
        this.sequenceOpener = sequenceOpener;
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
     * 変数の値を逐次処理可能な Iterable として取得します。
     * List を含む従来の Iterable 値もそのまま返します。
     */
    @SuppressWarnings("unchecked")
    public Iterable<Object> getIterable(String key) {
        Object contextVar = getValue(key, context);
        if (contextVar instanceof Iterable) return (Iterable<Object>) contextVar;
        Object loopContextVar = getValue(key, loopContext);
        if (loopContextVar instanceof Iterable) return (Iterable<Object>) loopContextVar;
        return Collections.emptyList();
    }

    /**
     * コレクションを1回の参照として開きます。
     * 返されたIterationは必ず閉じてください。
     */
    public Iteration openIteration(String key) {
        Object value = getValue(key, context);
        if (value == null) {
            value = getValue(key, loopContext);
        }
        return new Iteration(sequenceOpener.open(key, value));
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
        if (obj instanceof LocalDate) return new Date((LocalDate) obj);
        if (obj instanceof LocalDateTime) return new DateTime((LocalDateTime) obj);

        String s = String.valueOf(obj);
        try {
            return new Date(LocalDate.parse(s, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        } catch (Exception ignored) {}

        try {
            return new DateTime(LocalDateTime.parse(s, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS")));
        } catch (Exception ignored) {}

        return new Text(String.valueOf(obj));
    }

    @SuppressWarnings("unchecked")
    private static CloseableIterator<Object> openDirect(String name, Object value) {
        try {
            if (value instanceof SequenceSource<?>) {
                return (CloseableIterator<Object>) ((SequenceSource<?>) value).open();
            }
            if (value instanceof Iterable<?>) {
                return closeable((Iterator<Object>) ((Iterable<?>) value).iterator());
            }
            return closeable(Collections.emptyIterator());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private static CloseableIterator<Object> closeable(Iterator<Object> iterator) {
        if (iterator instanceof CloseableIterator<?>) {
            return (CloseableIterator<Object>) iterator;
        }
        return new CloseableIterator<Object>() {
            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public Object next() {
                return iterator.next();
            }

            @Override
            public void remove() {
                iterator.remove();
            }

            @Override
            public void close() throws IOException {
                if (!(iterator instanceof AutoCloseable)) {
                    return;
                }
                try {
                    ((AutoCloseable) iterator).close();
                } catch (IOException e) {
                    throw e;
                } catch (Exception e) {
                    throw new IOException("Iterator のクローズに失敗しました。", e);
                }
            }
        };
    }

    @FunctionalInterface
    public interface SequenceOpener {
        CloseableIterator<Object> open(String name, Object value);
    }

    /**
     * close時のIOExceptionをUncheckedIOExceptionとして伝播する反復スコープです。
     */
    public static final class Iteration implements Iterator<Object>, AutoCloseable {
        private final CloseableIterator<Object> delegate;

        private Iteration(CloseableIterator<Object> delegate) {
            this.delegate = delegate;
        }

        @Override
        public boolean hasNext() {
            return delegate.hasNext();
        }

        @Override
        public Object next() {
            return delegate.next();
        }

        @Override
        public void remove() {
            delegate.remove();
        }

        @Override
        public void close() {
            try {
                delegate.close();
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }
    }
}
