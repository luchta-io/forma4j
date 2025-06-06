package io.luchta.forma4j.reader.excel;

import io.luchta.forma4j.context.databind.json.JsonNode;
import io.luchta.forma4j.context.databind.json.JsonNodes;
import io.luchta.forma4j.context.databind.json.JsonObject;
import io.luchta.forma4j.reader.excel.objectreader.ObjectReader;
import io.luchta.forma4j.reader.excel.objectreader.ObjectReaderFactory;
import io.luchta.forma4j.reader.excel.objectreader.ObjectReaderFactoryParameter;
import io.luchta.forma4j.reader.model.excel.Index;
import io.luchta.forma4j.reader.model.tag.*;
import io.luchta.forma4j.reader.specification.DefaultTagTreeSpec;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

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
        return read(inputStream, tagTree, null);
    }

    /**
     * EXCEL の読み込みを行うメソッドです。
     * <p>
     * 設定ファイルの内容に従って読み込みを行います。パスワードが {@code NULL} またはブランクの場合はパスワードなしとして読み込みを行います。
     * </p>
     * @param inputStream 読み込みを行う EXCEL ファイル
     * @param tagTree EXCEL の読み込み定義を記述した設定ファイルをツリー構造に変換したオブジェクト
     * @param password パスワード
     * @return 読み込んだ EXCEL ファイルを JSON 形式に変換したオブジェクト
     * @throws IOException
     */
    public JsonObject read(InputStream inputStream, TagTree tagTree, String password) throws IOException {
        JsonNodes nodes = new JsonNodes();
        try (Workbook workbook = WorkbookFactory.create(inputStream, password)) {
            for (TagTree tree : tagTree.getChildren()) {
                Tag tag = tree.getTag();
                if (tag.isSheet()) {
                    JsonObject obj = read(workbook, tree);

                    if (obj.isJsonNodes()) {
                        nodes.addAll((JsonNodes) obj.getValue());
                    } else if (obj.isJsonNode()) {
                        nodes.add((JsonNode) obj.getValue());
                    }
                }
            }
        }

        JsonNode node = new JsonNode();
        switch (nodes.size()) {
            case 0: node.putVar(FormaReaderTag.TAG_NAME, new JsonObject()); break;
            case 1: node.putVar(FormaReaderTag.TAG_NAME, new JsonObject(nodes.get(0))); break;
            default: node.putVar(FormaReaderTag.TAG_NAME, new JsonObject(nodes)); break;
        }

        JsonObject resultValue = new JsonObject(node);
        return resultValue;
    }

    private JsonObject read(Workbook workbook, TagTree tagTree) {
        SheetTag sheetTag = (SheetTag) tagTree.getTag();

        JsonObject result = null;
        if (sheetTag.readAllSheets()) {
            result = readAllSheets(workbook, tagTree);
        } else {
            Sheet sheet = sheetTag.hasName() ?
                    workbook.getSheet(sheetTag.name().toString()) :
                    workbook.getSheetAt(sheetTag.index().toInteger());
            result = readSingleSheet(sheet, tagTree);
        }

        return result;
    }

    private JsonObject readSingleSheet(Sheet sheet, TagTree tagTree) {
        JsonObject obj = read(sheet, tagTree.getChildren());
        JsonNode node = new JsonNode();
        node.putVar(sheet.getSheetName(), obj);

        return new JsonObject(node);
    }

    private JsonObject readAllSheets(Workbook workbook, TagTree tagTree) {
        JsonNodes nodes = new JsonNodes();
        int numberOfSheets = workbook.getNumberOfSheets();
        for (int i = 0; i < numberOfSheets; i++) {
            Sheet sheet = workbook.getSheetAt(i);

            JsonObject obj = read(sheet, tagTree.getChildren());
            JsonNode node = new JsonNode();
            node.putVar(sheet.getSheetName(), obj);
            nodes.add(node);
        }

        return new JsonObject(nodes);
    }

    private JsonObject read(Sheet sheet, TagTrees tagTrees) {
        ObjectReaderFactory factory = new ObjectReaderFactory();
        Index rowIndex = new Index(0);
        Index colIndex = new Index(0);
        JsonNode node = new JsonNode();
        for (TagTree tree : tagTrees) {
            Tag tag = tree.getTag();
            ObjectReaderFactoryParameter param = new ObjectReaderFactoryParameter(
                    sheet, rowIndex, colIndex, tree, tag
            );
            ObjectReader reader = factory.create(param);
            node.putJsonNode(reader.read());
        }

        return new JsonObject(node);
    }
}
