package io.luchta.forma4j.writer.stream;

import java.io.IOException;
import java.util.Objects;

/**
 * 初回参照時に Writer 管理の一時ファイルへ退避する SequenceSource です。
 *
 * <p>要素は Map、List、文字列、数値、真偽値、null で構成される
 * JSON 互換値である必要があります。</p>
 *
 * @param <T> 要素型
 */
public final class SpooledSequenceSource<T> implements SequenceSource<T> {
    private final SequenceSource<? extends T> delegate;

    private SpooledSequenceSource(SequenceSource<? extends T> delegate) {
        this.delegate = delegate;
    }

    public static <T> SpooledSequenceSource<T> from(SequenceSource<? extends T> source) {
        return new SpooledSequenceSource<>(Objects.requireNonNull(source, "source"));
    }

    @Override
    public CloseableIterator<T> open() throws IOException {
        return CloseableIterators.from(delegate.open());
    }

    @Override
    public Replayability replayability() {
        return Replayability.SPOOLED;
    }
}
