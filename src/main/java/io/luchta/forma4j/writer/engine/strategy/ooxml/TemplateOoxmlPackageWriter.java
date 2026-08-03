package io.luchta.forma4j.writer.engine.strategy.ooxml;

import org.apache.poi.ss.util.WorkbookUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

final class TemplateOoxmlPackageWriter {
    private static final String RELATIONSHIPS_NAMESPACE =
            "http://schemas.openxmlformats.org/package/2006/relationships";
    private static final String OFFICE_RELATIONSHIPS_NAMESPACE =
            "http://schemas.openxmlformats.org/officeDocument/2006/relationships";
    private static final String CONTENT_TYPES_NAMESPACE =
            "http://schemas.openxmlformats.org/package/2006/content-types";
    private static final String WORKSHEET_RELATIONSHIP =
            OFFICE_RELATIONSHIPS_NAMESPACE + "/worksheet";
    private static final String STYLES_RELATIONSHIP =
            OFFICE_RELATIONSHIPS_NAMESPACE + "/styles";
    private static final String CALC_CHAIN_RELATIONSHIP =
            OFFICE_RELATIONSHIPS_NAMESPACE + "/calcChain";

    private final Path template;
    private final WorksheetXmlWriter worksheetWriter = new WorksheetXmlWriter();

    TemplateOoxmlPackageWriter(Path template) {
        this.template = template;
    }

