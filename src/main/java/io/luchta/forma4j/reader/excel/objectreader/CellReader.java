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
import java.util.*;

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
    public JsonNode read() {

        CellTag cellTag = (CellTag) tagTree.getTag();

        Integer row = cellTag.rowIsUndefined() ? rowIndex.toInteger() : cellTag.row().toInteger();
        Integer col = cellTag.colIsUndefined() ? colIndex.toInteger() : cellTag.col().toInteger();

        String[] names = cellTag.name().toString().split("\\.");

        Row r = sheet.getRow(row);
        if (r == null) {
            return setValue(names, new JsonObject());
        }
        Cell cell = sheet.getRow(row).getCell(col);

        if (cell == null || cell.getCellType() == CellType.BLANK) {
            return setValue(names, new JsonObject());
        } else if (cell.getCellType() == CellType.NUMERIC) {
            if (DateUtil.isCellDateFormatted(cell)) {
                Date date = cell.getDateCellValue();
                Instant instant = date.toInstant();
                return setValue(names, new JsonObject(LocalDateTime.ofInstant(instant, ZoneId.systemDefault())));
            } else {
                return setValue(names, new JsonObject(cell.getNumericCellValue()));
            }
        }

        return setValue(names, new JsonObject(cell.toString()));
    }

    private JsonNode setValue(String[] names, JsonObject jsonObject) {
        Map<String, JsonNode> map = new HashMap<>();
        for (String name : names) {
            JsonNode node = new JsonNode();
            map.put(name, node);
        }

        String valueName = names[names.length - 1];
        JsonNode result = map.get(valueName);
        result.putVar(valueName, jsonObject);
        map.put(valueName, result);

        for (int i = map.size() - 2; i >= 0; i--) {
            JsonNode parent = map.get(names[i]);
            JsonNode child = map.get(names[i + 1]);
            parent.putVar(names[i], new JsonObject(child));
            map.put(names[i], parent);
        }

        return map.get(names[0]);
    }
}
