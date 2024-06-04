package io.luchta.forma4j.writer.cell;

import io.luchta.forma4j.context.databind.json.JsonNode;
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
 * cellタグに関するテストクラス
 */
public class CellTest {
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
        InputStream readerConfig = classLoader.getResource("writer/cell/cell_check.xml").openStream();
        InputStream outputExcel = new FileInputStream(absolutePath);
        FormaReader reader = new FormaReader();
        JsonObject obj = reader.read(readerConfig, outputExcel);

        Object root = obj.getValue();
        Assertions.assertEquals(true, root instanceof JsonNode);

        JsonObject sheet = ((JsonNode) root).getVar("forma");
        Assertions.assertEquals(true, sheet.getValue() instanceof JsonNode);

        JsonObject cell = ((JsonNode) sheet.getValue()).getVar("test");
        Assertions.assertEquals(true, cell.getValue() instanceof JsonNode);

        JsonNode value = ((JsonNode) cell.getValue());
        Assertions.assertEquals("あいうえお", value.getVar("value").getValue().toString());
    }
}
