package io.luchta.forma4j.reader.hfor;

import io.luchta.forma4j.context.databind.convert.JsonSerializer;
import io.luchta.forma4j.context.databind.json.JsonNode;
import io.luchta.forma4j.context.databind.json.JsonNodes;
import io.luchta.forma4j.context.databind.json.JsonObject;
import io.luchta.forma4j.reader.FormaReader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.InputStream;

/**
 * h-forタグに関するテスト
 */
public class HForTest {
    /**
     * h-forを使用してセルの値を読み取るテスト
     */
    @Test
    public void hfor() throws Exception {
        try (InputStream config = new FileInputStream(this.getClass().getClassLoader().getResource("reader/hfor/hfor.xml").getPath());
             InputStream excel = new FileInputStream(this.getClass().getClassLoader().getResource("reader/Test.xlsx").getPath());) {
            FormaReader formaReader = new FormaReader();
            JsonObject obj = formaReader.read(config, excel);

            JsonSerializer serializer = new JsonSerializer();
            System.out.println(serializer.serializeFromJsonObject(obj));

            Object root = obj.getValue();
            Assertions.assertEquals(true, root instanceof JsonNode);

            JsonObject sheet = ((JsonNode) root).getVar("forma");
            Assertions.assertEquals(true, sheet.getValue() instanceof JsonNode);

            JsonObject sheets = ((JsonNode) sheet.getValue()).getVar("ひらがな");
            Assertions.assertEquals(true, sheets.getValue() instanceof JsonNode);

            JsonObject data = ((JsonNode) sheets.getValue()).getVar("data");
            Assertions.assertEquals(true, data.getValue() instanceof JsonNodes);

            JsonNodes values = ((JsonNodes) data.getValue());

            Assertions.assertEquals(5, values.size());
            Assertions.assertEquals("あああああ", values.get(0).getVar("value").getValue().toString());
            Assertions.assertEquals("いいいいい", values.get(1).getVar("value").getValue().toString());
            Assertions.assertEquals("ううううう", values.get(2).getVar("value").getValue().toString());
            Assertions.assertEquals("えええええ", values.get(3).getVar("value").getValue().toString());
            Assertions.assertEquals("おおおおお", values.get(4).getVar("value").getValue().toString());
        }
    }

    /**
     * h-forを使用して1列から3列のセルの値を読み取るテスト
     * @throws Exception
     */
    @Test
    public void hfor_from_1_to_3() throws Exception {
        try (InputStream config = new FileInputStream(this.getClass().getClassLoader().getResource("reader/hfor/hfor_from_1_to_3.xml").getPath());
             InputStream excel = new FileInputStream(this.getClass().getClassLoader().getResource("reader/Test.xlsx").getPath());) {
            FormaReader formaReader = new FormaReader();
            JsonObject obj = formaReader.read(config, excel);

            JsonSerializer serializer = new JsonSerializer();
            System.out.println(serializer.serializeFromJsonObject(obj));

            Object root = obj.getValue();
            Assertions.assertEquals(true, root instanceof JsonNode);

            JsonObject sheet = ((JsonNode) root).getVar("forma");
            Assertions.assertEquals(true, sheet.getValue() instanceof JsonNode);

            JsonObject sheets = ((JsonNode) sheet.getValue()).getVar("ひらがな");
            Assertions.assertEquals(true, sheets.getValue() instanceof JsonNode);

            JsonObject data = ((JsonNode) sheets.getValue()).getVar("data");
            Assertions.assertEquals(true, data.getValue() instanceof JsonNodes);

            JsonNodes values = ((JsonNodes) data.getValue());

            Assertions.assertEquals(3, values.size());
            Assertions.assertEquals("いいいいい", values.get(0).getVar("value").getValue().toString());
            Assertions.assertEquals("ううううう", values.get(1).getVar("value").getValue().toString());
            Assertions.assertEquals("えええええ", values.get(2).getVar("value").getValue().toString());
        }
    }

