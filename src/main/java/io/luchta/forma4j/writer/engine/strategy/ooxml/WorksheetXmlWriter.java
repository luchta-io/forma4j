package io.luchta.forma4j.writer.engine.strategy.ooxml;

import org.apache.poi.ss.usermodel.DateUtil;

import javax.xml.XMLConstants;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

final class WorksheetXmlWriter {
    static final String SPREADSHEET_NAMESPACE =
            "http://schemas.openxmlformats.org/spreadsheetml/2006/main";

    private final XMLInputFactory inputFactory;
    private final XMLOutputFactory outputFactory = XMLOutputFactory.newFactory();

    WorksheetXmlWriter() {
        inputFactory = XMLInputFactory.newFactory();
        setIfSupported(XMLInputFactory.SUPPORT_DTD, false);
        setIfSupported("javax.xml.stream.isSupportingExternalEntities", false);
    }

    void writeBlank(
            OutputStream output,
            SheetState sheet,
            OoxmlStyleRegistry.PreparedStyles styles,
            boolean date1904
    ) throws IOException {
        try (CellCommandStore.SortedIterator commands = sheet.commands.sortedIterator()) {
            XMLStreamWriter writer = outputFactory.createXMLStreamWriter(output, "UTF-8");
            writer.writeStartDocument("UTF-8", "1.0");
            writer.writeStartElement("worksheet");
            writer.writeDefaultNamespace(SPREADSHEET_NAMESPACE);
            writeColumns(writer, sheet.columnWidths);
            writer.writeStartElement("sheetData");
            CommandCursor cursor = new CommandCursor(commands);
            while (cursor.has()) {
                writeCommandRow(writer, cursor, styles, date1904);
            }
            writer.writeEndElement();
            writeAutoFilter(writer, sheet.autoFilterReference());
            writer.writeEndElement();
            writer.writeEndDocument();
            writer.flush();
        } catch (XMLStreamException e) {
            throw new IOException("ワークシートXMLの生成に失敗しました。", e);
        }
    }

    void transform(
            InputStream input,
            OutputStream output,
            SheetState sheet,
            OoxmlStyleRegistry.PreparedStyles styles,
            boolean date1904
    ) throws IOException {
        try (CellCommandStore.SortedIterator commands = sheet.commands.sortedIterator()) {
            XMLStreamReader reader = inputFactory.createXMLStreamReader(input);
            XMLStreamWriter writer = outputFactory.createXMLStreamWriter(output, "UTF-8");
            CommandCursor cursor = new CommandCursor(commands);
            boolean columnsWritten = false;
            boolean filterWritten = false;
            String filterReference = sheet.autoFilterReference();

            writer.writeStartDocument("UTF-8", "1.0");
            while (reader.hasNext()) {
                int event = reader.next();
                if (event == XMLStreamConstants.START_DOCUMENT
                        || event == XMLStreamConstants.END_DOCUMENT) {
                    continue;
                }
                if (event == XMLStreamConstants.START_ELEMENT) {
                    String localName = reader.getLocalName();
                    if ("dimension".equals(localName)) {
                        skipElement(reader);
                        continue;
                    }
                    if ("cols".equals(localName)) {
                        copyColumns(reader, writer, sheet.columnWidths);
                        columnsWritten = true;
                        continue;
                    }
                    if ("sheetData".equals(localName)) {
                        if (!columnsWritten && !sheet.columnWidths.isEmpty()) {
                            writeColumns(writer, sheet.columnWidths);
                            columnsWritten = true;
                        }
                        copyStartElement(reader, writer);
                        transformSheetData(reader, writer, cursor, styles, date1904);
                        continue;
                    }
                    if ("autoFilter".equals(localName) && filterReference != null) {
                        skipElement(reader);
                        writeAutoFilter(writer, filterReference);
                        filterWritten = true;
                        continue;
                    }
                    if (filterReference != null
                            && !filterWritten
                            && isAfterAutoFilter(localName)) {
                        writeAutoFilter(writer, filterReference);
                        filterWritten = true;
                    }
                    copyStartElement(reader, writer);
                } else if (event == XMLStreamConstants.END_ELEMENT) {
                    if ("worksheet".equals(reader.getLocalName())
                            && filterReference != null
                            && !filterWritten) {
                        writeAutoFilter(writer, filterReference);
                        filterWritten = true;
                    }
                    writer.writeEndElement();
                } else {
                    copyNonElementEvent(reader, writer, event);
                }
            }
            writer.writeEndDocument();
            writer.flush();
            reader.close();
        } catch (XMLStreamException e) {
            throw new IOException("テンプレートのワークシートXML変換に失敗しました。", e);
        }
    }

