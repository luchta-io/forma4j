package io.luchta.forma4j.writer.row;

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
 * rowタグに関するテスト
 */
public class RowTest {
    static final Logger logger = Logger.getLogger(RowTest.class.getName());

    /**
     * rowタグを使用してcellに値を出力するテスト
     * @throws Exception
     */
    @Test
    void row_cell() throws Exception {
        // 定義ファイルの読み込み
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream in = classLoader.getResource("writer/row/row_cell.xml").openStream();

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
        InputStream compared = classLoader.getResource("writer/row/row_cell.xlsx").openStream();
        JsonObject jsonObject = differ.diff(comparing, compared);
        logger.log(Level.INFO, "比較結果: " + new JsonSerializer().serializeFromJsonObject(jsonObject));

        Assertions.assertEquals(true, jsonObject.isJsonNodes());
        Assertions.assertEquals(0, ((JsonNodes) jsonObject.getValue()).size());
    }

    /**
     * rowタグを使用して複数のcellに値を出力するテスト
     * @throws Exception
     */
    @Test
    void row_cells() throws Exception {
        // 定義ファイルの読み込み
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream in = classLoader.getResource("writer/row/row_cells.xml").openStream();

        // 出力ファイルの設定
        File outFile = Files.createTempFile("test", String.format("%s.xlsx", LocalDateTime.now().toString())).toFile();
        FileOutputStream out = new FileOutputStream(outFile);
        String absolutePath = outFile.getAbsolutePath();
        logger.log(Level.INFO, "xlsxファイル出力先: " + absolutePath);

        // 書き込む内容の設定
        JsonNode jsonNode = new JsonNode();
        jsonNode.putVar("value1", new JsonObject("あいうえお"));
        jsonNode.putVar("value2", new JsonObject("かきくけこ"));
        jsonNode.putVar("value3", new JsonObject("さしすせそ"));

        // 書き込み
        FormaWriter sut = new FormaWriter();
        sut.write(in, out, new JsonObject(jsonNode));

        // 書き込んだ内容のチェック
        FormaDiffer differ = new FormaDiffer();
        FileInputStream comparing = new FileInputStream(absolutePath);
        InputStream compared = classLoader.getResource("writer/row/row_cells.xlsx").openStream();
        JsonObject jsonObject = differ.diff(comparing, compared);
        logger.log(Level.INFO, "比較結果: " + new JsonSerializer().serializeFromJsonObject(jsonObject));

        Assertions.assertEquals(true, jsonObject.isJsonNodes());
        Assertions.assertEquals(0, ((JsonNodes) jsonObject.getValue()).size());
    }

    /**
     * rowタグを使用して複数行のcellに値を出力するテスト
     * @throws Exception
     */
    @Test
    void rows_cell() throws Exception {
        // 定義ファイルの読み込み
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream in = classLoader.getResource("writer/row/rows_cell.xml").openStream();

        // 出力ファイルの設定
        File outFile = Files.createTempFile("test", String.format("%s.xlsx", LocalDateTime.now().toString())).toFile();
        FileOutputStream out = new FileOutputStream(outFile);
        String absolutePath = outFile.getAbsolutePath();
        logger.log(Level.INFO, "xlsxファイル出力先: " + absolutePath);

        // 書き込む内容の設定
        JsonNode jsonNode = new JsonNode();
        jsonNode.putVar("value1", new JsonObject("あいうえお"));
        jsonNode.putVar("value2", new JsonObject("かきくけこ"));
        jsonNode.putVar("value3", new JsonObject("さしすせそ"));

        // 書き込み
        FormaWriter sut = new FormaWriter();
        sut.write(in, out, new JsonObject(jsonNode));

        // 書き込んだ内容のチェック
        FormaDiffer differ = new FormaDiffer();
        FileInputStream comparing = new FileInputStream(absolutePath);
        InputStream compared = classLoader.getResource("writer/row/rows_cell.xlsx").openStream();
        JsonObject jsonObject = differ.diff(comparing, compared);
        logger.log(Level.INFO, "比較結果: " + new JsonSerializer().serializeFromJsonObject(jsonObject));

        Assertions.assertEquals(true, jsonObject.isJsonNodes());
        Assertions.assertEquals(0, ((JsonNodes) jsonObject.getValue()).size());
    }

    /**
     * rowタグを使用して複数行複数のcellに値を出力するテスト
     * @throws Exception
     */
    @Test
    void rows_cells() throws Exception {
        // 定義ファイルの読み込み
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream in = classLoader.getResource("writer/row/rows_cells.xml").openStream();

        // 出力ファイルの設定
        File outFile = Files.createTempFile("test", String.format("%s.xlsx", LocalDateTime.now().toString())).toFile();
        FileOutputStream out = new FileOutputStream(outFile);
        String absolutePath = outFile.getAbsolutePath();
        logger.log(Level.INFO, "xlsxファイル出力先: " + absolutePath);

        // 書き込む内容の設定
        JsonNode jsonNode = new JsonNode();
        jsonNode.putVar("value1-1", new JsonObject("あいうえお"));
        jsonNode.putVar("value1-2", new JsonObject("かきくけこ"));
        jsonNode.putVar("value2-1", new JsonObject("さしすせそ"));
        jsonNode.putVar("value2-2", new JsonObject("たちつてと"));
        jsonNode.putVar("value3-1", new JsonObject("なにぬねの"));
        jsonNode.putVar("value3-2", new JsonObject("はひふへほ"));

        // 書き込み
        FormaWriter sut = new FormaWriter();
        sut.write(in, out, new JsonObject(jsonNode));

        // 書き込んだ内容のチェック
        FormaDiffer differ = new FormaDiffer();
        FileInputStream comparing = new FileInputStream(absolutePath);
        InputStream compared = classLoader.getResource("writer/row/rows_cells.xlsx").openStream();
        JsonObject jsonObject = differ.diff(comparing, compared);
        logger.log(Level.INFO, "比較結果: " + new JsonSerializer().serializeFromJsonObject(jsonObject));

        Assertions.assertEquals(true, jsonObject.isJsonNodes());
        Assertions.assertEquals(0, ((JsonNodes) jsonObject.getValue()).size());
    }
}
