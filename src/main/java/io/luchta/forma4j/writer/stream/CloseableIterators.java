package io.luchta.forma4j.writer.stream;

import java.io.IOException;
import java.util.Iterator;
import java.util.Objects;

final class CloseableIterators {
    private CloseableIterators() {
    }

    @SuppressWarnings("unchecked")
    static <T> CloseableIterator<T> from(Iterator<? extends T> iterator) {
        Objects.requireNonNull(iterator, "iterator");
        if (iterator instanceof CloseableIterator<?>) {
            return (CloseableIterator<T>) iterator;
        }
        return new CloseableIterator<T>() {
            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public T next() {
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

    static <T> CloseableIterator<T> empty() {
        return from(java.util.Collections.<T>emptyList().iterator());
    }
}