    private void transformSheetData(
            XMLStreamReader reader,
            XMLStreamWriter writer,
            CommandCursor cursor,
            OoxmlStyleRegistry.PreparedStyles styles,
            boolean date1904
    ) throws XMLStreamException {
        int inferredRow = -1;
        while (reader.hasNext()) {
            int event = reader.next();
            if (event == XMLStreamConstants.START_ELEMENT
                    && "row".equals(reader.getLocalName())) {
                String rowAttribute = reader.getAttributeValue(null, "r");
                int row = rowAttribute == null
                        ? inferredRow + 1
                        : Integer.parseInt(rowAttribute) - 1;
                inferredRow = row;
                while (cursor.has() && cursor.current().row < row) {
                    writeCommandRow(writer, cursor, styles, date1904);
                }
                transformRow(reader, writer, row, cursor, styles, date1904);
                continue;
            }
            if (event == XMLStreamConstants.END_ELEMENT
                    && "sheetData".equals(reader.getLocalName())) {
                while (cursor.has()) {
                    writeCommandRow(writer, cursor, styles, date1904);
                }
                writer.writeEndElement();
                return;
            }
            if (event == XMLStreamConstants.START_ELEMENT) {
                copyElement(reader, writer);
            } else {
                copyNonElementEvent(reader, writer, event);
            }
        }
    }

    private void transformRow(
            XMLStreamReader reader,
            XMLStreamWriter writer,
            int row,
            CommandCursor cursor,
            OoxmlStyleRegistry.PreparedStyles styles,
            boolean date1904
    ) throws XMLStreamException {
        copyStartElement(reader, writer);
        int inferredColumn = -1;
        boolean pendingCellsWritten = false;
        while (reader.hasNext()) {
            int event = reader.next();
            if (event == XMLStreamConstants.START_ELEMENT
                    && "c".equals(reader.getLocalName())) {
                String reference = reader.getAttributeValue(null, "r");
                int column = reference == null
                        ? inferredColumn + 1
                        : CellReferences.columnIndex(reference);
                inferredColumn = column;
                while (cursor.isAtRow(row) && cursor.current().column < column) {
                    writeCell(writer, cursor.take(), null, styles, date1904);
                }
                if (cursor.isAt(row, column)) {
                    String existingStyle = reader.getAttributeValue(null, "s");
                    skipElement(reader);
                    writeCell(writer, cursor.take(), existingStyle, styles, date1904);
                } else {
                    copyElement(reader, writer);
                }
                continue;
            }
            if (event == XMLStreamConstants.END_ELEMENT
                    && "row".equals(reader.getLocalName())) {
                while (cursor.isAtRow(row)) {
                    writeCell(writer, cursor.take(), null, styles, date1904);
                }
                writer.writeEndElement();
                return;
            }
            if (event == XMLStreamConstants.START_ELEMENT) {
                if (!pendingCellsWritten) {
                    while (cursor.isAtRow(row)) {
                        writeCell(writer, cursor.take(), null, styles, date1904);
                    }
                    pendingCellsWritten = true;
                }
                copyElement(reader, writer);
            } else {
                copyNonElementEvent(reader, writer, event);
            }
        }
    }

