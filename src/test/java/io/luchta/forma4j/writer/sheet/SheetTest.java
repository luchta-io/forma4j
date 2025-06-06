package io.luchta.forma4j.writer.sheet;

import io.luchta.forma4j.context.databind.convert.JsonSerializer;
import io.luchta.forma4j.context.databind.json.JsonNode;
import io.luchta.forma4j.context.databind.json.JsonNodes;
import io.luchta.forma4j.context.databind.json.JsonObject;
import io.luchta.forma4j.reader.FormaReader;
import io.luchta.forma4j.writer.FormaWriter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import util.diff.FormaDiffer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SheetTest {
    private static final Logger logger = Logger.getLogger(SheetTest.class.getName());

    /**
     * collectionプロパティのテスト
     * @throws Exception
     */
    @Test
    public void sheet_collection() throws Exception {
        // 定義ファイルの読み込み
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream in = classLoader.getResource("writer/sheet/sheet_collection.xml").openStream();

        // 出力ファイルの設定
        File outFile = Files.createTempFile("test", String.format("%s.xlsx", LocalDateTime.now().toString())).toFile();
        FileOutputStream out = new FileOutputStream(outFile);
        String absolutePath = outFile.getAbsolutePath();
        logger.log(Level.INFO, "xlsxファイル出力先: " + absolutePath);

        // 書き込む内容の設定
        JsonNode rootNode = new JsonNode();

        JsonNode sheetNode1 = new JsonNode();
        sheetNode1.putVar("sheetName", new JsonObject("シート名１"));
        sheetNode1.putVar("キー１", new JsonObject("あいうえお"));
        sheetNode1.putVar("キー２", new JsonObject("かきくけこ"));
        sheetNode1.putVar("キー３", new JsonObject("さしすせそ"));
        sheetNode1.putVar("キー４", new JsonObject("たちつてと"));

        JsonNode sheetNode2 = new JsonNode();
        sheetNode2.putVar("sheetName", new JsonObject("シート名２"));
        sheetNode2.putVar("キー１", new JsonObject("なにぬねの"));
        sheetNode2.putVar("キー２", new JsonObject("はひふへほ"));
        sheetNode2.putVar("キー３", new JsonObject("まみむめも"));
        sheetNode2.putVar("キー４", new JsonObject("やゆよ"));

        JsonNodes sheetListNode = new JsonNodes();
        sheetListNode.add(sheetNode1);
        sheetListNode.add(sheetNode2);

        rootNode.putVar("データリスト", new JsonObject(sheetListNode));

        // 書き込み
        FormaWriter sut = new FormaWriter();
        sut.write(in, out, new JsonObject(rootNode));

        // 書き込んだ内容のチェック
        FormaDiffer differ = new FormaDiffer();
        FileInputStream comparing = new FileInputStream(absolutePath);
        InputStream compared = classLoader.getResource("writer/sheet/sheet_collection.xlsx").openStream();
        JsonObject jsonObject = differ.diff(comparing, compared);
        logger.log(Level.INFO, "比較結果: " + new JsonSerializer().serializeFromJsonObject(jsonObject));

        Assertions.assertEquals(true, jsonObject.isJsonNodes());
        Assertions.assertEquals(0, ((JsonNodes) jsonObject.getValue()).size());
    }
}
