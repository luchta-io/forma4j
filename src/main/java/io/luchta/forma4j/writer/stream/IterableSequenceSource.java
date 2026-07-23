package io.luchta.forma4j.writer.stream;

import java.io.IOException;
import java.util.Iterator;
import java.util.Objects;

/**
 * Java の Iterator または Iterable を SequenceSource として公開します。
 *
 * @param <T> 要素型
 */
public final class IterableSequenceSource<T> implements SequenceSource<T> {
    private final Iterator<? extends T> oneShotIterator;
    private final Iterable<? extends T> replayableIterable;
    private final Replayability replayability;
    private boolean opened;

    private IterableSequenceSource(
            Iterator<? extends T> oneShotIterator,
            Iterable<? extends T> replayableIterable,
            Replayability replayability
    ) {
        this.oneShotIterator = oneShotIterator;
        this.replayableIterable = replayableIterable;
        this.replayability = replayability;
    }

    public static <T> IterableSequenceSource<T> oneShot(Iterator<? extends T> iterator) {
        return new IterableSequenceSource<>(
                Objects.requireNonNull(iterator, "iterator"),
                null,
                Replayability.ONE_SHOT
        );
    }

    public static <T> IterableSequenceSource<T> replayable(Iterable<? extends T> iterable) {
        return new IterableSequenceSource<>(
                null,
                Objects.requireNonNull(iterable, "iterable"),
                Replayability.REPLAYABLE
        );
    }

    @Override
    public synchronized CloseableIterator<T> open() throws IOException {
        if (replayability == Replayability.ONE_SHOT) {
            if (opened) {
                throw new IOException("One-shot sequence has already been opened.");
            }
            opened = true;
            return CloseableIterators.from(oneShotIterator);
        }
        return CloseableIterators.from(replayableIterable.iterator());
    }

    @Override
    public Replayability replayability() {
        return replayability;
    }
}
