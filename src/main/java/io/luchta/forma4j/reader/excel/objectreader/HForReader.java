package io.luchta.forma4j.reader.excel.objectreader;

import io.luchta.forma4j.context.databind.json.JsonNode;
import io.luchta.forma4j.context.databind.json.JsonObject;
import io.luchta.forma4j.reader.model.excel.Index;
import io.luchta.forma4j.reader.model.tag.*;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.Map;

public class HForReader implements ObjectReader {

    Sheet sheet;
    Index rowIndex;
    TagTree tagTree;

    public HForReader(Sheet sheet, Index rowIndex, TagTree tagTree) {
        this.sheet = sheet;
        this.rowIndex = rowIndex;
        this.tagTree = tagTree;
    }

    @Override
    public JsonNode read() {

        HForTag hForTag = (HForTag) tagTree.getTag();

        Integer row = hForTag.rowIsUndefined() ? rowIndex.toInteger() : hForTag.row().toInteger();
        Integer fromCol = hForTag.fromIsUndefined() ? 0 : hForTag.from().toInteger();
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
                    if (r == null || r.getLastCellNum() <= i) {
                        isNotLastCellNum = false;
                        continue;
                    }
                }

                ObjectReaderFactoryParameter param = new ObjectReaderFactoryParameter(
                        sheet, new Index(row), new Index(i), child, tag
                );
                ObjectReader reader = factory.create(param);
                JsonNode node = reader.read();
                for (Map.Entry<String, JsonObject> entry : node.entrySet()) {
                    resultNode.putVar(entry.getKey() + (i - fromCol + 1), entry.getValue());
                }
            }
            if (!hForTag.toIsUndefined() && toCol <= i) {
                break;
            } else if (hForTag.toIsUndefined() && !isNotLastCellNum) {
                break;
            }
            i++;
        } while (true);

        JsonNode node = new JsonNode();
        node.putVar(hForTag.name().toString(), new JsonObject(resultNode));
        return node;
    }
}
