package io.luchta.forma4j.reader.excel.objectreader;

import io.luchta.forma4j.context.databind.json.JsonNode;
import io.luchta.forma4j.context.databind.json.JsonObject;
import io.luchta.forma4j.reader.model.excel.Index;
import io.luchta.forma4j.reader.model.tag.*;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.Map;

public class HForReader implements ObjectReader {

    @Override
    public JsonObject read(Sheet sheet, Index rowIndex, Index colIndex, TagTree tagTree) {

        HForTag hForTag = (HForTag) tagTree.getTag();

        Integer row = rowIndex.toInteger();
        Integer fromCol = hForTag.fromIsUndefined() ? rowIndex.toInteger() : hForTag.from().toInteger();
        Integer toCol = hForTag.to().toInteger();

        ObjectReaderFactory factory = new ObjectReaderFactory();
        JsonNode resultNode = new JsonNode();
        Integer i = fromCol;
        do {
            boolean isNotLastCellNum = true;
            TagTrees children = tagTree.getChildren();
            for (TagTree child : children) {
                Tag tag = child.getTag();

                if (tag.isCell()) {
                    CellTag cellTag = (CellTag) tag;
                    Row r;
                    if (cellTag.rowIsUndefined()) {
                        r = sheet.getRow(row);
                    } else {
                        r = sheet.getRow(cellTag.row().toInteger());
                    }
                    if (r == null || r.getLastCellNum() == i) {
                        isNotLastCellNum = false;
                        continue;
                    }
                }

                ObjectReader reader = factory.create(tag);
                JsonObject obj = reader.read(sheet, rowIndex, new Index(i), child);
                JsonNode node = (JsonNode) obj.getValue();
                for (Map.Entry<String, JsonObject> entry : node.entrySet()) {
                    resultNode.putVar(entry.getKey() + (i - fromCol + 1), entry.getValue());
                }
            }
            if (!hForTag.toIsUndefined() && toCol < i) {
                break;
            } else if (hForTag.toIsUndefined() && !isNotLastCellNum) {
                break;
            }
            i++;
        } while (true);

        JsonNode node = new JsonNode();
        node.putVar(hForTag.name().toString(), new JsonObject(resultNode));
        return new JsonObject(node);
    }
}
