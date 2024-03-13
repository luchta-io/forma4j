package io.luchta.forma4j.writer;

import io.luchta.forma4j.context.databind.json.JsonNode;
import io.luchta.forma4j.context.databind.json.JsonNodes;
import io.luchta.forma4j.context.databind.json.JsonObject;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FormaWriterTest {
    static final Logger logger = Logger.getLogger(FormaWriterTest.class.getName());

    /**
     * 書き込みのテスト
     * @throws IOException
     */
    @Test
    void sample() throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream in = classLoader.getResource("writer/一覧.xml").openStream();
        File outFile = Files.createTempFile("test", String.format("%s.xlsx", LocalDateTime.now().toString())).toFile();
        FileOutputStream out = new FileOutputStream(outFile);
        logger.log(Level.INFO, "xlsxファイル出力先: " + outFile.getAbsolutePath());

        JsonNode jsonNode = new JsonNode();
        jsonNode.putVar("出力日時", new JsonObject(LocalDate.now().toString()));
        jsonNode.putVar("可変項目名リスト", new JsonObject(可変項目名リスト()));
        jsonNode.putVar("データリスト", new JsonObject(データリスト()));

        FormaWriter sut = new FormaWriter();
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
        jsonNode1.putVar("可変項目リスト", new JsonObject(Arrays.asList("タス\rク", "v1.0.0\n\r.RELEASE", "1")));
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

    /**
     * list タグを使用した書き込みのテスト
     * @throws Exception
     */
    @Test
    void sample2() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream in = classLoader.getResource("writer/test.xml").openStream();
        File outFile = Files.createTempFile("test", String.format("%s.xlsx", LocalDateTime.now().toString())).toFile();
        FileOutputStream out = new FileOutputStream(outFile);
        logger.log(Level.INFO, "xlsxファイル出力先: " + outFile.getAbsolutePath());

        JsonNode jsonNode = new JsonNode();
        jsonNode.putVar("データリスト", new JsonObject(データリスト2()));

        FormaWriter sut = new FormaWriter();
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

    /**
     * 設定ファイルを使わずに書き込みを行うテスト
     * @throws Exception
     */
    @Test
    void sample3() throws Exception {
        File outFile = Files.createTempFile("test", String.format("%s.xlsx", LocalDateTime.now().toString())).toFile();
        FileOutputStream out = new FileOutputStream(outFile);
        logger.log(Level.INFO, "xlsxファイル出力先: " + outFile.getAbsolutePath());

        JsonNode jsonNode = new JsonNode();
        jsonNode.putVar("データリスト", new JsonObject(データリスト3()));

        FormaWriter sut = new FormaWriter();
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

    /**
     * 複数シートを設定ファイルに記述したときのテスト
     * @throws Exception
     */
    @Test
    void multiple_worksheet_test() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream in = classLoader.getResource("writer/multiple_worksheet_test.xml").openStream();
        File outFile = Files.createTempFile("test", String.format("%s.xlsx", LocalDateTime.now().toString())).toFile();
        FileOutputStream out = new FileOutputStream(outFile);
        logger.log(Level.INFO, "xlsxファイル出力先: " + outFile.getAbsolutePath());

        JsonNode jsonNode = new JsonNode();
        jsonNode.putVar("出力日時", new JsonObject(LocalDate.now().toString()));
        jsonNode.putVar("可変項目名リスト", new JsonObject(可変項目名リスト()));
        jsonNode.putVar("データリスト1", new JsonObject(multiple_worksheet_test_data1()));
        jsonNode.putVar("データリスト2", new JsonObject(multiple_worksheet_test_data2()));
        jsonNode.putVar("データリスト3", new JsonObject(multiple_worksheet_test_data3()));

        FormaWriter sut = new FormaWriter();
        sut.write(in, out, new JsonObject(jsonNode));
    }

    private JsonNodes multiple_worksheet_test_data1() {
        JsonNodes jsonNodes = new JsonNodes();
        JsonNode jsonNode1 = new JsonNode();
        jsonNode1.putVar("キー１", new JsonObject("あいうえお"));
        jsonNode1.putVar("キー２", new JsonObject("かきくけこ"));
        jsonNode1.putVar("キー３", new JsonObject("さしすせそ"));

        jsonNodes.add(jsonNode1);

        JsonNode jsonNode2 = new JsonNode();
        jsonNode2.putVar("キー１", new JsonObject("たちつてと"));
        jsonNode2.putVar("キー２", new JsonObject("なにぬねの"));
        jsonNode2.putVar("キー３", new JsonObject("はひふへほ"));

        jsonNodes.add(jsonNode2);

        return jsonNodes;
    }

    private JsonNodes multiple_worksheet_test_data2() {
        JsonNodes jsonNodes = new JsonNodes();
        JsonNode jsonNode1 = new JsonNode();
        jsonNode1.putVar("キー１", new JsonObject("まみむめも"));
        jsonNode1.putVar("キー２", new JsonObject("やゆよ"));

        jsonNodes.add(jsonNode1);

        JsonNode jsonNode2 = new JsonNode();
        jsonNode2.putVar("キー１", new JsonObject("らりるれろ"));
        jsonNode2.putVar("キー２", new JsonObject("わをん"));

        jsonNodes.add(jsonNode2);

        return jsonNodes;
    }

    private JsonNodes multiple_worksheet_test_data3() {
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

    /**
     * sheet タグに collection を設定ときのテスト
     * @throws Exception
     */
    @Test
    void sheet_collection_test() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream in = classLoader.getResource("writer/all_worksheet_test.xml").openStream();
        File outFile = Files.createTempFile("test", String.format("%s.xlsx", LocalDateTime.now().toString())).toFile();
        FileOutputStream out = new FileOutputStream(outFile);
        logger.log(Level.INFO, "xlsxファイル出力先: " + outFile.getAbsolutePath());

        JsonNode jsonNode = new JsonNode();
        jsonNode.putVar("データリスト", new JsonObject(all_worksheet_test_data()));

        FormaWriter sut = new FormaWriter();
        sut.write(in, out, new JsonObject(jsonNode));
    }

    private JsonNodes all_worksheet_test_data() {
        JsonNode sheet1Node = new JsonNode();
        sheet1Node.putVar("キー１", new JsonObject("あいうえお"));
        sheet1Node.putVar("キー２", new JsonObject("かきくけこ"));

        JsonNodes sheet1Nodes = new JsonNodes();
        sheet1Nodes.add(sheet1Node);

        JsonNode sheet1 = new JsonNode();
        sheet1.putVar("コレクション", new JsonObject(sheet1Nodes));

        JsonNode sheet2Node = new JsonNode();
        sheet2Node.putVar("key1", new JsonObject("さしすせそ"));
        sheet2Node.putVar("key2", new JsonObject("たちつてと"));
        sheet2Node.putVar("key3", new JsonObject("なにぬねの"));

        JsonNodes sheet2Nodes = new JsonNodes();
        sheet2Nodes.add(sheet2Node);

        JsonNode sheet2 = new JsonNode();
        sheet2.putVar("コレクション", new JsonObject(sheet2Nodes));

        JsonNode root = new JsonNode();
        root.putVar("sheet1", new JsonObject(sheet1));
        root.putVar("sheet2", new JsonObject(sheet2));

        JsonNodes result = new JsonNodes();
        result.add(root);

        return result;
    }

    /**
     * テンプレートを利用した Excel 書き込みのテスト
     * @throws Exception
     */
    @Test
    void template_xlsx_test() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream in = classLoader.getResource("writer/一覧.xml").openStream();
        File inFile = new File(classLoader.getResource("writer/list.xlsx").getPath());
        File outFile = Files.createTempFile("test", String.format("%s.xlsx", LocalDateTime.now().toString())).toFile();
        FileInputStream inputStream = new FileInputStream(inFile);
        FileOutputStream outputStream = new FileOutputStream(outFile, true);
        logger.log(Level.INFO, "xlsxファイル出力先: " + outFile.getAbsolutePath());

        JsonNode jsonNode = new JsonNode();
        jsonNode.putVar("出力日時", new JsonObject(LocalDate.now().toString()));
        jsonNode.putVar("データリスト", new JsonObject(append_test_data()));

        FormaWriter sut = new FormaWriter();
        sut.write(in, outputStream, inputStream, new JsonObject(jsonNode));
    }

    /**
     * パスワードを設定する Excel 書き込みのテスト
     * @throws Exception
     */
    @Test
    void password_xlsx_test() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream in = classLoader.getResource("writer/一覧.xml").openStream();
        File outFile = Files.createTempFile("test", String.format("%s.xlsx", LocalDateTime.now().toString())).toFile();
        logger.log(Level.INFO, "xlsxファイル出力先: " + outFile.getAbsolutePath());

        JsonNode jsonNode = new JsonNode();
        jsonNode.putVar("出力日時", new JsonObject(LocalDate.now().toString()));
        jsonNode.putVar("データリスト", new JsonObject(append_test_data()));

        FormaWriter sut = new FormaWriter();
        sut.write(in, outFile.getAbsolutePath(), "password", new JsonObject(jsonNode));
    }

    /**
     * パスワードを設定する Excel 書き込みのテスト（テンプレートを使用する場合）
     * @throws Exception
     */
    @Test
    void password_xlsx_with_template_test() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream in = classLoader.getResource("writer/一覧.xml").openStream();
        File inFile = new File(classLoader.getResource("writer/list.xlsx").getPath());
        File outFile = Files.createTempFile("test", String.format("%s.xlsx", LocalDateTime.now().toString())).toFile();
        FileInputStream inputStream = new FileInputStream(inFile);
        FileOutputStream outputStream = new FileOutputStream(outFile, true);
        logger.log(Level.INFO, "xlsxファイル出力先: " + outFile.getAbsolutePath());

        JsonNode jsonNode = new JsonNode();
        jsonNode.putVar("出力日時", new JsonObject(LocalDate.now().toString()));
        jsonNode.putVar("データリスト", new JsonObject(append_test_data()));

        FormaWriter sut = new FormaWriter();
        sut.write(in, outFile.getAbsolutePath(), "password", inputStream, new JsonObject(jsonNode));
    }

    private JsonNodes append_test_data() {
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

        JsonNode jsonNode3 = new JsonNode();
        jsonNode3.putVar("キー", new JsonObject("TEST-2"));
        jsonNode3.putVar("件名", new JsonObject("yy機能を実装する"));
        jsonNode3.putVar("担当者", new JsonObject(""));
        jsonNode3.putVar("状態", new JsonObject("未対応"));
        jsonNode3.putVar("更新日時", new JsonObject("2020/11/2"));
        jsonNode3.putVar("更新者", new JsonObject("ユーザB"));
        jsonNode3.putVar("登録日時", new JsonObject("2020/11/2"));
        jsonNode3.putVar("登録者", new JsonObject("ユーザB"));

        jsonNodes.add(jsonNode3);

        return jsonNodes;
    }

    /**
     * スタイル指定のテスト
     *
     * @throws IOException
     */
    @Test
    void style() throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream in = classLoader.getResource("writer/style.xml").openStream();
        File outFile = Files.createTempFile("test", String.format("%s.xlsx", LocalDateTime.now())).toFile();
        FileOutputStream out = new FileOutputStream(outFile);
        logger.log(Level.INFO, "xlsxファイル出力先: " + outFile.getAbsolutePath());

        FormaWriter sut = new FormaWriter();
        sut.write(in, out, new JsonObject());
    }
}
