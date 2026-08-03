package io.luchta.forma4j.writer.stream;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * ルート JSON 配列を要素単位で読み込む SequenceSource です。
 *
 * @param <T> JSONから生成されるMap、List、またはスカラーの要素型
 */
public final class JsonArraySequenceSource<T> implements SequenceSource<T> {
    private final InputStreamSupplier supplier;
    private final Replayability replayability;
    private final boolean closeInput;
    private boolean opened;

    private JsonArraySequenceSource(
            InputStreamSupplier supplier,
            Replayability replayability,
            boolean closeInput
    ) {
        this.supplier = supplier;
        this.replayability = replayability;
        this.closeInput = closeInput;
    }

    /**
     * {@code open()} ごとに新しい InputStream を返す Supplier からSourceを作成します。
     * WriterはSupplierから取得したInputStreamを閉じます。
     */
    public static <T> JsonArraySequenceSource<T> from(InputStreamSupplier supplier) {
        return new JsonArraySequenceSource<>(
                Objects.requireNonNull(supplier, "supplier"),
                Replayability.REPLAYABLE,
                true
        );
    }

    static JsonArraySequenceSource<Object> borrowed(InputStream inputStream) {
        return new JsonArraySequenceSource<>(
                () -> Objects.requireNonNull(inputStream, "inputStream"),
                Replayability.ONE_SHOT,
                false
        );
    }

    @Override
    public synchronized CloseableIterator<T> open() throws IOException {
        if (replayability == Replayability.ONE_SHOT && opened) {
            throw new IOException("One-shot JSON sequence has already been opened.");
        }
        opened = true;

        InputStream input = supplier.open();
        if (input == null) {
            throw new IOException("InputStreamSupplier returned null.");
        }
        JsonParser parser = null;
        try {
            parser = new JsonFactory().createParser(input);
            if (!closeInput) {
                parser.disable(JsonParser.Feature.AUTO_CLOSE_SOURCE);
            }
            if (parser.nextToken() != JsonToken.START_ARRAY) {
                if (!closeInput) {
                    throw new IllegalArgumentException(
                            "ストリーミング入力はルート JSON 配列である必要があります。"
                    );
                }
                throw new IOException("ストリーミング入力はルート JSON 配列である必要があります。");
            }
            return new ParserIterator<>(parser);
        } catch (IOException | RuntimeException e) {
            if (parser != null) {
                try {
                    parser.close();
                } catch (IOException closeFailure) {
                    e.addSuppressed(closeFailure);
                }
            } else if (closeInput) {
                try {
                    input.close();
                } catch (IOException closeFailure) {
                    e.addSuppressed(closeFailure);
                }
            }
            throw e;
        }
    }

    @Override
    public Replayability replayability() {
        return replayability;
    }

    private static final class ParserIterator<T> implements CloseableIterator<T> {
        private final JsonParser parser;
        private JsonToken nextToken;
        private boolean prepared;
        private boolean complete;
        private boolean closed;

        private ParserIterator(JsonParser parser) {
            this.parser = parser;
        }

        @Override
        public boolean hasNext() {
            prepare();
            return !complete;
        }

        @Override
        @SuppressWarnings("unchecked")
        public T next() {
            prepare();
            if (complete) {
                throw new NoSuchElementException();
            }
            try {
                T value = (T) JsonValueCodec.read(parser, nextToken);
                prepared = false;
                return value;
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }

        private void prepare() {
            if (prepared || complete) {
                return;
            }
            try {
                nextToken = parser.nextToken();
                prepared = true;
                if (nextToken == JsonToken.END_ARRAY) {
                    complete = true;
                    close();
                } else if (nextToken == null) {
                    throw new IOException("JSON 配列が途中で終了しました。");
                }
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }

        @Override
        public void close() throws IOException {
            if (closed) {
                return;
            }
            closed = true;
            parser.close();
        }
    }
}
