package io.luchta.forma4j.reader.excel;

import io.luchta.forma4j.context.databind.json.JsonNode;
import io.luchta.forma4j.context.databind.json.JsonObject;
import io.luchta.forma4j.reader.excel.objectreader.ObjectReader;
import io.luchta.forma4j.reader.excel.objectreader.ObjectReaderFactory;
import io.luchta.forma4j.reader.excel.objectreader.ObjectReaderFactoryParameter;
import io.luchta.forma4j.reader.model.excel.Index;
import io.luchta.forma4j.reader.specification.DefaultTagTreeSpec;
import io.luchta.forma4j.reader.model.tag.SheetTag;
import io.luchta.forma4j.reader.model.tag.Tag;
import io.luchta.forma4j.reader.model.tag.TagTree;
import io.luchta.forma4j.reader.model.tag.TagTrees;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.IOException;
import java.io.InputStream;

public class ExcelReader {

    public JsonObject read(InputStream inputStream) throws IOException {
        DefaultTagTreeSpec defaultTagTreeSpec = new DefaultTagTreeSpec();
        TagTree tagTree = defaultTagTreeSpec.create();
        return read(inputStream, tagTree);
    }

    public JsonObject read(InputStream inputStream, TagTree tagTree) throws IOException {

        JsonNode node = new JsonNode();
        Workbook workbook = WorkbookFactory.create(inputStream);
        for (TagTree tree : tagTree.getChildren()) {
            Tag tag = tree.getTag();
            if (tag.isSheet()) {
                JsonObject obj = read(workbook, tree);
                node.putVar("forma-reader", obj);
            }
        }
        return new JsonObject(node);
    }

    private JsonObject read(Workbook workbook, TagTree tagTree) {

        SheetTag sheetTag = (SheetTag) tagTree.getTag();
        Sheet sheet = workbook.getSheet(sheetTag.name().toString());

        JsonObject obj = read(sheet, tagTree.getChildren());
        JsonNode node = new JsonNode();
        node.putVar(sheetTag.name().toString(), obj);
        return new JsonObject(node);
    }

    private JsonObject read(Sheet sheet, TagTrees tagTrees) {

        JsonObject obj = null;

        ObjectReaderFactory factory = new ObjectReaderFactory();
        Index rowIndex = new Index(0);
        Index colIndex = new Index(0);
        for (TagTree tree : tagTrees) {
            Tag tag = tree.getTag();
            ObjectReaderFactoryParameter param = new ObjectReaderFactoryParameter(
                    sheet, rowIndex, colIndex, tree, tag
            );
            ObjectReader reader = factory.create(param);
            obj = reader.read();
        }

        return obj;
    }
}