    private void writeCommandRow(
            XMLStreamWriter writer,
            CommandCursor cursor,
            OoxmlStyleRegistry.PreparedStyles styles,
            boolean date1904
    ) throws XMLStreamException {
        int row = cursor.current().row;
        writer.writeStartElement("row");
        writer.writeAttribute("r", Integer.toString(row + 1));
        while (cursor.isAtRow(row)) {
            writeCell(writer, cursor.take(), null, styles, date1904);
        }
        writer.writeEndElement();
    }

    private void writeCell(
            XMLStreamWriter writer,
            CellCommand command,
            String existingStyle,
            OoxmlStyleRegistry.PreparedStyles styles,
            boolean date1904
    ) throws XMLStreamException {
        writer.writeStartElement("c");
        writer.writeAttribute("r", CellReferences.cell(command.row, command.column));
        if (command.emptyStyle && existingStyle != null) {
            writer.writeAttribute("s", existingStyle);
        } else if (command.styleToken >= 0) {
            writer.writeAttribute("s", Integer.toString(styles.styleId(command.styleToken)));
        }

        switch (command.type) {
            case TEXT:
                writer.writeAttribute("t", "inlineStr");
                writer.writeStartElement("is");
                writer.writeStartElement("t");
                writer.writeAttribute("xml", XMLConstants.XML_NS_URI, "space", "preserve");
                writer.writeCharacters(command.value);
                writer.writeEndElement();
                writer.writeEndElement();
                break;
            case BOOLEAN:
                writer.writeAttribute("t", "b");
                writeValue(writer, command.value);
                break;
            case NUMERIC:
                writeValue(writer, command.value);
                break;
            case DATE:
                writeValue(
                        writer,
                        Double.toString(DateUtil.getExcelDate(
                                LocalDate.parse(command.value),
                                date1904
                        ))
                );
                break;
            case DATETIME:
                writeValue(
                        writer,
                        Double.toString(DateUtil.getExcelDate(
                                LocalDateTime.parse(command.value),
                                date1904
                        ))
                );
                break;
            case FORMULA:
                writer.writeStartElement("f");
                writer.writeCharacters(command.value);
                writer.writeEndElement();
                break;
            default:
                throw new IllegalStateException("未対応のセル型です: " + command.type);
        }
        writer.writeEndElement();
    }

    private void writeValue(XMLStreamWriter writer, String value) throws XMLStreamException {
        writer.writeStartElement("v");
        writer.writeCharacters(value);
        writer.writeEndElement();
    }

    private void copyColumns(
            XMLStreamReader reader,
            XMLStreamWriter writer,
            Map<Integer, Integer> widths
    ) throws XMLStreamException {
        copyStartElement(reader, writer);
        List<ColumnDefinition> definitions = new ArrayList<>();
        while (reader.hasNext()) {
            int event = reader.next();
            if (event == XMLStreamConstants.END_ELEMENT
                    && "cols".equals(reader.getLocalName())) {
                mergeColumnWidths(definitions, widths);
                for (ColumnDefinition definition : definitions) {
                    definition.write(writer);
                }
                writer.writeEndElement();
                return;
            }
            if (event == XMLStreamConstants.START_ELEMENT
                    && "col".equals(reader.getLocalName())) {
                definitions.add(ColumnDefinition.read(reader));
                skipElement(reader);
            } else if (event == XMLStreamConstants.START_ELEMENT) {
                copyElement(reader, writer);
            } else {
                copyNonElementEvent(reader, writer, event);
            }
        }
    }

    private void writeColumns(XMLStreamWriter writer, Map<Integer, Integer> widths)
            throws XMLStreamException {
        if (widths.isEmpty()) {
            return;
        }
        writer.writeStartElement("cols");
        writeColumnDefinitions(writer, widths);
        writer.writeEndElement();
    }

    private void writeColumnDefinitions(
            XMLStreamWriter writer,
            Map<Integer, Integer> widths
    ) throws XMLStreamException {
        for (Map.Entry<Integer, Integer> entry : widths.entrySet()) {
            writer.writeEmptyElement("col");
            writer.writeAttribute("min", Integer.toString(entry.getKey() + 1));
            writer.writeAttribute("max", Integer.toString(entry.getKey() + 1));
            writer.writeAttribute("width", Integer.toString(entry.getValue()));
            writer.writeAttribute("customWidth", "1");
        }
    }

