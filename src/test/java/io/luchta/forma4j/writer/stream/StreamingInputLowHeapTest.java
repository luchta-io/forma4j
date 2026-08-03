package io.luchta.forma4j.writer.stream;

import io.luchta.forma4j.writer.Context;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayInputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.AbstractList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("low-heap")
class StreamingInputLowHeapTest {
    private static final int ROW_COUNT = 10_000;
    private static final int COLUMN_COUNT = 100;

    @Test
    void writesOneMillionCellsFromLazyRowsWith256MiBHeap(@TempDir Path directory)
            throws Exception {
        String definition = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
                + "<forma><sheet name=\"result\">"
                + "<vertical-for item=\"row\" collection=\"rows\" "
                + "startRowIndex=\"0\" startColumnIndex=\"0\">"
                + "<row><horizontal-for item=\"item\" collection=\"row.items\">"
                + "<cell>#{item}</cell>"
                + "</horizontal-for></row>"
                + "</vertical-for></sheet></forma>";
        Context context = new Context();
        context.putVar("rows", lazyRows());
        Path outputFile = directory.resolve("million-cells.xlsx");

        try (OutputStream output = Files.newOutputStream(outputFile)) {
            new FormaStreamingWriter().write(
                    new ByteArrayInputStream(definition.getBytes(StandardCharsets.UTF_8)),
                    output,
                    context
            );
        }

        assertTrue(Files.size(outputFile) > 0);
    }

    private Iterable<Map<String, Object>> lazyRows() {
        return () -> new Iterator<Map<String, Object>>() {
            private int row;

            @Override
            public boolean hasNext() {
                return row < ROW_COUNT;
            }

            @Override
            public Map<String, Object> next() {
                int current = row++;
                Map<String, Object> value = new LinkedHashMap<>();
                value.put("items", new AbstractList<String>() {
                    @Override
                    public String get(int column) {
                        return current + "-" + column;
                    }

                    @Override
                    public int size() {
                        return COLUMN_COUNT;
                    }
                });
                return value;
            }
        };
    }
}
