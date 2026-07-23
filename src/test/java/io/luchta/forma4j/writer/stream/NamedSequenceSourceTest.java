package io.luchta.forma4j.writer.stream;

import io.luchta.forma4j.writer.Context;
import io.luchta.forma4j.context.databind.json.JsonNode;
import io.luchta.forma4j.context.databind.json.JsonNodes;
import io.luchta.forma4j.context.databind.json.JsonObject;
import io.luchta.forma4j.writer.definition.XmlDocument;
import io.luchta.forma4j.writer.definition.XmlDocumentReader;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class NamedSequenceSourceTest {
    @Test
    void writesNamedSourcesScalarsAndDifferentSheets() throws Exception {
        String definition = forma(
                "<sheet name=\"headers\"><cell>#{title}</cell>"
                        + vertical("header", "headers", 1, "#{header.value}")
                        + "</sheet>"
                        + "<sheet name=\"rows\">"
                        + vertical("row", "rows", 0, "#{row.value}")
                        + "</sheet>"
        );
        Context context = new Context();
        context.putVar("title", "report");
        context.putSequence(
                "headers",
                JsonArraySequenceSource.from(() -> bytes("[{\"value\":\"H1\"},{\"value\":\"H2\"}]"))
        );
        context.putSequence(
                "rows",
                IterableSequenceSource.replayable(Arrays.asList(row("R1"), row("R2"), row("R3")))
        );

        byte[] output = write(definition, context);
        try (Workbook workbook = WorkbookFactory.create(new ByteArrayInputStream(output))) {
            assertEquals("report", workbook.getSheet("headers").getRow(0).getCell(0).getStringCellValue());
            assertEquals("H2", workbook.getSheet("headers").getRow(2).getCell(0).getStringCellValue());
            assertEquals("R3", workbook.getSheet("rows").getRow(2).getCell(0).getStringCellValue());
        }
    }

    @Test
    void writesNamedContextSourcesIntoTemplate() throws Exception {
        String definition = forma(
                "<sheet name=\"result\"><cell>#{title}</cell>"
                        + vertical("row", "rows", 1, "#{row.value}")
                        + "</sheet>"
        );
        Context context = new Context();
        context.putVar("title", "updated");
        context.putVar("rows", Arrays.asList(row("one"), row("two")));

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        new FormaStreamingWriter().write(
                bytes(definition),
                output,
                bytes(template()),
                context
        );

        try (Workbook workbook = WorkbookFactory.create(
                new ByteArrayInputStream(output.toByteArray()))) {
            assertEquals("updated", workbook.getSheet("result").getRow(0).getCell(0).getStringCellValue());
            assertEquals("two", workbook.getSheet("result").getRow(2).getCell(0).getStringCellValue());
        }
    }

    @Test
    void processesNestedJsonArrayWithoutRetainingRootRows() throws Exception {
        String definition = forma(
                "<sheet name=\"result\">"
                        + "<vertical-for item=\"row\" collection=\"rows\" startRowIndex=\"0\" startColumnIndex=\"0\">"
                        + "<row><cell>#{row.name}</cell>"
                        + "<horizontal-for item=\"item\" collection=\"row.items\">"
                        + "<cell>#{item}</cell>"
                        + "</horizontal-for></row>"
                        + "</vertical-for></sheet>"
        );
        Context context = new Context();
        context.putSequence(
                "rows",
                JsonArraySequenceSource.from(() -> bytes(
                        "[{\"name\":\"row1\",\"items\":[\"a\",\"b\"]},"
                                + "{\"name\":\"row2\",\"items\":[\"c\",\"d\"]}]"
                ))
        );

        byte[] output = write(definition, context);
        try (Workbook workbook = WorkbookFactory.create(new ByteArrayInputStream(output))) {
            Sheet sheet = workbook.getSheet("result");
            assertEquals("row1", sheet.getRow(0).getCell(0).getStringCellValue());
            assertEquals("b", sheet.getRow(0).getCell(2).getStringCellValue());
            assertEquals("row2", sheet.getRow(1).getCell(0).getStringCellValue());
            assertEquals("d", sheet.getRow(1).getCell(2).getStringCellValue());
        }
    }

    @Test
    void rejectsStaticOneShotMultipleReferencesBeforeWritingOutput() {
        String definition = forma(
                "<sheet name=\"first\">" + vertical("row", "rows", 0, "#{row.value}") + "</sheet>"
                        + "<sheet name=\"second\">" + vertical("row", "rows", 0, "#{row.value}") + "</sheet>"
        );
        Context context = new Context();
        context.putSequence(
                "rows",
                IterableSequenceSource.oneShot(Arrays.asList(row("value")).iterator())
        );
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        IllegalStateException failure = assertThrows(
                IllegalStateException.class,
                () -> new FormaStreamingWriter().write(bytes(definition), output, context)
        );

        assertEquals(
                "collection 'rows' is one-shot but is referenced more than once",
                failure.getMessage()
        );
        assertEquals(0, output.size());
    }

    @Test
    void rejectsDynamicSecondOpenOfOneShotSource() {
        String definition = forma(
                "<sheet name=\"result\">"
                        + "<vertical-for item=\"outerItem\" collection=\"outer\" startRowIndex=\"0\" startColumnIndex=\"0\">"
                        + "<row><vertical-for item=\"row\" collection=\"rows\" startRowIndex=\"0\" startColumnIndex=\"0\">"
                        + "<row><cell>#{row.value}</cell></row>"
                        + "</vertical-for></row>"
                        + "</vertical-for></sheet>"
        );
        Context context = new Context();
        context.putVar("outer", Arrays.asList(row("first"), row("second")));
        context.putSequence(
                "rows",
                IterableSequenceSource.oneShot(Arrays.asList(row("value")).iterator())
        );

        IllegalStateException failure = assertThrows(
                IllegalStateException.class,
                () -> write(definition, context)
        );
        assertEquals(
                "collection 'rows' is one-shot but is referenced more than once",
                failure.getMessage()
        );
    }

    @Test
    void reopensReplayableSourceAndReplaysSpooledOneShotSource() throws Exception {
        String definition = forma(
                "<sheet name=\"replay1\">" + vertical("row", "replay", 0, "#{row.value}") + "</sheet>"
                        + "<sheet name=\"replay2\">" + vertical("row", "replay", 0, "#{row.value}") + "</sheet>"
                        + "<sheet name=\"spool1\">" + vertical("row", "spool", 0, "#{row.value}") + "</sheet>"
                        + "<sheet name=\"spool2\">" + vertical("row", "spool", 0, "#{row.value}") + "</sheet>"
        );
        Context context = new Context();
        context.putSequence(
                "replay",
                IterableSequenceSource.replayable(Arrays.asList(row("R1"), row("R2")))
        );
        context.putSequence(
                "spool",
                SpooledSequenceSource.from(
                        IterableSequenceSource.oneShot(
                                Arrays.asList(row("S1"), row("S2")).iterator()
                        )
                )
        );

        byte[] output = write(definition, context);
        try (Workbook workbook = WorkbookFactory.create(new ByteArrayInputStream(output))) {
            assertEquals("R2", workbook.getSheet("replay1").getRow(1).getCell(0).getStringCellValue());
            assertEquals("R2", workbook.getSheet("replay2").getRow(1).getCell(0).getStringCellValue());
            assertEquals("S2", workbook.getSheet("spool1").getRow(1).getCell(0).getStringCellValue());
            assertEquals("S2", workbook.getSheet("spool2").getRow(1).getCell(0).getStringCellValue());
        }
    }

    @Test
    void reportsSourceNameAndClosesIteratorAfterReadFailure() {
        String definition = forma(
                "<sheet name=\"result\">" + vertical("row", "employees", 0, "#{row.value}") + "</sheet>"
        );
        AtomicBoolean closed = new AtomicBoolean();
        Context context = new Context();
        context.putSequence("employees", failingSource(closed));

        IOException failure = assertThrows(IOException.class, () -> write(definition, context));

        assertEquals("failed to read collection 'employees'", failure.getMessage());
        assertNotNull(failure.getCause());
        assertTrue(closed.get());
    }

    @Test
    void reportsSourceNameWhenOpenFails() {
        String definition = forma(
                "<sheet name=\"result\">" + vertical("row", "departments", 0, "#{row.value}") + "</sheet>"
        );
        Context context = new Context();
        context.putSequence("departments", new SequenceSource<Object>() {
            @Override
            public CloseableIterator<Object> open() throws IOException {
                throw new IOException("open failure");
            }

            @Override
            public Replayability replayability() {
                return Replayability.REPLAYABLE;
            }
        });

        IOException failure = assertThrows(IOException.class, () -> write(definition, context));

        assertEquals("failed to read collection 'departments'", failure.getMessage());
        assertEquals("open failure", failure.getCause().getMessage());
    }

    @Test
    void reportsSourceNameForMalformedJsonAndClosesItsStream() {
        String definition = forma(
                "<sheet name=\"result\">" + vertical("row", "employees", 0, "#{row.value}") + "</sheet>"
        );
        TrackingInputStream malformed = tracking("[{\"value\":\"broken\"");
        Context context = new Context();
        context.putSequence("employees", JsonArraySequenceSource.from(() -> malformed));

        IOException failure = assertThrows(IOException.class, () -> write(definition, context));

        assertEquals("failed to read collection 'employees'", failure.getMessage());
        assertTrue(malformed.closed);
    }

    @Test
    void reportsSourceNameWhenIteratorCloseFails() {
        String definition = forma(
                "<sheet name=\"result\">" + vertical("row", "rows", 0, "#{row.value}") + "</sheet>"
        );
        Context context = new Context();
        context.putSequence("rows", closeFailingSource());

        IOException failure = assertThrows(IOException.class, () -> write(definition, context));

        assertEquals("failed to read collection 'rows'", failure.getMessage());
        assertEquals("close failure", failure.getCause().getMessage());
    }

    @Test
    void closesAutoCloseableIteratorFromDirectIterable() throws Exception {
        String definition = forma(
                "<sheet name=\"result\">" + vertical("row", "rows", 0, "#{row.value}") + "</sheet>"
        );
        AtomicBoolean closed = new AtomicBoolean();
        Context context = new Context();
        context.putVar("rows", closeTrackingIterable(closed));

        write(definition, context);

        assertTrue(closed.get());
    }

    @Test
    void producesEquivalentCellsFromJsonObjectAndContext() throws Exception {
        String definition = forma(
                "<sheet name=\"result\"><cell>#{title}</cell>"
                        + vertical("row", "rows", 1, "#{row.value}")
                        + "</sheet>"
        );
        JsonNode root = new JsonNode();
        root.putVar("title", new JsonObject("report"));
        JsonNodes jsonRows = new JsonNodes();
        jsonRows.add(jsonRow("one"));
        jsonRows.add(jsonRow("two"));
        root.putVar("rows", new JsonObject(jsonRows));

        Context context = new Context();
        context.putVar("title", "report");
        context.putVar("rows", Arrays.asList(row("one"), row("two")));

        ByteArrayOutputStream jsonOutput = new ByteArrayOutputStream();
        new FormaStreamingWriter().write(
                bytes(definition),
                jsonOutput,
                new JsonObject(root)
        );
        byte[] contextOutput = write(definition, context);

        try (Workbook jsonWorkbook = WorkbookFactory.create(
                new ByteArrayInputStream(jsonOutput.toByteArray()));
             Workbook contextWorkbook = WorkbookFactory.create(
                     new ByteArrayInputStream(contextOutput))) {
            Sheet jsonSheet = jsonWorkbook.getSheet("result");
            Sheet contextSheet = contextWorkbook.getSheet("result");
            for (int row = 0; row <= 2; row++) {
                assertEquals(
                        jsonSheet.getRow(row).getCell(0).getCellType(),
                        contextSheet.getRow(row).getCell(0).getCellType()
                );
                assertEquals(
                        jsonSheet.getRow(row).getCell(0).getStringCellValue(),
                        contextSheet.getRow(row).getCell(0).getStringCellValue()
                );
                assertEquals(
                        jsonSheet.getRow(row).getCell(0).getCellStyle().getIndex(),
                        contextSheet.getRow(row).getCell(0).getCellStyle().getIndex()
                );
            }
        }
    }

    @Test
    void leavesBorrowedStreamsOpenAndClosesSupplierStreams() throws Exception {
        TrackingInputStream definition = tracking(forma(
                "<sheet name=\"result\">" + vertical("row", "list", 0, "#{row.value}") + "</sheet>"
        ));
        TrackingInputStream borrowedJson = tracking("[{\"value\":\"ok\"}]");
        TrackingOutputStream output = new TrackingOutputStream();

        new FormaStreamingWriter().write(definition, output, borrowedJson);

        assertFalse(definition.closed);
        assertFalse(borrowedJson.closed);
        assertFalse(output.closed);

        TrackingInputStream suppliedJson = tracking("[{\"value\":\"ok\"}]");
        Context context = new Context();
        context.putSequence("rows", JsonArraySequenceSource.from(() -> suppliedJson));
        write(
                forma("<sheet name=\"result\">" + vertical("row", "rows", 0, "#{row.value}") + "</sheet>"),
                context
        );
        assertTrue(suppliedJson.closed);

        TrackingInputStream template = new TrackingInputStream(template());
        Context templateContext = new Context();
        templateContext.putVar("title", "updated");
        new FormaStreamingWriter().write(
                tracking(forma("<sheet name=\"result\"><cell>#{title}</cell></sheet>")),
                new TrackingOutputStream(),
                template,
                templateContext
        );
        assertFalse(template.closed);
    }

    @Test
    void deletesSpooledInputDirectoryWhenSessionCloses() throws Exception {
        String definitionXml = forma(
                "<sheet name=\"result\">" + vertical("row", "rows", 0, "#{row.value}") + "</sheet>"
        );
        XmlDocument definition = new XmlDocumentReader().read(bytes(definitionXml));
        Context context = new Context();
        context.putSequence(
                "rows",
                SpooledSequenceSource.from(
                        IterableSequenceSource.oneShot(Arrays.asList(row("value")).iterator())
                )
        );

        Path temporaryDirectory;
        try (SequenceSourceSession session = new SequenceSourceSession(definition, context);
             CloseableIterator<Object> iterator = session.open("rows", context.getVar("rows"))) {
            assertTrue(iterator.hasNext());
            temporaryDirectory = session.temporaryDirectoryForTesting();
            assertNotNull(temporaryDirectory);
            assertTrue(java.nio.file.Files.exists(temporaryDirectory));
        }
        assertFalse(java.nio.file.Files.exists(temporaryDirectory));
    }

    @Test
    void deletesSpooledInputDirectoryAfterSpoolingFailure() throws Exception {
        String definitionXml = forma(
                "<sheet name=\"result\">" + vertical("row", "rows", 0, "#{row.value}") + "</sheet>"
        );
        XmlDocument definition = new XmlDocumentReader().read(bytes(definitionXml));
        Context context = new Context();
        context.putSequence(
                "rows",
                SpooledSequenceSource.from(
                        IterableSequenceSource.oneShot(
                                Arrays.<Object>asList(new Object()).iterator()
                        )
                )
        );

        Path temporaryDirectory;
        try (SequenceSourceSession session = new SequenceSourceSession(definition, context)) {
            UncheckedIOException failure = assertThrows(
                    UncheckedIOException.class,
                    () -> session.open("rows", context.getVar("rows"))
            );
            assertEquals("failed to read collection 'rows'", failure.getCause().getMessage());
            temporaryDirectory = session.temporaryDirectoryForTesting();
            assertTrue(java.nio.file.Files.exists(temporaryDirectory));
        }
        assertFalse(java.nio.file.Files.exists(temporaryDirectory));
    }

    private SequenceSource<Object> failingSource(AtomicBoolean closed) {
        return new SequenceSource<Object>() {
            @Override
            public CloseableIterator<Object> open() {
                return new CloseableIterator<Object>() {
                    @Override
                    public boolean hasNext() {
                        return true;
                    }

                    @Override
                    public Object next() {
                        throw new UncheckedIOException(new IOException("source failure"));
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
    }

    private SequenceSource<Object> closeFailingSource() {
        return new SequenceSource<Object>() {
            @Override
            public CloseableIterator<Object> open() {
                Iterator<Object> values = Arrays.<Object>asList(row("value")).iterator();
                return new CloseableIterator<Object>() {
                    @Override
                    public boolean hasNext() {
                        return values.hasNext();
                    }

                    @Override
                    public Object next() {
                        return values.next();
                    }

                    @Override
                    public void close() throws IOException {
                        throw new IOException("close failure");
                    }
                };
            }

            @Override
            public Replayability replayability() {
                return Replayability.REPLAYABLE;
            }
        };
    }

    private Iterable<Object> closeTrackingIterable(AtomicBoolean closed) {
        return () -> new CloseTrackingIterator(
                Arrays.<Object>asList(row("value")).iterator(),
                closed
        );
    }

    private byte[] write(String definition, Context context) throws Exception {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        new FormaStreamingWriter().write(bytes(definition), output, context);
        return output.toByteArray();
    }

    private String forma(String sheets) {
        return "<?xml version=\"1.0\" encoding=\"utf-8\"?><forma>" + sheets + "</forma>";
    }

    private String vertical(
            String item,
            String collection,
            int startRow,
            String cell
    ) {
        return "<vertical-for item=\"" + item + "\" collection=\"" + collection
                + "\" startRowIndex=\"" + startRow + "\" startColumnIndex=\"0\">"
                + "<row><cell>" + cell + "</cell></row>"
                + "</vertical-for>";
    }

    private Map<String, Object> row(String value) {
        Map<String, Object> row = new LinkedHashMap<>();
        row.put("value", value);
        return row;
    }

    private JsonNode jsonRow(String value) {
        JsonNode row = new JsonNode();
        row.putVar("value", new JsonObject(value));
        return row;
    }

    private InputStream bytes(String value) {
        return new ByteArrayInputStream(value.getBytes(StandardCharsets.UTF_8));
    }

    private InputStream bytes(byte[] value) {
        return new ByteArrayInputStream(value);
    }

    private byte[] template() throws Exception {
        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            workbook.createSheet("result").createRow(0).createCell(0).setCellValue("old");
            workbook.write(output);
            return output.toByteArray();
        }
    }

    private TrackingInputStream tracking(String value) {
        return new TrackingInputStream(value.getBytes(StandardCharsets.UTF_8));
    }

    private static final class TrackingInputStream extends ByteArrayInputStream {
        private boolean closed;

        private TrackingInputStream(byte[] value) {
            super(value);
        }

        @Override
        public void close() throws IOException {
            closed = true;
            super.close();
        }
    }

    private static final class TrackingOutputStream extends ByteArrayOutputStream {
        private boolean closed;

        @Override
        public void close() throws IOException {
            closed = true;
            super.close();
        }
    }

    private static final class CloseTrackingIterator
            implements Iterator<Object>, AutoCloseable {
        private final Iterator<Object> delegate;
        private final AtomicBoolean closed;

        private CloseTrackingIterator(
                Iterator<Object> delegate,
                AtomicBoolean closed
        ) {
            this.delegate = delegate;
            this.closed = closed;
        }

        @Override
        public boolean hasNext() {
            return delegate.hasNext();
        }

        @Override
        public Object next() {
            return delegate.next();
        }

        @Override
        public void close() {
            closed.set(true);
        }
    }
}
