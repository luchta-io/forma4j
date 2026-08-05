package io.luchta.forma4j.writer.engine.strategy.ooxml;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;

final class CellCommandStore implements Closeable {
    private static final int SORT_CHUNK_SIZE = 4096;
    private static final int MERGE_FAN_IN = 64;

    private final Path directory;
    private final Path spool;
    private DataOutputStream output;
    private boolean sealed;

    CellCommandStore(Path directory, int number) throws IOException {
        this.directory = directory;
        this.spool = directory.resolve("sheet-" + number + ".commands");
        this.output = new DataOutputStream(new BufferedOutputStream(Files.newOutputStream(spool)));
    }

    void append(CellCommand command) throws IOException {
        if (sealed) {
            throw new IllegalStateException("セル命令ストアは既に確定されています。");
        }
        command.write(output);
    }

    SortedIterator sortedIterator() throws IOException {
        seal();
        List<Path> chunks = compactChunks(createSortedChunks());
        return new SortedIterator(chunks);
    }

    private void seal() throws IOException {
        if (sealed) {
            return;
        }
        sealed = true;
        output.close();
        output = null;
    }

    private List<Path> createSortedChunks() throws IOException {
        List<Path> chunks = new ArrayList<>();
        try (DataInputStream input = new DataInputStream(
                new BufferedInputStream(Files.newInputStream(spool)))) {
            int chunkNumber = 0;
            while (true) {
                List<CellCommand> commands = new ArrayList<>(SORT_CHUNK_SIZE);
                while (commands.size() < SORT_CHUNK_SIZE) {
                    CellCommand command = CellCommand.read(input);
                    if (command == null) {
                        break;
                    }
                    commands.add(command);
                }
                if (commands.isEmpty()) {
                    break;
                }
                Collections.sort(commands);
                Path chunk = directory.resolve(
                        spool.getFileName().toString() + ".sorted-" + chunkNumber++
                );
                try (DataOutputStream chunkOutput = new DataOutputStream(
                        new BufferedOutputStream(Files.newOutputStream(chunk)))) {
                    for (CellCommand command : commands) {
                        command.write(chunkOutput);
                    }
                }
                chunks.add(chunk);
                if (commands.size() < SORT_CHUNK_SIZE) {
                    break;
                }
            }
        }
        return chunks;
    }

    private List<Path> compactChunks(List<Path> chunks) throws IOException {
        int pass = 0;
        List<Path> current = chunks;
        while (current.size() > MERGE_FAN_IN) {
            List<Path> merged = new ArrayList<>();
            for (int offset = 0; offset < current.size(); offset += MERGE_FAN_IN) {
                int end = Math.min(offset + MERGE_FAN_IN, current.size());
                List<Path> group = new ArrayList<>(current.subList(offset, end));
                Path result = directory.resolve(
                        spool.getFileName().toString()
                                + ".merge-"
                                + pass
                                + "-"
                                + merged.size()
                );
                try (SortedIterator iterator = new SortedIterator(group);
                     DataOutputStream output = new DataOutputStream(
                             new BufferedOutputStream(Files.newOutputStream(result)))) {
                    while (iterator.hasNext()) {
                        iterator.next().write(output);
                    }
                }
                merged.add(result);
            }
            current = merged;
            pass++;
        }
        return current;
    }

    @Override
    public void close() throws IOException {
        if (output != null) {
            output.close();
            output = null;
        }
    }

    static final class SortedIterator implements Iterator<CellCommand>, Closeable {
        private final List<ChunkReader> readers = new ArrayList<>();
        private final PriorityQueue<ChunkReader> queue = new PriorityQueue<>(
                (left, right) -> left.current.compareTo(right.current)
        );
        private CellCommand buffered;
        private CellCommand next;
        private boolean prepared;

        SortedIterator(List<Path> chunks) throws IOException {
            try {
                for (Path chunk : chunks) {
                    ChunkReader reader = new ChunkReader(chunk);
                    readers.add(reader);
                    if (reader.current != null) {
                        queue.add(reader);
                    }
                }
            } catch (IOException e) {
                close();
                throw e;
            }
        }

        @Override
        public boolean hasNext() {
            prepare();
            return next != null;
        }

        @Override
        public CellCommand next() {
            prepare();
            if (next == null) {
                throw new NoSuchElementException();
            }
            CellCommand result = next;
            prepared = false;
            next = null;
            return result;
        }

        private void prepare() {
            if (prepared) {
                return;
            }
            prepared = true;
            CellCommand selected = takeRaw();
            if (selected == null) {
                next = null;
                return;
            }
            while (true) {
                CellCommand candidate = takeRaw();
                if (candidate == null) {
                    buffered = null;
                    break;
                }
                if (!selected.sameAddress(candidate)) {
                    buffered = candidate;
                    break;
                }
                selected = candidate;
            }
            next = selected;
        }

        private CellCommand takeRaw() {
            if (buffered != null) {
                CellCommand result = buffered;
                buffered = null;
                return result;
            }
            ChunkReader reader = queue.poll();
            if (reader == null) {
                return null;
            }
            CellCommand result = reader.current;
            try {
                reader.advance();
            } catch (IOException e) {
                throw new CellCommandReadException(e);
            }
            if (reader.current != null) {
                queue.add(reader);
            }
            return result;
        }

        @Override
        public void close() throws IOException {
            IOException failure = null;
            for (ChunkReader reader : readers) {
                try {
                    reader.close();
                } catch (IOException e) {
                    if (failure == null) {
                        failure = e;
                    } else {
                        failure.addSuppressed(e);
                    }
                }
            }
            if (failure != null) {
                throw failure;
            }
        }
    }

    private static final class ChunkReader implements Closeable {
        private final Path path;
        private final DataInputStream input;
        private CellCommand current;

        private ChunkReader(Path path) throws IOException {
            this.path = path;
            input = new DataInputStream(new BufferedInputStream(Files.newInputStream(path)));
            advance();
        }

        private void advance() throws IOException {
            current = CellCommand.read(input);
        }

        @Override
        public void close() throws IOException {
            try {
                input.close();
            } finally {
                Files.deleteIfExists(path);
            }
        }
    }

    static final class CellCommandReadException extends RuntimeException {
        private CellCommandReadException(IOException cause) {
            super(cause);
        }

        IOException ioCause() {
            return (IOException) getCause();
        }
    }
}
