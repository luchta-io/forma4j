package io.luchta.forma4j.writer.stream;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import io.luchta.forma4j.writer.Context;
import io.luchta.forma4j.writer.definition.XmlDocument;
import io.luchta.forma4j.writer.definition.schema.Element;
import io.luchta.forma4j.writer.definition.schema.element.HorizontalFor;
import io.luchta.forma4j.writer.definition.schema.element.ListElement;
import io.luchta.forma4j.writer.definition.schema.element.Sheet;
import io.luchta.forma4j.writer.definition.schema.element.VerticalFor;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Stream;

/**
 * FormaStreamingWriter の1回の実行内でSourceの状態と一時ファイルを管理します。
 */
final class SequenceSourceSession implements AutoCloseable {
    private final Map<SequenceSource<?>, Integer> openCounts = new IdentityHashMap<>();
    private final Map<SequenceSource<?>, Path> spools = new IdentityHashMap<>();
    private Path temporaryDirectory;
    private boolean closed;

    SequenceSourceSession(XmlDocument definition, Context context) {
        validateOneShotReferences(definition, context);
    }

    CloseableIterator<Object> open(String name, Object value) {
        if (!(value instanceof SequenceSource<?>)) {
            if (value instanceof Iterable<?>) {
                return named(name, CloseableIterators.from(((Iterable<?>) value).iterator()));
            }
            return CloseableIterators.empty();
        }

        SequenceSource<?> source = (SequenceSource<?>) value;
        try {
            if (source.replayability() == Replayability.ONE_SHOT) {
                int count = openCounts.getOrDefault(source, 0);
                if (count > 0) {
                    throw oneShotFailure(name);
                }
                openCounts.put(source, count + 1);
                return named(name, source.open());
            }
            if (source.replayability() == Replayability.SPOOLED) {
                return named(name, replaySpool(name, source));
            }
            return named(name, source.open());
        } catch (IOException e) {
            throw new UncheckedIOException(namedFailure(name, e));
        }
    }

    private CloseableIterator<Object> replaySpool(
            String name,
            SequenceSource<?> source
    ) throws IOException {
        Path spool = spools.get(source);
        if (spool == null) {
            spool = createSpool(name, source);
            spools.put(source, spool);
        }
        Path replayFile = spool;
        return JsonArraySequenceSource.from(() -> Files.newInputStream(replayFile)).open();
    }

    private Path createSpool(String name, SequenceSource<?> source) throws IOException {
        Path directory = temporaryDirectory();
        Path spool = Files.createTempFile(directory, "sequence-", ".json");
        boolean success = false;
        try (CloseableIterator<?> iterator = source.open();
             OutputStream output = new BufferedOutputStream(Files.newOutputStream(spool));
             JsonGenerator generator = new JsonFactory().createGenerator(output)) {
            generator.writeStartArray();
            while (iterator.hasNext()) {
                JsonValueCodec.write(generator, iterator.next());
            }
            generator.writeEndArray();
            success = true;
            return spool;
        } catch (UncheckedIOException e) {
            IOException failure = namedFailure(name, e.getCause());
            for (Throwable suppressed : e.getSuppressed()) {
                failure.addSuppressed(suppressed);
            }
            throw failure;
        } catch (IOException e) {
            throw namedFailure(name, e);
        } finally {
            if (!success) {
                Files.deleteIfExists(spool);
            }
        }
    }

    private Path temporaryDirectory() throws IOException {
        if (temporaryDirectory == null) {
            temporaryDirectory = Files.createTempDirectory("forma4j-input-");
        }
        return temporaryDirectory;
    }

    Path temporaryDirectoryForTesting() {
        return temporaryDirectory;
    }

    @SuppressWarnings("unchecked")
    private CloseableIterator<Object> named(
            String name,
            CloseableIterator<?> iterator
    ) {
        CloseableIterator<Object> delegate = (CloseableIterator<Object>) iterator;
        return new CloseableIterator<Object>() {
            @Override
            public boolean hasNext() {
                try {
                    return delegate.hasNext();
                } catch (UncheckedIOException e) {
                    throw new UncheckedIOException(namedFailure(name, e.getCause()));
                }
            }

            @Override
            public Object next() {
                try {
                    return delegate.next();
                } catch (UncheckedIOException e) {
                    throw new UncheckedIOException(namedFailure(name, e.getCause()));
                }
            }

            @Override
            public void remove() {
                delegate.remove();
            }

            @Override
            public void close() throws IOException {
                try {
                    delegate.close();
                } catch (IOException e) {
                    throw namedFailure(name, e);
                }
            }
        };
    }

    private static IOException namedFailure(String name, IOException cause) {
        String message = "failed to read collection '" + name + "'";
        if (message.equals(cause.getMessage())) {
            return cause;
        }
        return new IOException(message, cause);
    }

    private static IllegalStateException oneShotFailure(String name) {
        return new IllegalStateException(
                "collection '" + name + "' is one-shot but is referenced more than once"
        );
    }

    private static void validateOneShotReferences(
            XmlDocument definition,
            Context context
    ) {
        Map<String, Integer> references = new LinkedHashMap<>();
        for (Element element : definition.root().children()) {
            collectReferences(element, context, references);
        }
        for (Map.Entry<String, Integer> reference : references.entrySet()) {
            Object value = context.getVar(reference.getKey());
            if (value instanceof SequenceSource<?>
                    && ((SequenceSource<?>) value).replayability() == Replayability.ONE_SHOT
                    && reference.getValue() > 1) {
                throw oneShotFailure(reference.getKey());
            }
        }
    }

    private static void collectReferences(
            Element element,
            Context context,
            Map<String, Integer> references
    ) {
        String collection = collectionName(element, context);
        if (collection != null && !collection.isEmpty()) {
            references.merge(collection, 1, Integer::sum);
        }
        for (Element child : element.children()) {
            collectReferences(child, context, references);
        }
    }

    private static String collectionName(Element element, Context context) {
        switch (element.type()) {
            case SHEET:
                Sheet sheet = (Sheet) element;
                return sheet.collection().isEmpty() ? null : sheet.collection().toString();
            case VERTICAL_FOR:
                return ((VerticalFor) element).collection().toString();
            case HORIZONTAL_FOR:
                return ((HorizontalFor) element).collection().toString();
            case LIST:
                ListElement list = (ListElement) element;
                if (!list.collection().isEmpty()) {
                    return list.collection().toString();
                }
                return context.getKeys().isEmpty()
                        ? null
                        : context.getKeys().iterator().next();
            default:
                return null;
        }
    }

    @Override
    public void close() throws IOException {
        if (closed) {
            return;
        }
        closed = true;
        if (temporaryDirectory == null) {
            return;
        }
        IOException failure = null;
        try (Stream<Path> paths = Files.walk(temporaryDirectory)) {
            Path[] ordered = paths.sorted(Comparator.reverseOrder()).toArray(Path[]::new);
            for (Path path : ordered) {
                try {
                    Files.deleteIfExists(path);
                } catch (IOException e) {
                    if (failure == null) {
                        failure = e;
                    } else {
                        failure.addSuppressed(e);
                    }
                }
            }
        }
        if (failure != null) {
            throw failure;
        }
    }
}
