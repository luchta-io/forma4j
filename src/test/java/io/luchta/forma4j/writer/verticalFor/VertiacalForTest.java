package io.luchta.forma4j.writer.verticalFor;

import io.luchta.forma4j.context.databind.convert.JsonSerializer;
import io.luchta.forma4j.context.databind.json.JsonNode;
import io.luchta.forma4j.context.databind.json.JsonNodes;
import io.luchta.forma4j.context.databind.json.JsonObject;
import io.luchta.forma4j.reader.FormaReader;
import io.luchta.forma4j.writer.FormaWriter;
import io.luchta.forma4j.writer.definition.schema.element.HorizontalFor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import util.diff.FormaDiffer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * vertical-forタグに関するテスト
 */
public class VertiacalForTest {
    private static final Logger logger = Logger.getLogger(VertiacalForTest.class.getName());

    /**
     * vertical-forタグを使用してcellへ値を出力するテスト
     * <p>
     * collectionにList<String>をセットして出力確認を行う。
     * </p>
     * @throws Exception
     */
    @ParameterizedTest
    @CsvSource({
            "writer/verticalFor/vertical_for_cell_set_list.xml, writer/verticalFor/vertical_for_cell_set_list.xlsx",
            "writer/verticalFor/vertical_for_cell_set_list_3_5.xml, writer/verticalFor/vertical_for_cell_set_list_3_5.xlsx"
    })
    public void vertical_for_cell_set_jsonnodes(String configPath, String checkXlsxPath) throws Exception {
        // 定義ファイルの読み込み
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream in = classLoader.getResource(configPath).openStream();

        // 出力ファイルの設定
        File outFile = Files.createTempFile("test", String.format("%s.xlsx", LocalDateTime.now().toString())).toFile();
        FileOutputStream out = new FileOutputStream(outFile);
        String absolutePath = outFile.getAbsolutePath();
        logger.info("xlsxファイル出力先: " + absolutePath);

        // 書き込む内容の設定
        JsonNode jsonNode = new JsonNode();
        jsonNode.putVar("list", new JsonObject(Arrays.asList("あいうえお", "かきくけこ", "さしすせそ")));

        // 書き込み
        FormaWriter sut = new FormaWriter();
        sut.write(in, out, new JsonObject(jsonNode));

        // 書き込んだ内容のチェック
        FormaDiffer differ = new FormaDiffer();
        FileInputStream comparing = new FileInputStream(absolutePath);
        InputStream compared = classLoader.getResource(checkXlsxPath).openStream();
        JsonObject jsonObject = differ.diff(comparing, compared);
        logger.log(Level.INFO, "比較結果: " + new JsonSerializer().serializeFromJsonObject(jsonObject));

        Assertions.assertEquals(true, jsonObject.isJsonNodes());
        Assertions.assertEquals(0, ((JsonNodes) jsonObject.getValue()).size());
    }

    /**
     * vertical-forタグを使用してcellへ値を出力するテスト
     * <p>
     * collectionにJsonNodesをセットして出力確認を行う。
     * </p>
     * @throws Exception
     */
    @ParameterizedTest
    @CsvSource({
            "writer/verticalFor/vertical_for_cell_set_jsonnodes.xml, writer/verticalFor/vertical_for_cell_set_jsonnodes.xlsx",
            "writer/verticalFor/vertical_for_cell_set_jsonnodes_3_5.xml, writer/verticalFor/vertical_for_cell_set_jsonnodes_3_5.xlsx"
    })
    public void vertical_for_cell_set_jsonnodes_to_collection(String configPath, String checkXlsxPath) throws Exception {
        // 定義ファイルの読み込み
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream in = classLoader.getResource(configPath).openStream();

        // 出力ファイルの設定
        File outFile = Files.createTempFile("test", String.format("%s.xlsx", LocalDateTime.now().toString())).toFile();
        FileOutputStream out = new FileOutputStream(outFile);
        String absolutePath = outFile.getAbsolutePath();
        logger.info("xlsxファイル出力先: " + absolutePath);

        // 書き込む内容の設定
        JsonNodes jsonNodes = new JsonNodes();
        JsonNode jsonNode1 = new JsonNode();
        jsonNode1.putVar("key1", new JsonObject("あいうえお"));
        jsonNode1.putVar("key2", new JsonObject("かきくけこ"));
        jsonNode1.putVar("key3", new JsonObject("さしすせそ"));
        jsonNodes.add(jsonNode1);

        JsonNode jsonNode2 = new JsonNode();
        jsonNode2.putVar("key1", new JsonObject("たちつてと"));
        jsonNode2.putVar("key2", new JsonObject("なにぬねの"));
        jsonNode2.putVar("key3", new JsonObject("はひふへほ"));
        jsonNodes.add(jsonNode2);

        JsonNode outputData = new JsonNode();
        outputData.putVar("list", new JsonObject(jsonNodes));

        // 書き込み
        FormaWriter sut = new FormaWriter();
        sut.write(in, out, new JsonObject(outputData));

        // 書き込んだ内容のチェック
        FormaDiffer differ = new FormaDiffer();
        FileInputStream comparing = new FileInputStream(absolutePath);
        InputStream compared = classLoader.getResource(checkXlsxPath).openStream();
        JsonObject jsonObject = differ.diff(comparing, compared);
        logger.log(Level.INFO, "比較結果: " + new JsonSerializer().serializeFromJsonObject(jsonObject));

        Assertions.assertEquals(true, jsonObject.isJsonNodes());
        Assertions.assertEquals(0, ((JsonNodes) jsonObject.getValue()).size());
    }

    /**
     * vertical-forタグが取れない子タグをセットしてエラーとなることを確認する
     * @throws Exception
     */
    @ParameterizedTest
    @CsvSource({
            "writer/verticalFor/vertical_for_error.xml",
    })
    public void vertical_for_error(String configPath) throws Exception {
        // 定義ファイルの読み込み
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream in = classLoader.getResource(configPath).openStream();

        // 出力ファイルの設定
        File outFile = Files.createTempFile("test", String.format("%s.xlsx", LocalDateTime.now().toString())).toFile();
        FileOutputStream out = new FileOutputStream(outFile);
        String absolutePath = outFile.getAbsolutePath();
        logger.info("xlsxファイル出力先: " + absolutePath);

        // 書き込む内容の設定
        JsonNode jsonNode = new JsonNode();
        jsonNode.putVar("list", new JsonObject(Arrays.asList("あいうえお", "かきくけこ", "さしすせそ")));

        // チェック
        FormaWriter sut = new FormaWriter();
        IllegalStateException exception = Assertions.assertThrows(IllegalStateException.class, () -> sut.write(in, out, new JsonObject(jsonNode)));
        Assertions.assertEquals("vertical-forの子タグにはrowタグのみが設定可能です", exception.getMessage());
    }
}
