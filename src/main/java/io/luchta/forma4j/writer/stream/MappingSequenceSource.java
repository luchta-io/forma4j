package io.luchta.forma4j.writer.stream;

import java.io.IOException;
import java.util.Objects;
import java.util.function.Function;

public final class MappingSequenceSource<S, T> implements SequenceSource<T> {
    private final SequenceSource<? extends S> source;
    private final Function<? super S, ? extends T> mapper;

    MappingSequenceSource(
            SequenceSource<? extends S> source,
            Function<? super S, ? extends T> mapper
    ) {
        this.source = Objects.requireNonNull(source);
        this.mapper = Objects.requireNonNull(mapper);
    }

    @Override
    public CloseableIterator<T> open() throws IOException {
        CloseableIterator<? extends S> iterator = source.open();

        return new CloseableIterator<>() {
            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public T next() {
                return mapper.apply(iterator.next());
            }

            @Override
            public void close() throws IOException {
                iterator.close();
            }
        };
    }

    @Override
    public Replayability replayability() {
        return source.replayability();
    }
}
