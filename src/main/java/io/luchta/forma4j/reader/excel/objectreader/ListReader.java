package io.luchta.forma4j.reader.excel.objectreader;

import io.luchta.forma4j.context.databind.json.JsonNode;
import io.luchta.forma4j.context.databind.json.JsonNodes;
import io.luchta.forma4j.context.databind.json.JsonObject;
import io.luchta.forma4j.reader.model.tag.ListTag;
import io.luchta.forma4j.reader.model.tag.TagTree;
import org.apache.poi.ss.usermodel.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ListReader implements ObjectReader {
    Sheet sheet;
    TagTree tagTree;

    public ListReader(Sheet sheet, TagTree tagTree) {
        this.sheet = sheet;
        this.tagTree = tagTree;
    }

    @Override
    public JsonNode read() {
        ListTag listTag = (ListTag) tagTree.getTag();

        Integer headerStartRowNum = listTag.getHeaderStartRow().toInteger();
        Integer headerStartColNum = listTag.getHeaderStartCol().toInteger();
        Integer detailStartRowNum = listTag.getDetailStartRow().toInteger();
        Integer detailStartColNum = listTag.getDetailStartCol().toInteger();

        List<String> headerList = new ArrayList<>();
        Integer i = headerStartColNum;
        Row headerRow = sheet.getRow(headerStartRowNum);
        while (i < headerRow.getLastCellNum()) {
            headerList.add(headerRow.getCell(i).toString());
            i++;
        }

        List<JsonNode> nodeList = new ArrayList<>();
        Integer j = detailStartRowNum;
        while (j <= sheet.getLastRowNum()) {
            JsonNode node = new JsonNode();
            Integer k = detailStartColNum;
            Row row = sheet.getRow(j);
            for (String headerName : headerList) {
                if (k >= row.getLastCellNum()) {
                    node.putVar(headerName, new JsonObject());
                } else {
                    Cell cell = row.getCell(k);
                    if (cell == null || cell.getCellType() == CellType.BLANK) {
                        node.putVar(headerName, new JsonObject());
                    } else if (cell.getCellType() == CellType.NUMERIC) {
                        if (DateUtil.isCellDateFormatted(cell)) {
                            Date date = cell.getDateCellValue();
                            Instant instant = date.toInstant();
                            node.putVar(headerName, new JsonObject(LocalDateTime.ofInstant(instant, ZoneId.systemDefault())));
                        } else {
                            node.putVar(headerName, new JsonObject(cell.getNumericCellValue()));
                        }
                    } else {
                        node.putVar(headerName, new JsonObject(cell.toString()));
                    }
                }
                k++;
            }
            nodeList.add(node);
            j++;
        }
        JsonObject jsonObject = new JsonObject(new JsonNodes(nodeList));
        JsonNode node = new JsonNode();
        node.putVar(listTag.name().toString(), jsonObject);
        return node;
    }
}
