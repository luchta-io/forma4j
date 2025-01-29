package io.luchta.forma4j.writer.horizontalFor;

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
 * horizontal-forタグに関するテスト
 */
public class HorizontalForTest {
    private static final Logger logger = Logger.getLogger(HorizontalForTest.class.getName());

    /**
     * horizontal-forタグを使用してcellへ値を出力するテスト
     * <p>
     * collectionにList<String>をセットして出力確認を行う。ループの開始位置はrowIndex=0, columnIndex=0。
     * </p>
     * @throws Exception
     */
    @ParameterizedTest
    @CsvSource({
            "writer/horizontalFor/horizontal_for_cell_set_list.xml, writer/horizontalFor/horizontal_for_cell_set_list.xlsx",
            "writer/horizontalFor/horizontal_for_cell_set_list_3_5.xml, writer/horizontalFor/horizontal_for_cell_set_list_3_5.xlsx"
    })
    public void horizontal_for_cell_set_jsonnodes(String configPath, String checkConfigPath) throws Exception {
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
        InputStream compared = classLoader.getResource(checkConfigPath).openStream();
        JsonObject jsonObject = differ.diff(comparing, compared);
        logger.log(Level.INFO, "比較結果: " + new JsonSerializer().serializeFromJsonObject(jsonObject));

        Assertions.assertEquals(true, jsonObject.isJsonNodes());
        Assertions.assertEquals(0, ((JsonNodes) jsonObject.getValue()).size());
    }

    /**
     * horizontal-forタグを使用してcellへ値を出力するテスト
     * <p>
     * collectionにJsonNodesをセットして出力確認を行う。
     * </p>
     * @throws Exception
     */
    @ParameterizedTest
    @CsvSource({
            "writer/horizontalFor/horizontal_for_cell_set_jsonnodes.xml, writer/horizontalFor/horizontal_for_cell_set_jsonnodes.xlsx",
            "writer/horizontalFor/horizontal_for_cell_set_jsonnodes_3_5.xml, writer/horizontalFor/horizontal_for_cell_set_jsonnodes_3_5.xlsx"
    })
    public void horizontal_for_cell_set_jsonnodes_to_collection(String configPath, String checkConfigPath) throws Exception {
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
        InputStream compared = classLoader.getResource(checkConfigPath).openStream();
        JsonObject jsonObject = differ.diff(comparing, compared);
        logger.log(Level.INFO, "比較結果: " + new JsonSerializer().serializeFromJsonObject(jsonObject));

        Assertions.assertEquals(true, jsonObject.isJsonNodes());
        Assertions.assertEquals(0, ((JsonNodes) jsonObject.getValue()).size());
    }

    /**
     * horizontal-forタグを使用してcellへ値を出力するテスト
     * <p>
     * vertical-forタグの子孫要素としてhorizontal-forを設定して出力確認を行う。
     * </p>
     * @throws Exception
     */
    @ParameterizedTest
    @CsvSource({
            "writer/horizontalFor/horizontal_for_with_vertical_for.xml, writer/horizontalFor/horizontal_for_with_vertical_for.xlsx",
            "writer/horizontalFor/horizontal_for_with_vertical_for_3_5.xml, writer/horizontalFor/horizontal_for_with_vertical_for_3_5.xlsx"
    })
    public void horizontal_for_with_vertical_for(String configPath, String checkConfigPath) throws Exception {
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

        JsonNodes items1 = new JsonNodes();
        JsonNode item1 = new JsonNode();
        item1.putVar("value", new JsonObject("あいうえお"));
        items1.add(item1);
        JsonNode item2 = new JsonNode();
        item2.putVar("value", new JsonObject("かきくけこ"));
        items1.add(item2);
        JsonNode item3 = new JsonNode();
        item3.putVar("value", new JsonObject("さしすせそ"));
        items1.add(item3);

        JsonNode data1 = new JsonNode();
        data1.putVar("items", new JsonObject(items1));
        jsonNodes.add(data1);

        JsonNodes items2 = new JsonNodes();
        JsonNode item4 = new JsonNode();
        item4.putVar("value", new JsonObject("たちつてと"));
        items2.add(item4);
        JsonNode item5 = new JsonNode();
        item5.putVar("value", new JsonObject("なにぬねの"));
        items2.add(item5);
        JsonNode item6 = new JsonNode();
        item6.putVar("value", new JsonObject("はひふへほ"));
        items2.add(item6);

        JsonNode data2 = new JsonNode();
        data2.putVar("items", new JsonObject(items2));
        jsonNodes.add(data2);

        JsonNode outputData = new JsonNode();
        outputData.putVar("list", new JsonObject(jsonNodes));

        // 書き込み
        FormaWriter sut = new FormaWriter();
        sut.write(in, out, new JsonObject(outputData));

        // 書き込んだ内容のチェック
        FormaDiffer differ = new FormaDiffer();
        FileInputStream comparing = new FileInputStream(absolutePath);
        InputStream compared = classLoader.getResource(checkConfigPath).openStream();
        JsonObject jsonObject = differ.diff(comparing, compared);
        logger.log(Level.INFO, "比較結果: " + new JsonSerializer().serializeFromJsonObject(jsonObject));

        Assertions.assertEquals(true, jsonObject.isJsonNodes());
        Assertions.assertEquals(0, ((JsonNodes) jsonObject.getValue()).size());
    }
}
