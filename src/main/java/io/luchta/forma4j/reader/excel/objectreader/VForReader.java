package io.luchta.forma4j.reader.excel.objectreader;

import io.luchta.forma4j.context.databind.json.JsonNode;
import io.luchta.forma4j.context.databind.json.JsonNodes;
import io.luchta.forma4j.context.databind.json.JsonObject;
import io.luchta.forma4j.reader.excel.Accumulator;
import io.luchta.forma4j.reader.model.excel.Header;
import io.luchta.forma4j.reader.model.excel.Index;
import io.luchta.forma4j.reader.model.tag.Tag;
import io.luchta.forma4j.reader.model.tag.TagTree;
import io.luchta.forma4j.reader.model.tag.TagTrees;
import io.luchta.forma4j.reader.model.tag.VForTag;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class VForReader implements ObjectReader {

    Sheet sheet;
    Index rowIndex;
    Index colIndex;
    TagTree tagTree;

    public VForReader(Sheet sheet, Index rowIndex, Index colIndex, TagTree tagTree) {
        this.sheet = sheet;
        this.rowIndex = rowIndex;
        this.colIndex = colIndex;
        this.tagTree = tagTree;
    }

    @Override
    public JsonObject read() {

        VForTag vForTag = (VForTag) tagTree.getTag();

        Integer fromRow = vForTag.fromIsUndefined() ? rowIndex.toInteger() : vForTag.from().toInteger();
        Integer toRow = vForTag.to().toInteger();
        Integer col = colIndex.toInteger();

        ObjectReaderFactory factory = new ObjectReaderFactory();
        Header header = new Header();
        if (Accumulator.containsKey(vForTag.header().toString())) {
            header = (Header) Accumulator.getData(vForTag.header().toString());
        }
        List<JsonNode> nodeList = new ArrayList<>();
        Integer i = fromRow;
        do {
            JsonNode childrenNode = new JsonNode();
            TagTrees children = tagTree.getChildren();
            for (TagTree child : children) {
                Tag tag = child.getTag();
                ObjectReaderFactoryParameter param = new ObjectReaderFactoryParameter(
                        sheet, new Index(i), colIndex, header, child, tag
                );
                ObjectReader reader = factory.create(param);
                JsonObject obj = reader.read();
                JsonNode node = (JsonNode) obj.getValue();
                for (Map.Entry<String, JsonObject> entry : node.entrySet()) {
                    childrenNode.putVar(entry.getKey(), entry.getValue());
                }
            }
            nodeList.add(childrenNode);
            if (!vForTag.toIsUndefined() && toRow < i) {
                break;
            } else if (vForTag.toIsUndefined() && sheet.getLastRowNum() < i) {
                break;
            } else if (sheet.getLastRowNum() == i) {
                break;
            }
            i++;
        } while (true);

        JsonNode node = new JsonNode();
        node.putVar(vForTag.name().toString(), new JsonObject(new JsonNodes(nodeList)));

        return new JsonObject(node);
    }
}