    private void mergeColumnWidths(
            List<ColumnDefinition> definitions,
            Map<Integer, Integer> widths
    ) {
        for (Map.Entry<Integer, Integer> width : widths.entrySet()) {
            int column = width.getKey() + 1;
            List<ColumnDefinition> merged = new ArrayList<>();
            boolean found = false;
            for (ColumnDefinition definition : definitions) {
                if (column < definition.min || column > definition.max) {
                    merged.add(definition);
                    continue;
                }
                found = true;
                if (definition.min < column) {
                    merged.add(definition.range(definition.min, column - 1));
                }
                ColumnDefinition replacement = definition.range(column, column);
                replacement.attributes.put("width", Integer.toString(width.getValue()));
                replacement.attributes.put("customWidth", "1");
                merged.add(replacement);
                if (column < definition.max) {
                    merged.add(definition.range(column + 1, definition.max));
                }
            }
            if (!found) {
                ColumnDefinition added = new ColumnDefinition(column, column);
                added.attributes.put("width", Integer.toString(width.getValue()));
                added.attributes.put("customWidth", "1");
                merged.add(added);
            }
            definitions.clear();
            definitions.addAll(merged);
        }
        definitions.sort(Comparator.comparingInt(definition -> definition.min));
    }

    private void writeAutoFilter(XMLStreamWriter writer, String reference)
            throws XMLStreamException {
        if (reference == null) {
            return;
        }
        writer.writeEmptyElement("autoFilter");
        writer.writeAttribute("ref", reference);
    }

    private boolean isAfterAutoFilter(String localName) {
        switch (localName) {
            case "sortState":
            case "dataConsolidate":
            case "customSheetViews":
            case "mergeCells":
            case "phoneticPr":
            case "conditionalFormatting":
            case "dataValidations":
            case "hyperlinks":
            case "printOptions":
            case "pageMargins":
            case "pageSetup":
            case "headerFooter":
            case "rowBreaks":
            case "colBreaks":
            case "customProperties":
            case "cellWatches":
            case "ignoredErrors":
            case "smartTags":
            case "drawing":
            case "legacyDrawing":
            case "legacyDrawingHF":
            case "picture":
            case "oleObjects":
            case "controls":
            case "webPublishItems":
            case "tableParts":
            case "extLst":
                return true;
            default:
                return false;
        }
    }

    private void copyElement(XMLStreamReader reader, XMLStreamWriter writer)
            throws XMLStreamException {
        int depth = 1;
        copyStartElement(reader, writer);
        while (depth > 0 && reader.hasNext()) {
            int event = reader.next();
            if (event == XMLStreamConstants.START_ELEMENT) {
                copyStartElement(reader, writer);
                depth++;
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                writer.writeEndElement();
                depth--;
            } else {
                copyNonElementEvent(reader, writer, event);
            }
        }
    }

    private void skipElement(XMLStreamReader reader) throws XMLStreamException {
        int depth = 1;
        while (depth > 0 && reader.hasNext()) {
            int event = reader.next();
            if (event == XMLStreamConstants.START_ELEMENT) {
                depth++;
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                depth--;
            }
        }
    }

