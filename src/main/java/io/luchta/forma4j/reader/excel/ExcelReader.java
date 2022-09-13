package io.luchta.forma4j.reader.excel;

import io.luchta.forma4j.context.databind.json.JsonNode;
import io.luchta.forma4j.context.databind.json.JsonNodes;
import io.luchta.forma4j.context.databind.json.JsonObject;
import io.luchta.forma4j.reader.excel.objectreader.ObjectReader;
import io.luchta.forma4j.reader.excel.objectreader.ObjectReaderFactory;
import io.luchta.forma4j.reader.excel.objectreader.ObjectReaderFactoryParameter;
import io.luchta.forma4j.reader.model.excel.Index;
import io.luchta.forma4j.reader.model.tag.SheetTag;
import io.luchta.forma4j.reader.model.tag.Tag;
import io.luchta.forma4j.reader.model.tag.TagTree;
import io.luchta.forma4j.reader.model.tag.TagTrees;
import io.luchta.forma4j.reader.specification.DefaultTagTreeSpec;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.IOException;
import java.io.InputStream;

/**
 * {@code ExcelReader} は EXCEL の読み込みを行うクラスです。
 *
 * @since 0.1.0
 */
public class ExcelReader {
    /**
     * EXCEL の読み込みを行うメソッドです。
     * <p>
     * 設定ファイルなしで読み込みを行います。デフォルトの設定ファイルの内容を生成してから読み込みを行います。読み込み処理は {@link ExcelReader#read(Sheet, TagTrees)} メソッドを呼び出して行います。
     * </p>
     * @param inputStream 読み込みを行う EXCEL ファイル
     * @return 読み込んだ EXCEL ファイルを JSON 形式に変換したオブジェクト
     * @throws IOException
     */
    public JsonObject read(InputStream inputStream) throws IOException {
        DefaultTagTreeSpec defaultTagTreeSpec = new DefaultTagTreeSpec();
        TagTree tagTree = defaultTagTreeSpec.create();
        return read(inputStream, tagTree);
    }

    /**
     * EXCEL の読み込みを行うメソッドです。
     * <p>
     * 設定ファイルの内容に従って読み込みを行います。
     * </p>
     * @param inputStream 読み込みを行う EXCEL ファイル
     * @param tagTree EXCEL の読み込み定義を記述した設定ファイルをツリー構造に変換したオブジェクト
     * @return 読み込んだ EXCEL ファイルを JSON 形式に変換したオブジェクト
     * @throws IOException
     */
    public JsonObject read(InputStream inputStream, TagTree tagTree) throws IOException {
        JsonNodes nodes = new JsonNodes();
        Workbook workbook = WorkbookFactory.create(inputStream);
        for (TagTree tree : tagTree.getChildren()) {
            Tag tag = tree.getTag();
            if (tag.isSheet()) {
                JsonObject obj = read(workbook, tree);

                JsonNode node = new JsonNode();
                node.putVar("forma-reader", obj);

                nodes.add(node);
            }
        }

        switch (nodes.size()) {
            case 0: return new JsonObject();
            case 1: return new JsonObject(nodes.get(0));
            default: return new JsonObject(nodes);
        }
    }

    private JsonObject read(Workbook workbook, TagTree tagTree) {
        SheetTag sheetTag = (SheetTag) tagTree.getTag();

        JsonObject result = null;
        if (sheetTag.readAllSheets()) {
            JsonNodes nodes = new JsonNodes();
            int numberOfSheets = workbook.getNumberOfSheets();
            for (int i = 0; i < numberOfSheets; i++) {
                Sheet sheet = workbook.getSheetAt(i);

                JsonObject obj = read(sheet, tagTree.getChildren());
                JsonNode node = new JsonNode();
                node.putVar(sheet.getSheetName(), obj);
                nodes.add(node);
            }

            result = new JsonObject(nodes);
        } else {
            Sheet sheet = workbook.getSheet(sheetTag.name().toString());

            JsonObject obj = read(sheet, tagTree.getChildren());
            JsonNode node = new JsonNode();
            node.putVar(sheetTag.name().toString(), obj);

            result = new JsonObject(node);
        }

        return result;
    }

    private JsonObject read(Sheet sheet, TagTrees tagTrees) {
        ObjectReaderFactory factory = new ObjectReaderFactory();
        Index rowIndex = new Index(0);
        Index colIndex = new Index(0);
        JsonNodes nodes = new JsonNodes();
        for (TagTree tree : tagTrees) {
            Tag tag = tree.getTag();
            ObjectReaderFactoryParameter param = new ObjectReaderFactoryParameter(
                    sheet, rowIndex, colIndex, tree, tag
            );
            ObjectReader reader = factory.create(param);
            nodes.add(reader.read());
        }

        if (nodes.size() > 1) {
            return new JsonObject(nodes);
        }
        return new JsonObject(nodes.get(0));
    }
}
