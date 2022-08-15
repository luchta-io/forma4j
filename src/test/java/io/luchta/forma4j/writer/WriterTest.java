package io.luchta.forma4j.writer;

import io.luchta.forma4j.context.databind.json.JsonNode;
import io.luchta.forma4j.context.databind.json.JsonNodes;
import io.luchta.forma4j.context.databind.json.JsonObject;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WriterTest {
    static final Logger logger = Logger.getLogger(WriterTest.class.getName());

    @Test
    void sample() throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream in = classLoader.getResource("writer/一覧.xml").openStream();
        File outFile = Files.createTempFile("test", String.format("%s.xlsx", LocalDateTime.now().toString())).toFile();
        FileOutputStream out = new FileOutputStream(outFile);
        logger.log(Level.INFO, "xlsxファイル出力先: " + outFile.getAbsolutePath());

        JsonObject jsonObject = new JsonObject();

        JsonNode jsonNode = new JsonNode();
        jsonNode.putVar("出力日時", new JsonObject(LocalDate.now().toString()));
        jsonNode.putVar("可変項目名リスト", new JsonObject(可変項目名リスト()));
        jsonNode.putVar("データリスト", new JsonObject(データリスト()));

        Writer sut = new Writer();
        sut.write(in, out, new JsonObject(jsonNode));
    }

    private List<String> 可変項目名リスト() {
        return Arrays.asList("カテゴリ", "マイルストーン", "優先度");
    }

    private JsonNodes データリスト() {
        JsonNodes jsonNodes = new JsonNodes();
        JsonNode jsonNode1 = new JsonNode();
        jsonNode1.putVar("キー", new JsonObject("TEST\"-1"));
        jsonNode1.putVar("件名", new JsonObject("xx機能実装する"));
        jsonNode1.putVar("担当者", new JsonObject("ユー/ザA"));
        jsonNode1.putVar("状態", new JsonObject("処理\b中"));
        jsonNode1.putVar("可変項目リスト", new JsonObject(Arrays.asList("タス\rク", "v1.0.0.RELEASE", "1")));
        jsonNode1.putVar("更新日時", new JsonObject("2020/11\n/5"));
        jsonNode1.putVar("更新者", new JsonObject("ユーザ\tA"));
        jsonNode1.putVar("登録日時", new JsonObject("2020\\u0041/11/2"));
        jsonNode1.putVar("登録者", new JsonObject("ユーザB"));

        jsonNodes.add(jsonNode1);

        JsonNode jsonNode2 = new JsonNode();
        jsonNode2.putVar("キー", new JsonObject("TEST-2"));
        jsonNode2.putVar("件名", new JsonObject("yy機能を実装する"));
        jsonNode2.putVar("担当者", new JsonObject(""));
        jsonNode2.putVar("状態", new JsonObject("未対応"));
        jsonNode2.putVar("可変項目リスト", new JsonObject(Arrays.asList("", "", "")));
        jsonNode2.putVar("更新日時", new JsonObject("2020/11/2"));
        jsonNode2.putVar("更新者", new JsonObject("ユーザB"));
        jsonNode2.putVar("登録日時", new JsonObject("2020/11/2"));
        jsonNode2.putVar("登録者", new JsonObject("ユーザB"));

        jsonNodes.add(jsonNode2);

        return jsonNodes;
    }

    @Test
    void sample2() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream in = classLoader.getResource("writer/test.xml").openStream();
        File outFile = Files.createTempFile("test", String.format("%s.xlsx", LocalDateTime.now().toString())).toFile();
        FileOutputStream out = new FileOutputStream(outFile);
        logger.log(Level.INFO, "xlsxファイル出力先: " + outFile.getAbsolutePath());

        JsonNode jsonNode = new JsonNode();
        jsonNode.putVar("データリスト", new JsonObject(データリスト2()));

        Writer sut = new Writer();
        sut.write(in, out, new JsonObject(jsonNode));
    }

    private JsonNodes データリスト2() {
        JsonNodes jsonNodes = new JsonNodes();
        JsonNode jsonNode1 = new JsonNode();
        jsonNode1.putVar("キー", new JsonObject("TEST\"-1"));
        jsonNode1.putVar("件名", new JsonObject("xx機能実装する"));
        jsonNode1.putVar("担当者", new JsonObject("ユー/ザA"));
        jsonNode1.putVar("状態", new JsonObject("処理\b中"));
        jsonNode1.putVar("更新日時", new JsonObject("2020/11\n/5"));
        jsonNode1.putVar("更新者", new JsonObject("ユーザ\tA"));
        jsonNode1.putVar("登録日時", new JsonObject("2020\\u0041/11/2"));
        jsonNode1.putVar("登録者", new JsonObject("ユーザB"));

        jsonNodes.add(jsonNode1);

        JsonNode jsonNode2 = new JsonNode();
        jsonNode2.putVar("キー", new JsonObject("TEST-2"));
        jsonNode2.putVar("件名", new JsonObject("yy機能を実装する"));
        jsonNode2.putVar("担当者", new JsonObject(""));
        jsonNode2.putVar("状態", new JsonObject("未対応"));
        jsonNode2.putVar("更新日時", new JsonObject("2020/11/2"));
        jsonNode2.putVar("更新者", new JsonObject("ユーザB"));
        jsonNode2.putVar("登録日時", new JsonObject("2020/11/2"));
        jsonNode2.putVar("登録者", new JsonObject("ユーザB"));

        jsonNodes.add(jsonNode2);

        return jsonNodes;
    }

    @Test
    void sample3() throws Exception {
        File outFile = Files.createTempFile("test", String.format("%s.xlsx", LocalDateTime.now().toString())).toFile();
        FileOutputStream out = new FileOutputStream(outFile);
        logger.log(Level.INFO, "xlsxファイル出力先: " + outFile.getAbsolutePath());

        JsonNode jsonNode = new JsonNode();
        jsonNode.putVar("データリスト", new JsonObject(データリスト3()));

        Writer sut = new Writer();
        sut.write(out, new JsonObject(jsonNode));
    }

    private JsonNodes データリスト3() {
        JsonNodes jsonNodes = new JsonNodes();
        JsonNode jsonNode1 = new JsonNode();
        jsonNode1.putVar("キー", new JsonObject("TEST\"-1"));
        jsonNode1.putVar("件名", new JsonObject("xx機能実装する"));
        jsonNode1.putVar("担当者", new JsonObject("ユー/ザA"));
        jsonNode1.putVar("状態", new JsonObject("処理\b中"));
        jsonNode1.putVar("更新日時", new JsonObject("2020/11\n/5"));
        jsonNode1.putVar("更新者", new JsonObject("ユーザ\tA"));
        jsonNode1.putVar("登録日時", new JsonObject("2020\\u0041/11/2"));
        jsonNode1.putVar("登録者", new JsonObject("ユーザB"));

        jsonNodes.add(jsonNode1);

        JsonNode jsonNode2 = new JsonNode();
        jsonNode2.putVar("キー", new JsonObject("TEST-2"));
        jsonNode2.putVar("件名", new JsonObject("yy機能を実装する"));
        jsonNode2.putVar("担当者", new JsonObject(""));
        jsonNode2.putVar("状態", new JsonObject("未対応"));
        jsonNode2.putVar("更新日時", new JsonObject("2020/11/2"));
        jsonNode2.putVar("更新者", new JsonObject("ユーザB"));
        jsonNode2.putVar("登録日時", new JsonObject("2020/11/2"));
        jsonNode2.putVar("登録者", new JsonObject("ユーザB"));

        jsonNodes.add(jsonNode2);

        return jsonNodes;
    }
}
