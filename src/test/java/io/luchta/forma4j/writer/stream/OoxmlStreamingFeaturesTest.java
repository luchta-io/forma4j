package io.luchta.forma4j.writer.stream;

import io.luchta.forma4j.context.databind.json.JsonNode;
import io.luchta.forma4j.context.databind.json.JsonObject;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OoxmlStreamingFeaturesTest {
    @Test
    void writesTypesStylesWidthsFiltersFormulasAndMultipleSheets() throws Exception {
        String definition = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
                + "<forma><sheet name=\"typed\">"
                + "<row rowIndex=\"0\" startColumnIndex=\"0\" autoFilter=\"true\">"
                + "<cell style=\"border:thin;background-color:#FFC000;width:18\">header</cell>"
                + "<cell>value</cell></row>"
                + "<row rowIndex=\"1\" startColumnIndex=\"0\">"
                + "<cell cellType=\"boolean\">#{bool}</cell>"
                + "<cell cellType=\"numeric\">#{number}</cell>"
                + "<cell cellType=\"date\">#{date}</cell>"
                + "<cell cellType=\"datetime\">#{datetime}</cell>"
                + "<cell cellType=\"formula\">#{formula}</cell>"
                + "</row></sheet>"
                + "<sheet name=\"other\"><cell>second sheet</cell></sheet></forma>";

        JsonNode root = new JsonNode();
        root.putVar("bool", new JsonObject(true));
        root.putVar("number", new JsonObject(new BigDecimal("12.5")));
        root.putVar("date", new JsonObject(LocalDate.of(2026, 7, 23)));
        root.putVar("datetime", new JsonObject(LocalDateTime.of(2026, 7, 23, 12, 34, 56)));
        root.putVar("formula", new JsonObject("1+1"));

        byte[] output = write(definition, new JsonObject(root));
        try (XSSFWorkbook workbook = new XSSFWorkbook(new ByteArrayInputStream(output))) {
            Sheet sheet = workbook.getSheet("typed");
            assertEquals(CellType.BOOLEAN, sheet.getRow(1).getCell(0).getCellType());
            assertTrue(sheet.getRow(1).getCell(0).getBooleanCellValue());
            assertEquals(12.5d, sheet.getRow(1).getCell(1).getNumericCellValue());
            assertEquals(
                    LocalDate.of(2026, 7, 23),
                    sheet.getRow(1).getCell(2).getLocalDateTimeCellValue().toLocalDate()
            );
            assertEquals(
                    LocalDateTime.of(2026, 7, 23, 12, 34, 56),
                    sheet.getRow(1).getCell(3).getLocalDateTimeCellValue()
            );
            assertEquals("1+1", sheet.getRow(1).getCell(4).getCellFormula());
            assertEquals(
                    BorderStyle.THIN,
                    sheet.getRow(0).getCell(0).getCellStyle().getBorderBottom()
            );
            assertEquals(
                    FillPatternType.SOLID_FOREGROUND,
                    sheet.getRow(0).getCell(0).getCellStyle().getFillPattern()
            );
            assertEquals(18 * 256, sheet.getColumnWidth(0));
            assertEquals(
                    "A1:B1",
                    ((XSSFSheet) sheet).getCTWorksheet().getAutoFilter().getRef()
            );
            assertEquals(
                    "second sheet",
                    workbook.getSheet("other").getRow(0).getCell(0).getStringCellValue()
            );
            assertTrue(workbook.getForceFormulaRecalculation());
        }
    }

    @Test
    void preservesUnknownTemplatePartsAndExistingStylesWhileAddingStyles() throws Exception {
        byte[] template = styledTemplate();
        byte[] marker = zipEntries(template).get("docProps/app.xml");
        byte[] image = zipEntries(template).get("xl/media/image1.png");
        assertNotNull(marker);
        assertNotNull(image);
        String definition = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
                + "<forma><sheet name=\"result\">"
                + "<cell>updated</cell>"
                + "<cell rowIndex=\"1\" style=\"border:thin;width:22\">appended</cell>"
                + "<cell rowIndex=\"2\" cellType=\"formula\">1+1</cell>"
                + "</sheet></forma>";

        byte[] output = writeTemplate(definition, template, new JsonObject(new JsonNode()));
        assertArrayEquals(marker, zipEntries(output).get("docProps/app.xml"));
        assertArrayEquals(image, zipEntries(output).get("xl/media/image1.png"));
        try (Workbook workbook = WorkbookFactory.create(new ByteArrayInputStream(output))) {
            Sheet sheet = workbook.getSheet("result");
            Cell updated = sheet.getRow(0).getCell(0);
            assertEquals("updated", updated.getStringCellValue());
            assertEquals(IndexedColors.YELLOW.getIndex(), updated.getCellStyle().getFillForegroundColor());
            assertEquals(
                    BorderStyle.THIN,
                    sheet.getRow(1).getCell(0).getCellStyle().getBorderBottom()
            );
            assertEquals(22 * 256, sheet.getColumnWidth(0));
            assertEquals(1, workbook.getAllPictures().size());
            assertEquals("1+1", sheet.getRow(2).getCell(0).getCellFormula());
            assertTrue(workbook.getForceFormulaRecalculation());
        }
    }

    @Test
    void usesThe1904DateWindowingFromTemplate() throws Exception {
        byte[] template;
        try (XSSFWorkbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            workbook.createSheet("result");
            if (!workbook.getCTWorkbook().isSetWorkbookPr()) {
                workbook.getCTWorkbook().addNewWorkbookPr();
            }
            workbook.getCTWorkbook().getWorkbookPr().setDate1904(true);
            workbook.write(output);
            template = output.toByteArray();
        }

        String definition = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
                + "<forma><sheet name=\"result\">"
                + "<cell cellType=\"date\">#{date}</cell>"
                + "</sheet></forma>";
        JsonNode root = new JsonNode();
        root.putVar("date", new JsonObject(LocalDate.of(2026, 7, 23)));

        byte[] output = writeTemplate(definition, template, new JsonObject(root));
        try (Workbook workbook = WorkbookFactory.create(new ByteArrayInputStream(output))) {
            assertEquals(
                    LocalDate.of(2026, 7, 23),
                    workbook.getSheet("result")
                            .getRow(0)
                            .getCell(0)
                            .getLocalDateTimeCellValue()
                            .toLocalDate()
            );
        }
    }

    @Test
    void rejectsDigitallySignedTemplatePackages() throws Exception {
        byte[] signed = addEntry(
                styledTemplate(),
                "_xmlsignatures/sig1.xml",
                "<signature/>".getBytes(StandardCharsets.UTF_8)
        );
        String definition = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
                + "<forma><sheet name=\"result\"><cell>value</cell></sheet></forma>";

        assertThrows(
                IllegalArgumentException.class,
                () -> writeTemplate(definition, signed, new JsonObject(new JsonNode()))
        );
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

    private byte[] writeTemplate(
            String definition,
            byte[] template,
            JsonObject jsonObject
    ) throws Exception {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        new FormaStreamingWriter().write(
                new ByteArrayInputStream(definition.getBytes(StandardCharsets.UTF_8)),
                output,
                new ByteArrayInputStream(template),
                jsonObject
        );
        return output.toByteArray();
    }

    private byte[] styledTemplate() throws Exception {
        try (XSSFWorkbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("result");
            Cell cell = sheet.createRow(0).createCell(0);
            cell.setCellValue("original");
            org.apache.poi.ss.usermodel.CellStyle style = workbook.createCellStyle();
            style.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            cell.setCellStyle(style);
            sheet.setColumnWidth(0, 10 * 256);

            byte[] png = Base64.getDecoder().decode(
                    "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwC"
                            + "AAAAC0lEQVR42mNk+A8AAQUBAScY42YAAAAASUVORK5CYII="
            );
            int pictureIndex = workbook.addPicture(png, Workbook.PICTURE_TYPE_PNG);
            CreationHelper helper = workbook.getCreationHelper();
            Drawing<?> drawing = sheet.createDrawingPatriarch();
            ClientAnchor anchor = helper.createClientAnchor();
            anchor.setCol1(2);
            anchor.setRow1(0);
            Picture picture = drawing.createPicture(anchor, pictureIndex);
            picture.resize();
            workbook.write(output);
            return output.toByteArray();
        }
    }

    private byte[] addEntry(byte[] source, String name, byte[] value) throws Exception {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try (ZipInputStream input = new ZipInputStream(new ByteArrayInputStream(source));
             ZipOutputStream zip = new ZipOutputStream(output)) {
            ZipEntry entry;
            byte[] buffer = new byte[8192];
            while ((entry = input.getNextEntry()) != null) {
                zip.putNextEntry(new ZipEntry(entry.getName()));
                int length;
                while ((length = input.read(buffer)) >= 0) {
                    zip.write(buffer, 0, length);
                }
                zip.closeEntry();
            }
            zip.putNextEntry(new ZipEntry(name));
            zip.write(value);
            zip.closeEntry();
        }
        return output.toByteArray();
    }

    private Map<String, byte[]> zipEntries(byte[] xlsx) throws Exception {
        Map<String, byte[]> entries = new HashMap<>();
        try (ZipInputStream input = new ZipInputStream(new ByteArrayInputStream(xlsx))) {
            ZipEntry entry;
            byte[] buffer = new byte[8192];
            while ((entry = input.getNextEntry()) != null) {
                ByteArrayOutputStream output = new ByteArrayOutputStream();
                int length;
                while ((length = input.read(buffer)) >= 0) {
                    output.write(buffer, 0, length);
                }
                entries.put(entry.getName(), output.toByteArray());
            }
        }
        return entries;
    }
}