    void write(
            OutputStream output,
            Map<String, SheetState> requestedSheets,
            OoxmlStyleRegistry styleRegistry,
            boolean formulas
    ) throws IOException {
        try (ZipFile zip = new ZipFile(template.toFile())) {
            rejectUnsupportedPackage(zip);
            Metadata metadata = Metadata.read(zip);
            metadata.addRequestedSheets(requestedSheets);
            if (formulas) {
                metadata.enableFormulaRecalculation();
            }

            OoxmlStyleRegistry.PreparedStyles preparedStyles;
            boolean rewriteStyles = styleRegistry.hasCustomStyles();
            if (rewriteStyles) {
                ZipEntry styleEntry = metadata.stylesPart == null
                        ? null
                        : zip.getEntry(metadata.stylesPart);
                try (InputStream input = styleEntry == null ? null : zip.getInputStream(styleEntry)) {
                    preparedStyles = styleRegistry.prepareTemplate(input);
                }
                metadata.ensureStylesPart();
            } else {
                preparedStyles = styleRegistry.prepareBlank();
            }

            ZipOutputStream result = new ZipOutputStream(output);
            Enumeration<? extends ZipEntry> entries = zip.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                String name = entry.getName();
                if (entry.isDirectory()
                        || metadata.shouldSkip(
                                name,
                                requestedSheets,
                                rewriteStyles,
                                formulas
                        )) {
                    continue;
                }
                putEntry(result, name);
                try (InputStream input = zip.getInputStream(entry)) {
                    copy(input, result);
                }
                result.closeEntry();
            }

            writeDocument(result, "[Content_Types].xml", metadata.contentTypes);
            writeDocument(result, metadata.workbookPart, metadata.workbook);
            writeDocument(result, metadata.workbookRelationshipsPart, metadata.workbookRelationships);

            if (rewriteStyles) {
                putEntry(result, metadata.stylesPart);
                preparedStyles.write(result);
                result.closeEntry();
            }

            for (Map.Entry<String, SheetState> requested : requestedSheets.entrySet()) {
                String part = metadata.sheetParts.get(requested.getKey());
                if (part == null) {
                    throw new IOException("ワークシートのパーツを解決できません: " + requested.getKey());
                }
                SheetState state = requested.getValue();
                if (!state.hasChanges() && zip.getEntry(part) != null) {
                    continue;
                }
                putEntry(result, part);
                ZipEntry original = zip.getEntry(part);
                if (original == null) {
                    worksheetWriter.writeBlank(result, state, preparedStyles, metadata.date1904);
                } else {
                    try (InputStream input = zip.getInputStream(original)) {
                        worksheetWriter.transform(
                                input,
                                result,
                                state,
                                preparedStyles,
                                metadata.date1904
                        );
                    }
                }
                result.closeEntry();
            }

            // 変更対象だが空だった既存シートは上のコピー経路で既に出力されています。
            result.finish();
            result.flush();
        }
    }

    private void rejectUnsupportedPackage(ZipFile zip) {
        Enumeration<? extends ZipEntry> entries = zip.entries();
        while (entries.hasMoreElements()) {
            String name = entries.nextElement().getName();
            String lower = name.toLowerCase();
            if (lower.startsWith("_xmlsignatures/")) {
                throw new IllegalArgumentException("電子署名付きテンプレートには出力できません。");
            }
            if (lower.endsWith("/vbaproject.bin") || lower.equals("xl/vbaproject.bin")) {
                throw new IllegalArgumentException("マクロ付きテンプレートには出力できません。");
            }
        }
    }

    private static void writeDocument(
            ZipOutputStream zip,
            String name,
            Document document
    ) throws IOException {
        putEntry(zip, name);
        try {
            TransformerFactory factory = TransformerFactory.newInstance();
            try {
                factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            } catch (TransformerException ignored) {
                // 実装が対応しない場合も外部リソースは使用しません。
            }
            Transformer transformer = factory.newTransformer();
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
            transformer.transform(new DOMSource(document), new StreamResult(zip));
        } catch (TransformerException e) {
            throw new IOException("OOXMLパーツの書込みに失敗しました: " + name, e);
        }
        zip.closeEntry();
    }

    private static void putEntry(ZipOutputStream zip, String name) throws IOException {
        zip.putNextEntry(new ZipEntry(name));
    }

    private static void copy(InputStream input, OutputStream output) throws IOException {
        byte[] buffer = new byte[8192];
        int length;
        while ((length = input.read(buffer)) >= 0) {
            output.write(buffer, 0, length);
        }
    }

    private static final class Metadata {
        private final Document contentTypes;
        private final Document workbook;
        private final Document workbookRelationships;
        private final String workbookPart;
        private final String workbookRelationshipsPart;
        private final String workbookDirectory;
        private final Map<String, String> relationshipTargets = new HashMap<>();
        private final Map<String, String> sheetParts = new LinkedHashMap<>();
        private final Set<String> newSheetParts = new HashSet<>();
        private String stylesPart;
        private String calcChainPart;
        private boolean date1904;
        private int nextSheetId;
        private int nextRelationshipId;
        private int nextSheetPartNumber;

        private Metadata(
                Document contentTypes,
                Document workbook,
                Document workbookRelationships,
                String workbookPart,
                String workbookRelationshipsPart
        ) {
            this.contentTypes = contentTypes;
            this.workbook = workbook;
            this.workbookRelationships = workbookRelationships;
            this.workbookPart = workbookPart;
            this.workbookRelationshipsPart = workbookRelationshipsPart;
            int slash = workbookPart.lastIndexOf('/');
            workbookDirectory = slash < 0 ? "" : workbookPart.substring(0, slash + 1);
        }

        static Metadata read(ZipFile zip) throws IOException {
            Document contentTypes = parse(zip, "[Content_Types].xml");
            Document rootRelationships = parse(zip, "_rels/.rels");
            String workbookPart = null;
            NodeList rootRelations = rootRelationships.getElementsByTagNameNS(
                    RELATIONSHIPS_NAMESPACE,
                    "Relationship"
            );
            for (int index = 0; index < rootRelations.getLength(); index++) {
                Element relationship = (Element) rootRelations.item(index);
                if (relationship.getAttribute("Type").endsWith("/officeDocument")) {
                    workbookPart = normalizePart("", relationship.getAttribute("Target"));
                    break;
                }
            }
            if (workbookPart == null) {
                throw new IOException("テンプレートのworkbookパーツを解決できません。");
            }
            String relationshipsPart = relationshipPart(workbookPart);
            Metadata result = new Metadata(
                    contentTypes,
                    parse(zip, workbookPart),
                    parse(zip, relationshipsPart),
                    workbookPart,
                    relationshipsPart
            );
            result.index();
            return result;
        }

        private void index() {
            NodeList relations = workbookRelationships.getElementsByTagNameNS(
                    RELATIONSHIPS_NAMESPACE,
                    "Relationship"
            );
            for (int index = 0; index < relations.getLength(); index++) {
                Element relationship = (Element) relations.item(index);
                String id = relationship.getAttribute("Id");
                String type = relationship.getAttribute("Type");
                String part = normalizePart(workbookDirectory, relationship.getAttribute("Target"));
                relationshipTargets.put(id, part);
                nextRelationshipId = Math.max(nextRelationshipId, numericSuffix(id) + 1);
                if (type.endsWith("/styles")) {
                    stylesPart = part;
                } else if (type.endsWith("/calcChain")) {
                    calcChainPart = part;
                }
            }

            NodeList workbookSheets = workbook.getElementsByTagNameNS(
                    WorksheetXmlWriter.SPREADSHEET_NAMESPACE,
                    "sheet"
            );
            for (int index = 0; index < workbookSheets.getLength(); index++) {
                Element sheet = (Element) workbookSheets.item(index);
                String relationshipId = sheet.getAttributeNS(
                        OFFICE_RELATIONSHIPS_NAMESPACE,
                        "id"
                );
                sheetParts.put(sheet.getAttribute("name"), relationshipTargets.get(relationshipId));
                nextSheetId = Math.max(
                        nextSheetId,
                        parseInteger(sheet.getAttribute("sheetId"), index + 1) + 1
                );
            }
            if (nextSheetId == 0) {
                nextSheetId = 1;
            }
            if (nextRelationshipId == 0) {
                nextRelationshipId = 1;
            }

            for (String part : relationshipTargets.values()) {
                String name = part.substring(part.lastIndexOf('/') + 1);
                if (name.startsWith("sheet") && name.endsWith(".xml")) {
                    nextSheetPartNumber = Math.max(
                            nextSheetPartNumber,
                            parseInteger(name.substring(5, name.length() - 4), 0) + 1
                    );
                }
            }
            if (nextSheetPartNumber == 0) {
                nextSheetPartNumber = 1;
            }

            NodeList properties = workbook.getElementsByTagNameNS(
                    WorksheetXmlWriter.SPREADSHEET_NAMESPACE,
                    "workbookPr"
            );
            if (properties.getLength() > 0) {
                String value = ((Element) properties.item(0)).getAttribute("date1904");
                date1904 = "1".equals(value) || "true".equalsIgnoreCase(value);
            }
        }

        private void addRequestedSheets(Map<String, SheetState> requested) {
            for (String sheetName : requested.keySet()) {
                WorkbookUtil.validateSheetName(sheetName);
                if (sheetParts.containsKey(sheetName)) {
                    continue;
                }
                addSheet(sheetName);
            }
        }

        private void addSheet(String name) {
            String relationshipId = nextRelationshipId();
            String relativeTarget = "worksheets/sheet" + nextSheetPartNumber++ + ".xml";
            String part = normalizePart(workbookDirectory, relativeTarget);

            Element relationship = workbookRelationships.createElementNS(
                    RELATIONSHIPS_NAMESPACE,
                    "Relationship"
            );
            relationship.setAttribute("Id", relationshipId);
            relationship.setAttribute("Type", WORKSHEET_RELATIONSHIP);
            relationship.setAttribute("Target", relativeTarget);
            workbookRelationships.getDocumentElement().appendChild(relationship);

            NodeList sheetContainers = workbook.getElementsByTagNameNS(
                    WorksheetXmlWriter.SPREADSHEET_NAMESPACE,
                    "sheets"
            );
            Element sheetsElement;
            if (sheetContainers.getLength() == 0) {
                sheetsElement = workbook.createElementNS(
                        WorksheetXmlWriter.SPREADSHEET_NAMESPACE,
                        "sheets"
                );
                workbook.getDocumentElement().appendChild(sheetsElement);
            } else {
                sheetsElement = (Element) sheetContainers.item(0);
            }
            Element sheet = workbook.createElementNS(
                    WorksheetXmlWriter.SPREADSHEET_NAMESPACE,
                    "sheet"
            );
            sheet.setAttribute("name", name);
            sheet.setAttribute("sheetId", Integer.toString(nextSheetId++));
            sheet.setAttributeNS(
                    OFFICE_RELATIONSHIPS_NAMESPACE,
                    "r:id",
                    relationshipId
            );
            sheetsElement.appendChild(sheet);

            addContentType(part,
                    "application/vnd.openxmlformats-officedocument.spreadsheetml.worksheet+xml");
            sheetParts.put(name, part);
            newSheetParts.add(part);
        }

        private void ensureStylesPart() {
            if (stylesPart != null) {
                return;
            }
            String relationshipId = nextRelationshipId();
            String relativeTarget = "styles.xml";
            stylesPart = normalizePart(workbookDirectory, relativeTarget);
            Element relationship = workbookRelationships.createElementNS(
                    RELATIONSHIPS_NAMESPACE,
                    "Relationship"
            );
            relationship.setAttribute("Id", relationshipId);
            relationship.setAttribute("Type", STYLES_RELATIONSHIP);
            relationship.setAttribute("Target", relativeTarget);
            workbookRelationships.getDocumentElement().appendChild(relationship);
            addContentType(
                    stylesPart,
                    "application/vnd.openxmlformats-officedocument.spreadsheetml.styles+xml"
            );
        }

        private void enableFormulaRecalculation() {
            NodeList calculations = workbook.getElementsByTagNameNS(
                    WorksheetXmlWriter.SPREADSHEET_NAMESPACE,
                    "calcPr"
            );
            Element calculation;
            if (calculations.getLength() == 0) {
                calculation = workbook.createElementNS(
                        WorksheetXmlWriter.SPREADSHEET_NAMESPACE,
                        "calcPr"
                );
                NodeList extensions = workbook.getElementsByTagNameNS(
                        WorksheetXmlWriter.SPREADSHEET_NAMESPACE,
                        "extLst"
                );
                if (extensions.getLength() == 0) {
                    workbook.getDocumentElement().appendChild(calculation);
                } else {
                    workbook.getDocumentElement().insertBefore(
                            calculation,
                            extensions.item(0)
                    );
                }
            } else {
                calculation = (Element) calculations.item(0);
            }
            calculation.setAttribute("calcMode", "auto");
            calculation.setAttribute("fullCalcOnLoad", "1");
            calculation.setAttribute("forceFullCalc", "1");

            List<Element> toRemove = new ArrayList<>();
            NodeList relations = workbookRelationships.getElementsByTagNameNS(
                    RELATIONSHIPS_NAMESPACE,
                    "Relationship"
            );
            for (int index = 0; index < relations.getLength(); index++) {
                Element relation = (Element) relations.item(index);
                if (relation.getAttribute("Type").endsWith("/calcChain")) {
                    toRemove.add(relation);
                }
            }
            for (Element relation : toRemove) {
                relation.getParentNode().removeChild(relation);
            }
            if (calcChainPart != null) {
                removeContentType(calcChainPart);
            }
        }

        private boolean shouldSkip(
                String name,
                Map<String, SheetState> requestedSheets,
                boolean rewriteStyles,
                boolean formulas
        ) {
            if ("[Content_Types].xml".equals(name)
                    || workbookPart.equals(name)
                    || workbookRelationshipsPart.equals(name)) {
                return true;
            }
            if (rewriteStyles && name.equals(stylesPart)) {
                return true;
            }
            if (formulas && name.equals(calcChainPart)) {
                return true;
            }
            for (Map.Entry<String, SheetState> requested : requestedSheets.entrySet()) {
                String part = sheetParts.get(requested.getKey());
                if (name.equals(part)
                        && (requested.getValue().hasChanges() || newSheetParts.contains(name))) {
                    return true;
                }
            }
            return false;
        }

        private void addContentType(String part, String contentType) {
            Element override = contentTypes.createElementNS(
                    CONTENT_TYPES_NAMESPACE,
                    "Override"
            );
            override.setAttribute("PartName", "/" + part);
            override.setAttribute("ContentType", contentType);
            contentTypes.getDocumentElement().appendChild(override);
        }

        private void removeContentType(String part) {
            List<Element> toRemove = new ArrayList<>();
            NodeList overrides = contentTypes.getElementsByTagNameNS(
                    CONTENT_TYPES_NAMESPACE,
                    "Override"
            );
            for (int index = 0; index < overrides.getLength(); index++) {
                Element override = (Element) overrides.item(index);
                if (("/" + part).equals(override.getAttribute("PartName"))) {
                    toRemove.add(override);
                }
            }
            for (Element override : toRemove) {
                override.getParentNode().removeChild(override);
            }
        }

        private String nextRelationshipId() {
            String candidate;
            do {
                candidate = "rId" + nextRelationshipId++;
            } while (relationshipTargets.containsKey(candidate));
            return candidate;
        }
    }

    private static Document parse(ZipFile zip, String part) throws IOException {
        ZipEntry entry = zip.getEntry(part);
        if (entry == null) {
            throw new IOException("テンプレートに必要なOOXMLパーツがありません: " + part);
        }
        try (InputStream input = zip.getInputStream(entry)) {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
            factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
            factory.setXIncludeAware(false);
            factory.setExpandEntityReferences(false);
            DocumentBuilder builder = factory.newDocumentBuilder();
            return builder.parse(input);
        } catch (ParserConfigurationException | SAXException e) {
            throw new IOException("OOXMLパーツの解析に失敗しました: " + part, e);
        }
    }

    private static String relationshipPart(String part) {
        int slash = part.lastIndexOf('/');
        String directory = slash < 0 ? "" : part.substring(0, slash + 1);
        String file = slash < 0 ? part : part.substring(slash + 1);
        return directory + "_rels/" + file + ".rels";
    }

    private static String normalizePart(String baseDirectory, String target) {
        String value = target.replace('\\', '/');
        if (value.startsWith("/")) {
            value = value.substring(1);
        } else {
            value = baseDirectory + value;
        }
        List<String> normalized = new ArrayList<>();
        for (String segment : value.split("/")) {
            if (segment.isEmpty() || ".".equals(segment)) {
                continue;
            }
            if ("..".equals(segment)) {
                if (!normalized.isEmpty()) {
                    normalized.remove(normalized.size() - 1);
                }
            } else {
                normalized.add(segment);
            }
        }
        return String.join("/", normalized);
    }

    private static int numericSuffix(String value) {
        int index = value.length() - 1;
        while (index >= 0 && Character.isDigit(value.charAt(index))) {
            index--;
        }
        if (index == value.length() - 1) {
            return 0;
        }
        return parseInteger(value.substring(index + 1), 0);
    }

    private static int parseInteger(String value, int fallback) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return fallback;
        }
    }
}
