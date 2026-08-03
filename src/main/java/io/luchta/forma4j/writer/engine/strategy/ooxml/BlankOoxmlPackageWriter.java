package io.luchta.forma4j.writer.engine.strategy.ooxml;

import org.apache.poi.ss.util.WorkbookUtil;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

final class BlankOoxmlPackageWriter {
    private static final String RELATIONSHIPS_NAMESPACE =
            "http://schemas.openxmlformats.org/package/2006/relationships";
    private static final String OFFICE_RELATIONSHIPS_NAMESPACE =
            "http://schemas.openxmlformats.org/officeDocument/2006/relationships";
    private static final String CONTENT_TYPES_NAMESPACE =
            "http://schemas.openxmlformats.org/package/2006/content-types";

    private final WorksheetXmlWriter worksheetWriter = new WorksheetXmlWriter();
    private final XMLOutputFactory xmlOutputFactory = XMLOutputFactory.newFactory();

    void write(
            OutputStream output,
            Map<String, SheetState> sheets,
            OoxmlStyleRegistry.PreparedStyles styles,
            boolean formulas
    ) throws IOException {
        for (String sheetName : sheets.keySet()) {
            WorkbookUtil.validateSheetName(sheetName);
        }
        ZipOutputStream zip = new ZipOutputStream(output);
        writeContentTypes(zip, sheets.size());
        writeRootRelationships(zip);
        writeWorkbook(zip, sheets, formulas);
        writeWorkbookRelationships(zip, sheets.size());
        writeStyles(zip, styles);

        int index = 1;
        for (SheetState sheet : sheets.values()) {
            putEntry(zip, "xl/worksheets/sheet" + index++ + ".xml");
            worksheetWriter.writeBlank(zip, sheet, styles, false);
            zip.closeEntry();
        }
        zip.finish();
        zip.flush();
    }

    private void writeContentTypes(ZipOutputStream zip, int sheetCount) throws IOException {
        putEntry(zip, "[Content_Types].xml");
        writeXml(zip, writer -> {
            writer.writeStartElement("Types");
            writer.writeDefaultNamespace(CONTENT_TYPES_NAMESPACE);
            empty(writer, "Default", "Extension", "rels", "ContentType",
                    "application/vnd.openxmlformats-package.relationships+xml");
            empty(writer, "Default", "Extension", "xml", "ContentType", "application/xml");
            empty(writer, "Override", "PartName", "/xl/workbook.xml", "ContentType",
                    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet.main+xml");
            empty(writer, "Override", "PartName", "/xl/styles.xml", "ContentType",
                    "application/vnd.openxmlformats-officedocument.spreadsheetml.styles+xml");
            for (int index = 1; index <= sheetCount; index++) {
                empty(writer, "Override",
                        "PartName", "/xl/worksheets/sheet" + index + ".xml",
                        "ContentType",
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.worksheet+xml");
            }
            writer.writeEndElement();
        });
        zip.closeEntry();
    }

    private void writeRootRelationships(ZipOutputStream zip) throws IOException {
        putEntry(zip, "_rels/.rels");
        writeXml(zip, writer -> {
            writer.writeStartElement("Relationships");
            writer.writeDefaultNamespace(RELATIONSHIPS_NAMESPACE);
            empty(writer, "Relationship",
                    "Id", "rId1",
                    "Type", OFFICE_RELATIONSHIPS_NAMESPACE + "/officeDocument",
                    "Target", "xl/workbook.xml");
            writer.writeEndElement();
        });
        zip.closeEntry();
    }

    private void writeWorkbook(
            ZipOutputStream zip,
            Map<String, SheetState> sheets,
            boolean formulas
    ) throws IOException {
        putEntry(zip, "xl/workbook.xml");
        writeXml(zip, writer -> {
            writer.writeStartElement("workbook");
            writer.writeDefaultNamespace(WorksheetXmlWriter.SPREADSHEET_NAMESPACE);
            writer.writeNamespace("r", OFFICE_RELATIONSHIPS_NAMESPACE);
            writer.writeStartElement("sheets");
            int index = 1;
            for (String name : sheets.keySet()) {
                writer.writeEmptyElement("sheet");
                writer.writeAttribute("name", name);
                writer.writeAttribute("sheetId", Integer.toString(index));
                writer.writeAttribute(
                        "r",
                        OFFICE_RELATIONSHIPS_NAMESPACE,
                        "id",
                        "rId" + (index + 1)
                );
                index++;
            }
            writer.writeEndElement();
            if (formulas) {
                writer.writeEmptyElement("calcPr");
                writer.writeAttribute("calcMode", "auto");
                writer.writeAttribute("fullCalcOnLoad", "1");
                writer.writeAttribute("forceFullCalc", "1");
            }
            writer.writeEndElement();
        });
        zip.closeEntry();
    }

    private void writeWorkbookRelationships(ZipOutputStream zip, int sheetCount)
            throws IOException {
        putEntry(zip, "xl/_rels/workbook.xml.rels");
        writeXml(zip, writer -> {
            writer.writeStartElement("Relationships");
            writer.writeDefaultNamespace(RELATIONSHIPS_NAMESPACE);
            empty(writer, "Relationship",
                    "Id", "rId1",
                    "Type", OFFICE_RELATIONSHIPS_NAMESPACE + "/styles",
                    "Target", "styles.xml");
            for (int index = 1; index <= sheetCount; index++) {
                empty(writer, "Relationship",
                        "Id", "rId" + (index + 1),
                        "Type", OFFICE_RELATIONSHIPS_NAMESPACE + "/worksheet",
                        "Target", "worksheets/sheet" + index + ".xml");
            }
            writer.writeEndElement();
        });
        zip.closeEntry();
    }

    private void writeStyles(
            ZipOutputStream zip,
            OoxmlStyleRegistry.PreparedStyles styles
    ) throws IOException {
        putEntry(zip, "xl/styles.xml");
        styles.write(zip);
        zip.closeEntry();
    }

    private void writeXml(OutputStream output, XmlBody body) throws IOException {
        try {
            XMLStreamWriter writer = xmlOutputFactory.createXMLStreamWriter(output, "UTF-8");
            writer.writeStartDocument("UTF-8", "1.0");
            body.write(writer);
            writer.writeEndDocument();
            writer.flush();
        } catch (XMLStreamException e) {
            throw new IOException("OOXMLパーツの生成に失敗しました。", e);
        }
    }

    private void empty(XMLStreamWriter writer, String element, String... attributes)
            throws XMLStreamException {
        writer.writeEmptyElement(element);
        for (int index = 0; index < attributes.length; index += 2) {
            writer.writeAttribute(attributes[index], attributes[index + 1]);
        }
    }

    private void putEntry(ZipOutputStream zip, String name) throws IOException {
        zip.putNextEntry(new ZipEntry(name));
    }

    @FunctionalInterface
    private interface XmlBody {
        void write(XMLStreamWriter writer) throws XMLStreamException;
    }
}