    /**
     * h-forを使用してrow=7のセルの値を読み取るテスト
     * @throws Exception
     */
    @Test
    public void hfor_row_7() throws Exception {
        try (InputStream config = new FileInputStream(this.getClass().getClassLoader().getResource("reader/hfor/hfor_row_7.xml").getPath());
             InputStream excel = new FileInputStream(this.getClass().getClassLoader().getResource("reader/Test.xlsx").getPath());) {
            FormaReader formaReader = new FormaReader();
            JsonObject obj = formaReader.read(config, excel);

            JsonSerializer serializer = new JsonSerializer();
            System.out.println(serializer.serializeFromJsonObject(obj));

            Object root = obj.getValue();
            Assertions.assertEquals(true, root instanceof JsonNode);

            JsonObject sheet = ((JsonNode) root).getVar("forma");
            Assertions.assertEquals(true, sheet.getValue() instanceof JsonNode);

            JsonObject sheets = ((JsonNode) sheet.getValue()).getVar("ひらがな");
            Assertions.assertEquals(true, sheets.getValue() instanceof JsonNode);

            JsonObject data = ((JsonNode) sheets.getValue()).getVar("data");
            Assertions.assertEquals(true, data.getValue() instanceof JsonNodes);

            JsonNodes values = ((JsonNodes) data.getValue());

            Assertions.assertEquals(5, values.size());
            Assertions.assertEquals("ややややや", values.get(0).getVar("value").getValue().toString());
            Assertions.assertEquals(true, values.get(1).getVar("value").isEmpty());
            Assertions.assertEquals("ゆゆゆゆゆ", values.get(2).getVar("value").getValue().toString());
            Assertions.assertEquals(true, values.get(3).getVar("value").isEmpty());
            Assertions.assertEquals("よよよよよ", values.get(4).getVar("value").getValue().toString());
        }
    }

    /**
     * h-forを使用してrow=7,from=1,to=3のセルの値を読み取るテスト
     * @throws Exception
     */
    @Test
    public void hfor_row_3_from_1_to_3() throws Exception {
        try (InputStream config = new FileInputStream(this.getClass().getClassLoader().getResource("reader/hfor/hfor_row_3_from_1_to_3.xml").getPath());
             InputStream excel = new FileInputStream(this.getClass().getClassLoader().getResource("reader/Test.xlsx").getPath());) {
            FormaReader formaReader = new FormaReader();
            JsonObject obj = formaReader.read(config, excel);

            JsonSerializer serializer = new JsonSerializer();
            System.out.println(serializer.serializeFromJsonObject(obj));

            Object root = obj.getValue();
            Assertions.assertEquals(true, root instanceof JsonNode);

            JsonObject sheet = ((JsonNode) root).getVar("forma");
            Assertions.assertEquals(true, sheet.getValue() instanceof JsonNode);

            JsonObject sheets = ((JsonNode) sheet.getValue()).getVar("ひらがな");
            Assertions.assertEquals(true, sheets.getValue() instanceof JsonNode);

            JsonObject data = ((JsonNode) sheets.getValue()).getVar("data");
            Assertions.assertEquals(true, data.getValue() instanceof JsonNodes);

            JsonNodes values = ((JsonNodes) data.getValue());

            Assertions.assertEquals(3, values.size());
            Assertions.assertEquals("ちちちちち", values.get(0).getVar("value").getValue().toString());
            Assertions.assertEquals("つつつつつ", values.get(1).getVar("value").getValue().toString());
            Assertions.assertEquals("ててててて", values.get(2).getVar("value").getValue().toString());
        }
    }

    /**
     * h-forを使用して最終列以降のセルの値を読み取るテスト
     */
    @Test
    public void hfor_beyond_last_column() throws Exception {
        try (InputStream config = new FileInputStream(this.getClass().getClassLoader().getResource("reader/hfor/hfor_beyond_last_column.xml").getPath());
             InputStream excel = new FileInputStream(this.getClass().getClassLoader().getResource("reader/Test.xlsx").getPath());) {
            FormaReader formaReader = new FormaReader();
            JsonObject obj = formaReader.read(config, excel);

            JsonSerializer serializer = new JsonSerializer();
            System.out.println(serializer.serializeFromJsonObject(obj));

            Object root = obj.getValue();
            Assertions.assertEquals(true, root instanceof JsonNode);

            JsonObject sheet = ((JsonNode) root).getVar("forma");
            Assertions.assertEquals(true, sheet.getValue() instanceof JsonNode);

            JsonObject sheets = ((JsonNode) sheet.getValue()).getVar("ひらがな");
            Assertions.assertEquals(true, sheets.getValue() instanceof JsonNode);

            JsonObject data = ((JsonNode) sheets.getValue()).getVar("data");
            Assertions.assertEquals(true, data.getValue() instanceof JsonNodes);

            JsonNodes values = ((JsonNodes) data.getValue());
            Assertions.assertEquals(0, values.size());
        }
    }
}