    private void copyStartElement(XMLStreamReader reader, XMLStreamWriter writer)
            throws XMLStreamException {
        String prefix = reader.getPrefix();
        String namespace = reader.getNamespaceURI();
        if (namespace == null || namespace.isEmpty()) {
            writer.writeStartElement(reader.getLocalName());
        } else {
            writer.writeStartElement(prefix == null ? "" : prefix, reader.getLocalName(), namespace);
        }
        for (int index = 0; index < reader.getNamespaceCount(); index++) {
            String namespacePrefix = reader.getNamespacePrefix(index);
            String namespaceValue = reader.getNamespaceURI(index);
            if (namespacePrefix == null) {
                writer.writeDefaultNamespace(namespaceValue);
            } else {
                writer.writeNamespace(namespacePrefix, namespaceValue);
            }
        }
        for (int index = 0; index < reader.getAttributeCount(); index++) {
            String attributeNamespace = reader.getAttributeNamespace(index);
            String attributePrefix = reader.getAttributePrefix(index);
            if (attributeNamespace == null || attributeNamespace.isEmpty()) {
                writer.writeAttribute(
                        reader.getAttributeLocalName(index),
                        reader.getAttributeValue(index)
                );
            } else {
                writer.writeAttribute(
                        attributePrefix == null ? "" : attributePrefix,
                        attributeNamespace,
                        reader.getAttributeLocalName(index),
                        reader.getAttributeValue(index)
                );
            }
        }
    }

    private void copyNonElementEvent(
            XMLStreamReader reader,
            XMLStreamWriter writer,
            int event
    ) throws XMLStreamException {
        switch (event) {
            case XMLStreamConstants.CHARACTERS:
            case XMLStreamConstants.SPACE:
                writer.writeCharacters(reader.getText());
                break;
            case XMLStreamConstants.CDATA:
                writer.writeCData(reader.getText());
                break;
            case XMLStreamConstants.COMMENT:
                writer.writeComment(reader.getText());
                break;
            case XMLStreamConstants.PROCESSING_INSTRUCTION:
                writer.writeProcessingInstruction(
                        reader.getPITarget(),
                        reader.getPIData() == null ? "" : reader.getPIData()
                );
                break;
            default:
                break;
        }
    }

    private void setIfSupported(String property, Object value) {
        try {
            inputFactory.setProperty(property, value);
        } catch (IllegalArgumentException ignored) {
            // 実装が対応しないプロパティは無視します。
        }
    }

    private static final class CommandCursor {
        private final CellCommandStore.SortedIterator iterator;
        private CellCommand current;

        private CommandCursor(CellCommandStore.SortedIterator iterator) {
            this.iterator = iterator;
            advance();
        }

        private boolean has() {
            return current != null;
        }

        private CellCommand current() {
            return current;
        }

        private boolean isAtRow(int row) {
            return current != null && current.row == row;
        }

        private boolean isAt(int row, int column) {
            return current != null && current.row == row && current.column == column;
        }

        private CellCommand take() {
            CellCommand result = current;
            advance();
            return result;
        }

        private void advance() {
            current = iterator.hasNext() ? iterator.next() : null;
        }
    }

    private static final class ColumnDefinition {
        private int min;
        private int max;
        private final Map<String, String> attributes = new LinkedHashMap<>();

        private ColumnDefinition(int min, int max) {
            this.min = min;
            this.max = max;
        }

        private static ColumnDefinition read(XMLStreamReader reader) {
            int min = Integer.parseInt(reader.getAttributeValue(null, "min"));
            int max = Integer.parseInt(reader.getAttributeValue(null, "max"));
            ColumnDefinition result = new ColumnDefinition(min, max);
            for (int index = 0; index < reader.getAttributeCount(); index++) {
                result.attributes.put(
                        reader.getAttributeLocalName(index),
                        reader.getAttributeValue(index)
                );
            }
            return result;
        }

        private ColumnDefinition range(int newMin, int newMax) {
            ColumnDefinition result = new ColumnDefinition(newMin, newMax);
            result.attributes.putAll(attributes);
            result.attributes.put("min", Integer.toString(newMin));
            result.attributes.put("max", Integer.toString(newMax));
            return result;
        }

        private void write(XMLStreamWriter writer) throws XMLStreamException {
            writer.writeEmptyElement("col");
            attributes.put("min", Integer.toString(min));
            attributes.put("max", Integer.toString(max));
            for (Map.Entry<String, String> attribute : attributes.entrySet()) {
                writer.writeAttribute(attribute.getKey(), attribute.getValue());
            }
        }
    }
}
