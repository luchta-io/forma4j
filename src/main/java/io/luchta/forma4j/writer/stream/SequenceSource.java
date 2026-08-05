package io.luchta.forma4j.writer.stream;

import java.io.IOException;
import java.util.function.Function;

/**
 * コレクションを全件保持せず、先頭から開くためのデータソースです。
 *
 * <p>{@link java.util.Iterator#hasNext()} または {@link java.util.Iterator#next()}
 * で発生したIOExceptionは {@link java.io.UncheckedIOException} で通知してください。
 * FormaStreamingWriterは元のIOExceptionへ戻して呼び出し元へ通知します。</p>
 *
 * @param <T> 要素型
 */
public interface SequenceSource<T> {
    CloseableIterator<T> open() throws IOException;

    Replayability replayability();

    default <R> SequenceSource<R> map(
            Function<? super T, ? extends R> mapper
    ) {
        return new MappingSequenceSource<>(this, mapper);
    }
}
