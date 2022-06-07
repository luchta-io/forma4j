package io.luchta.forma4j.reader.excel.objectreader;

import io.luchta.forma4j.context.databind.json.JsonNode;
import io.luchta.forma4j.context.databind.json.JsonObject;
import io.luchta.forma4j.reader.model.excel.Index;
import io.luchta.forma4j.reader.model.tag.CellTag;
import io.luchta.forma4j.reader.model.tag.TagTree;
import org.apache.poi.ss.usermodel.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class CellReader implements ObjectReader {

    Sheet sheet;
    Index rowIndex;
    Index colIndex;
    TagTree tagTree;

    public CellReader(Sheet sheet, Index rowIndex, Index colIndex, TagTree tagTree) {
        this.sheet = sheet;
        this.rowIndex = rowIndex;
        this.colIndex = colIndex;
        this.tagTree = tagTree;
    }

    @Override
    public JsonObject read() {

        CellTag cellTag = (CellTag) tagTree.getTag();

        Integer row = cellTag.rowIsUndefined() ? rowIndex.toInteger() : cellTag.row().toInteger();
        Integer col = cellTag.colIsUndefined() ? colIndex.toInteger() : cellTag.col().toInteger();

        Row r = sheet.getRow(row);
        if (r == null) {
            JsonNode node = new JsonNode();
            node.putVar(cellTag.name().toString(), new JsonObject(""));
            return new JsonObject(node);
        }
        Cell cell = sheet.getRow(row).getCell(col);

        if (cell.getCellType() == CellType.NUMERIC) {
            if (DateUtil.isCellDateFormatted(cell)) {
                Date date = cell.getDateCellValue();
                Instant instant = date.toInstant();
                JsonNode node = new JsonNode();
                node.putVar(cellTag.name().toString(), new JsonObject(LocalDateTime.ofInstant(instant, ZoneId.systemDefault())));
                return new JsonObject(node);
            } else {
                JsonNode node = new JsonNode();
                node.putVar(cellTag.name().toString(), new JsonObject(cell.getNumericCellValue()));
                return new JsonObject(node);
            }
        }

        JsonNode node = new JsonNode();
        node.putVar(cellTag.name().toString(), new JsonObject(cell.toString()));
        return new JsonObject(node);
    }
}
