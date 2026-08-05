package io.luchta.forma4j.writer.stream;

import java.io.IOException;
import java.util.Iterator;

/**
 * 使用後に閉じる必要がある Iterator です。
 *
 * @param <T> 要素型
 */
public interface CloseableIterator<T> extends Iterator<T>, AutoCloseable {
    @Override
    void close() throws IOException;
}
