package io.luchta.forma4j.writer.cell;

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

/**
 * cellタグに関するテストクラス
 */
public class CellTest {
    /** ロガー */
    static final Logger logger = Logger.getLogger(CellTest.class.getName());

    /**
     * rowIndexとcolumnIndexを指定してcellへ値を出力するテスト
     */
    @Test
    public void cell_test() throws Exception {
        // 定義ファイルの読み込み
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream in = classLoader.getResource("writer/cell/cell.xml").openStream();

        // 出力ファイルの設定
        File outFile = Files.createTempFile("test", String.format("%s.xlsx", LocalDateTime.now().toString())).toFile();
        FileOutputStream out = new FileOutputStream(outFile);
        String absolutePath = outFile.getAbsolutePath();
        logger.log(Level.INFO, "xlsxファイル出力先: " + absolutePath);

        // 書き込む内容の設定
        JsonNode jsonNode = new JsonNode();
        jsonNode.putVar("value", new JsonObject("あいうえお"));

        // 書き込み
        FormaWriter sut = new FormaWriter();
        sut.write(in, out, new JsonObject(jsonNode));

        // 書き込んだ内容のチェック
        FormaDiffer differ = new FormaDiffer();
        FileInputStream comparing = new FileInputStream(absolutePath);
        InputStream compared = classLoader.getResource("writer/cell/cell.xlsx").openStream();
        JsonObject jsonObject = differ.diff(comparing, compared);
        logger.log(Level.INFO, "比較結果: " + new JsonSerializer().serializeFromJsonObject(jsonObject));

        Assertions.assertEquals(true, jsonObject.isJsonNodes());
        Assertions.assertEquals(0, ((JsonNodes) jsonObject.getValue()).size());
    }

    /**
     * ドット記法を正しく解析し値を出力できることを確認するテスト
     */
    @Test
    public void cell_dot_notation_parsing_test() throws Exception {
        // 定義ファイルの読み込み
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream in = classLoader.getResource("writer/cell/cell_dot_notation_parsing_test.xml").openStream();

        // 出力ファイルの設定
        File outFile = Files.createTempFile("test", String.format("%s.xlsx", LocalDateTime.now().toString())).toFile();
        FileOutputStream out = new FileOutputStream(outFile);
        String absolutePath = outFile.getAbsolutePath();
        logger.log(Level.INFO, "xlsxファイル出力先: " + absolutePath);

        // 書き込む内容の設定
        JsonNode jsonNode = new JsonNode();
        JsonNode jsonNode1 = new JsonNode();
        JsonNode jsonNode2 = new JsonNode();
        jsonNode2.putVar("value", new JsonObject("FORMA4J-000001"));
        jsonNode1.putVar("id", new JsonObject(jsonNode2));
        jsonNode.putVar("employee", new JsonObject(jsonNode1));

        // 書き込み
        FormaWriter sut = new FormaWriter();
        sut.write(in, out, new JsonObject(jsonNode));

        // 書き込んだ内容のチェック
        FormaDiffer differ = new FormaDiffer();
        FileInputStream comparing = new FileInputStream(absolutePath);
        InputStream compared = classLoader.getResource("writer/cell/cell_dot_notation_parsing_test.xlsx").openStream();
        JsonObject jsonObject = differ.diff(comparing, compared);
        logger.log(Level.INFO, "比較結果: " + new JsonSerializer().serializeFromJsonObject(jsonObject));

        Assertions.assertEquals(true, jsonObject.isJsonNodes());
        Assertions.assertEquals(0, ((JsonNodes) jsonObject.getValue()).size());
    }
}
