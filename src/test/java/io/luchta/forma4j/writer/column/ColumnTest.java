package io.luchta.forma4j.writer.column;

import io.luchta.forma4j.context.databind.json.JsonNode;
import io.luchta.forma4j.context.databind.json.JsonNodes;
import io.luchta.forma4j.context.databind.json.JsonObject;
import io.luchta.forma4j.reader.FormaReader;
import io.luchta.forma4j.writer.FormaWriter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * columnタグに関するテスト
 */
public class ColumnTest {
    static final Logger logger = Logger.getLogger(ColumnTest.class.getName());

    /**
     * columnタグを使用してcellに値を出力するテスト
     * @throws Exception
     */
    @Test
    void column_cell() throws Exception {
        // 定義ファイルの読み込み
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream in = classLoader.getResource("writer/column/column_cell.xml").openStream();

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
        InputStream readerConfig = classLoader.getResource("writer/column/column_cell_check.xml").openStream();
        InputStream outputExcel = new FileInputStream(absolutePath);
        FormaReader reader = new FormaReader();
        JsonObject obj = reader.read(readerConfig, outputExcel);

        Object root = obj.getValue();
        Assertions.assertEquals(true, root instanceof JsonNode);

        JsonObject sheet = ((JsonNode) root).getVar("forma");
        Assertions.assertEquals(true, sheet.getValue() instanceof JsonNode);

        JsonObject cell = ((JsonNode) sheet.getValue()).getVar("test");
        Assertions.assertEquals(true, cell.getValue() instanceof JsonNode);

        JsonNode values = ((JsonNode) cell.getValue());
        Assertions.assertEquals("あいうえお", values.getVar("value").getValue().toString());
    }

    /**
     * columnタグを使用して複数のcellに値を出力するテスト
     * @throws Exception
     */
    @Test
    void column_cells() throws Exception {
        // 定義ファイルの読み込み
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream in = classLoader.getResource("writer/column/column_cells.xml").openStream();

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
        InputStream readerConfig = classLoader.getResource("writer/column/column_cells_check.xml").openStream();
        InputStream outputExcel = new FileInputStream(absolutePath);
        FormaReader reader = new FormaReader();
        JsonObject obj = reader.read(readerConfig, outputExcel);

        Object root = obj.getValue();
        Assertions.assertEquals(true, root instanceof JsonNode);

        JsonObject sheet = ((JsonNode) root).getVar("forma");
        Assertions.assertEquals(true, sheet.getValue() instanceof JsonNode);

        JsonObject cell = ((JsonNode) sheet.getValue()).getVar("test");
        Assertions.assertEquals(true, cell.getValue() instanceof JsonNodes);

        JsonNodes values = ((JsonNodes) cell.getValue());
        Assertions.assertEquals("あいうえお", values.get(0).getVar("value1").getValue().toString());
        Assertions.assertEquals("かきくけこ", values.get(1).getVar("value2").getValue().toString());
        Assertions.assertEquals("さしすせそ", values.get(2).getVar("value3").getValue().toString());
    }

    /**
     * columnタグを使用して複数列のcellに値を出力するテスト
     * @throws Exception
     */
    @Test
    void columns_cell() throws Exception {
        // 定義ファイルの読み込み
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream in = classLoader.getResource("writer/column/columns_cell.xml").openStream();

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
        InputStream readerConfig = classLoader.getResource("writer/column/columns_cell_check.xml").openStream();
        InputStream outputExcel = new FileInputStream(absolutePath);
        FormaReader reader = new FormaReader();
        JsonObject obj = reader.read(readerConfig, outputExcel);

        Object root = obj.getValue();
        Assertions.assertEquals(true, root instanceof JsonNode);

        JsonObject sheet = ((JsonNode) root).getVar("forma");
        Assertions.assertEquals(true, sheet.getValue() instanceof JsonNode);

        JsonObject cell = ((JsonNode) sheet.getValue()).getVar("test");
        Assertions.assertEquals(true, cell.getValue() instanceof JsonNodes);

        JsonNodes values = ((JsonNodes) cell.getValue());
        Assertions.assertEquals("あいうえお", values.get(0).getVar("value1").getValue().toString());
        Assertions.assertEquals("かきくけこ", values.get(1).getVar("value2").getValue().toString());
        Assertions.assertEquals("さしすせそ", values.get(2).getVar("value3").getValue().toString());
    }

    /**
     * columnタグを使用して複数列の複数cellに値を出力するテスト
     * @throws Exception
     */
    @Test
    void columns_cells() throws Exception {
        // 定義ファイルの読み込み
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream in = classLoader.getResource("writer/column/columns_cells.xml").openStream();

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
        InputStream readerConfig = classLoader.getResource("writer/column/columns_cells_check.xml").openStream();
        InputStream outputExcel = new FileInputStream(absolutePath);
        FormaReader reader = new FormaReader();
        JsonObject obj = reader.read(readerConfig, outputExcel);

        Object root = obj.getValue();
        Assertions.assertEquals(true, root instanceof JsonNode);

        JsonObject sheet = ((JsonNode) root).getVar("forma");
        Assertions.assertEquals(true, sheet.getValue() instanceof JsonNode);

        JsonObject cell = ((JsonNode) sheet.getValue()).getVar("test");
        Assertions.assertEquals(true, cell.getValue() instanceof JsonNodes);

        JsonNodes values = ((JsonNodes) cell.getValue());
        Assertions.assertEquals("あいうえお", values.get(0).getVar("value1-1").getValue().toString());
        Assertions.assertEquals("かきくけこ", values.get(1).getVar("value1-2").getValue().toString());
        Assertions.assertEquals("さしすせそ", values.get(2).getVar("value2-1").getValue().toString());
        Assertions.assertEquals("たちつてと", values.get(3).getVar("value2-2").getValue().toString());
        Assertions.assertEquals("なにぬねの", values.get(4).getVar("value3-1").getValue().toString());
        Assertions.assertEquals("はひふへほ", values.get(5).getVar("value3-2").getValue().toString());
    }
}
