package io.luchta.forma4j.writer.stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MappingSequenceSourceTest {
    @Test
    void mapsElementsInOrder() throws Exception {
        SequenceSource<String> source = source(
                Arrays.asList("one", "two", "three"),
                Replayability.REPLAYABLE
        ).map(String::toUpperCase);

        try (CloseableIterator<String> iterator = source.open()) {
            assertEquals("ONE", iterator.next());
            assertEquals("TWO", iterator.next());
            assertEquals("THREE", iterator.next());
            assertFalse(iterator.hasNext());
        }
    }

    @Test
    void mapsLazilyWhenNextIsCalled() throws Exception {
        AtomicInteger mappingCount = new AtomicInteger();
        SequenceSource<Integer> source = source(
                Arrays.asList(1, 2),
                Replayability.REPLAYABLE
        ).map(value -> {
            mappingCount.incrementAndGet();
            return value * 10;
        });

        assertEquals(0, mappingCount.get());
        try (CloseableIterator<Integer> iterator = source.open()) {
            assertEquals(0, mappingCount.get());
            assertTrue(iterator.hasNext());
            assertEquals(0, mappingCount.get());

            assertEquals(10, iterator.next());
            assertEquals(1, mappingCount.get());
            assertTrue(iterator.hasNext());
            assertEquals(1, mappingCount.get());

            assertEquals(20, iterator.next());
            assertEquals(2, mappingCount.get());
        }
    }

    @Test
    void delegatesCloseToSourceIterator() throws Exception {
        AtomicBoolean closed = new AtomicBoolean();
        SequenceSource<Integer> source = new SequenceSource<Integer>() {
            @Override
            public CloseableIterator<Integer> open() {
                Iterator<Integer> values = Arrays.asList(1, 2).iterator();
                return new CloseableIterator<Integer>() {
                    @Override
                    public boolean hasNext() {
                        return values.hasNext();
                    }

                    @Override
                    public Integer next() {
                        return values.next();
                    }

                    @Override
                    public void close() {
                        closed.set(true);
                    }
                };
            }

            @Override
            public Replayability replayability() {
                return Replayability.REPLAYABLE;
            }
        };

        try (CloseableIterator<String> ignored = source.map(String::valueOf).open()) {
            assertFalse(closed.get());
        }

        assertTrue(closed.get());
    }

    @ParameterizedTest
    @EnumSource(Replayability.class)
    void delegatesReplayability(Replayability replayability) {
        SequenceSource<String> mapped = source(
                java.util.Collections.<Integer>emptyList(),
                replayability
        ).map(String::valueOf);

        assertSame(replayability, mapped.replayability());
    }

    @Test
    void propagatesMapperExceptionUnchanged() throws Exception {
        RuntimeException expected = new IllegalStateException("mapping failed");
        SequenceSource<String> mapped = source(
                java.util.Collections.singletonList(1),
                Replayability.REPLAYABLE
        ).map(value -> {
            throw expected;
        });

        try (CloseableIterator<String> iterator = mapped.open()) {
            RuntimeException actual = assertThrows(RuntimeException.class, iterator::next);
            assertSame(expected, actual);
        }
    }

    @Test
    void rejectsNullMapper() {
        SequenceSource<Integer> source = source(
                java.util.Collections.singletonList(1),
                Replayability.REPLAYABLE
        );

        assertThrows(NullPointerException.class, () -> source.map(null));
    }

    private <T> SequenceSource<T> source(
            List<? extends T> values,
            Replayability replayability
    ) {
        return new SequenceSource<T>() {
            @Override
            public CloseableIterator<T> open() throws IOException {
                return CloseableIterators.from(values.iterator());
            }

            @Override
            public Replayability replayability() {
                return replayability;
            }
        };
    }
}
