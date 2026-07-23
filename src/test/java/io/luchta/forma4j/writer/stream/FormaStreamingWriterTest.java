package io.luchta.forma4j.writer.stream;

import io.luchta.forma4j.context.databind.json.JsonNode;
import io.luchta.forma4j.context.databind.json.JsonNodes;
import io.luchta.forma4j.context.databind.json.JsonObject;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FormaStreamingWriterTest {
    @Test
    void writesListHeaderAndDetailsFromOnePassJsonArray() throws Exception {
        String definition = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
                + "<forma><sheet name=\"result\"><list collection=\"list\"/></sheet></forma>";
        byte[] output = write(definition, "[{\"name\":\"first\",\"count\":1},{\"name\":\"second\",\"count\":2}]");

        try (Workbook workbook = WorkbookFactory.create(new ByteArrayInputStream(output))) {
            Sheet sheet = workbook.getSheet("result");
            assertEquals("name", sheet.getRow(0).getCell(0).getStringCellValue());
            assertEquals("count", sheet.getRow(0).getCell(1).getStringCellValue());
            assertEquals("first", sheet.getRow(1).getCell(0).getStringCellValue());
            assertEquals("2", sheet.getRow(2).getCell(1).getStringCellValue());
        }
    }

    @Test
    void writesMoreThanTheStreamingWindowAndIgnoresAutoSizeColumn() throws Exception {
        String definition = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
                + "<forma><sheet name=\"result\" autoSizeColumn=\"true\">"
                + "<vertical-for item=\"item\" collection=\"list\" startRowIndex=\"0\" startColumnIndex=\"0\">"
                + "<row><cell style=\"width:18\">#{item.value}</cell></row>"
                + "</vertical-for></sheet></forma>";
        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < 150; i++) {
            if (i > 0) {
                json.append(',');
            }
            json.append("{\"value\":\"").append(i).append("\"}");
        }
        json.append(']');

        byte[] output = write(definition, json.toString());
        try (Workbook workbook = WorkbookFactory.create(new ByteArrayInputStream(output))) {
            Sheet sheet = workbook.getSheet("result");
            assertEquals("0", sheet.getRow(0).getCell(0).getStringCellValue());
            assertEquals("149", sheet.getRow(149).getCell(0).getStringCellValue());
            assertEquals(18 * 256, sheet.getColumnWidth(0));
        }
    }

    @Test
    void writesExistingJsonObjectWithScalarsAndCollections() throws Exception {
        String definition = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
                + "<forma><sheet name=\"result\">"
                + "<cell>#{title}</cell>"
                + "<vertical-for item=\"item\" collection=\"items\" startRowIndex=\"1\" startColumnIndex=\"0\">"
                + "<row><cell>#{item.value}</cell></row>"
                + "</vertical-for></sheet></forma>";
        JsonNode root = new JsonNode();
        root.putVar("title", new JsonObject("report"));
        root.putVar("items", new JsonObject(items(150)));
        root.putVar("unused", new JsonObject(items(1)));

        byte[] output = write(definition, new JsonObject(root));
        try (Workbook workbook = WorkbookFactory.create(new ByteArrayInputStream(output))) {
            Sheet sheet = workbook.getSheet("result");
            assertEquals("report", sheet.getRow(0).getCell(0).getStringCellValue());
            assertEquals("0", sheet.getRow(1).getCell(0).getStringCellValue());
            assertEquals("149", sheet.getRow(150).getCell(0).getStringCellValue());
        }
    }

    @Test
    void rejectsNonObjectJsonObjectForJsonObjectOverload() {
        String definition = "<?xml version=\"1.0\" encoding=\"utf-8\"?><forma/>";
        assertThrows(IllegalArgumentException.class, () -> write(definition, new JsonObject("not-an-object")));
    }

    @Test
    void appendsRootJsonArrayAfterTemplateRows() throws Exception {
        String definition = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
                + "<forma><sheet name=\"result\">"
                + "<cell>updated header</cell>"
                + "<vertical-for item=\"item\" collection=\"list\" startRowIndex=\"1\" startColumnIndex=\"0\">"
                + "<row><cell>#{item.value}</cell></row>"
                + "</vertical-for></sheet></forma>";
        String json = jsonValues(150);

        byte[] output = writeTemplate(definition, template(), json);
        try (Workbook workbook = WorkbookFactory.create(new ByteArrayInputStream(output))) {
            Sheet sheet = workbook.getSheet("result");
            assertEquals("updated header", sheet.getRow(0).getCell(0).getStringCellValue());
            assertEquals("0", sheet.getRow(1).getCell(0).getStringCellValue());
            assertEquals("149", sheet.getRow(150).getCell(0).getStringCellValue());
        }
    }

    @Test
    void createsNewSheetWhenWritingJsonObjectWithTemplate() throws Exception {
        String definition = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
                + "<forma><sheet name=\"result\"><cell>#{title}</cell></sheet>"
                + "<sheet name=\"appended\">"
                + "<vertical-for item=\"item\" collection=\"items\" startRowIndex=\"0\" startColumnIndex=\"0\">"
                + "<row><cell>#{item.value}</cell></row>"
                + "</vertical-for></sheet></forma>";
        JsonNode root = new JsonNode();
        root.putVar("title", new JsonObject("updated header"));
        root.putVar("items", new JsonObject(items(2)));

        byte[] output = writeTemplate(definition, template(), new JsonObject(root));
        try (Workbook workbook = WorkbookFactory.create(new ByteArrayInputStream(output))) {
            assertEquals("updated header", workbook.getSheet("result").getRow(0).getCell(0).getStringCellValue());
            assertEquals("0", workbook.getSheet("appended").getRow(0).getCell(0).getStringCellValue());
            assertEquals("1", workbook.getSheet("appended").getRow(1).getCell(0).getStringCellValue());
        }
    }

    @Test
    void updatesExistingTemplateRowsAfterAppendCommands() throws Exception {
        String definition = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
                + "<forma><sheet name=\"result\">"
                + "<vertical-for item=\"item\" collection=\"list\" startRowIndex=\"1\" startColumnIndex=\"0\">"
                + "<row><cell>#{item.value}</cell></row>"
                + "</vertical-for><cell rowIndex=\"0\">replacement</cell>"
                + "</sheet></forma>";
        byte[] output = writeTemplate(
                definition,
                template(),
                jsonValues(1)
        );
        try (Workbook workbook = WorkbookFactory.create(new ByteArrayInputStream(output))) {
            Sheet sheet = workbook.getSheet("result");
            assertEquals("replacement", sheet.getRow(0).getCell(0).getStringCellValue());
            assertEquals("0", sheet.getRow(1).getCell(0).getStringCellValue());
        }
    }

    @Test
    void updatesTemplateRowsWithoutPromotingToStreaming() throws Exception {
        String definition = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
                + "<forma><sheet name=\"result\"><cell>updated only</cell></sheet></forma>";

        byte[] output = writeTemplate(definition, template(), new JsonObject(new JsonNode()));
        try (Workbook workbook = WorkbookFactory.create(new ByteArrayInputStream(output))) {
            assertEquals("updated only", workbook.getSheet("result").getRow(0).getCell(0).getStringCellValue());
        }
    }

    private byte[] write(String definition, String json) throws Exception {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        new FormaStreamingWriter().write(
                new ByteArrayInputStream(definition.getBytes(StandardCharsets.UTF_8)),
                output,
                new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8))
        );
        return output.toByteArray();
    }

    private byte[] write(String definition, JsonObject jsonObject) throws Exception {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        new FormaStreamingWriter().write(
                new ByteArrayInputStream(definition.getBytes(StandardCharsets.UTF_8)),
                output,
                jsonObject
        );
        return output.toByteArray();
    }

    private byte[] writeTemplate(String definition, byte[] template, String json) throws Exception {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        new FormaStreamingWriter().write(
                new ByteArrayInputStream(definition.getBytes(StandardCharsets.UTF_8)),
                output,
                new ByteArrayInputStream(template),
                new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8))
        );
        return output.toByteArray();
    }

    private byte[] writeTemplate(String definition, byte[] template, JsonObject jsonObject) throws Exception {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        new FormaStreamingWriter().write(
                new ByteArrayInputStream(definition.getBytes(StandardCharsets.UTF_8)),
                output,
                new ByteArrayInputStream(template),
                jsonObject
        );
        return output.toByteArray();
    }

    private byte[] template() throws Exception {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("result");
            sheet.createRow(0).createCell(0).setCellValue("header");
            workbook.write(output);
            return output.toByteArray();
        }
    }

    private JsonNodes items(int count) {
        JsonNodes items = new JsonNodes();
        for (int i = 0; i < count; i++) {
            JsonNode item = new JsonNode();
            item.putVar("value", new JsonObject(String.valueOf(i)));
            items.add(item);
        }
        return items;
    }

    private String jsonValues(int count) {
        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < count; i++) {
            if (i > 0) {
                json.append(',');
            }
            json.append("{\"value\":\"").append(i).append("\"}");
        }
        return json.append(']').toString();
    }
}
