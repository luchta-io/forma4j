package io.luchta.forma4j.reader.vfor;

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
 * v-forタグに関するテスト
 */
public class VForTest {
    /**
     * v-forタグを使用してセルの値を読み取るテスト
     */
    @Test
    public void vfor() throws Exception {
        try (InputStream config = new FileInputStream(this.getClass().getClassLoader().getResource("reader/vfor/vfor.xml").getPath());
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

            Assertions.assertEquals(11, values.size());

            Assertions.assertEquals("あああああ", values.get(0).getVar("value1").getValue().toString());
            Assertions.assertEquals("えええええ", values.get(0).getVar("value2").getValue().toString());

            Assertions.assertEquals("かかかかか", values.get(1).getVar("value1").getValue().toString());
            Assertions.assertEquals("けけけけけ", values.get(1).getVar("value2").getValue().toString());

            Assertions.assertEquals("さささささ", values.get(2).getVar("value1").getValue().toString());
            Assertions.assertEquals("せせせせせ", values.get(2).getVar("value2").getValue().toString());

            Assertions.assertEquals("たたたたた", values.get(3).getVar("value1").getValue().toString());
            Assertions.assertEquals("ててててて", values.get(3).getVar("value2").getValue().toString());

            Assertions.assertEquals("ななななな", values.get(4).getVar("value1").getValue().toString());
            Assertions.assertEquals("ねねねねね", values.get(4).getVar("value2").getValue().toString());

            Assertions.assertEquals("ははははは", values.get(5).getVar("value1").getValue().toString());
            Assertions.assertEquals("へへへへへ", values.get(5).getVar("value2").getValue().toString());

            Assertions.assertEquals("ままままま", values.get(6).getVar("value1").getValue().toString());
            Assertions.assertEquals("めめめめめ", values.get(6).getVar("value2").getValue().toString());

            Assertions.assertEquals("ややややや", values.get(7).getVar("value1").getValue().toString());
            Assertions.assertEquals(true, values.get(7).getVar("value2").isEmpty());

            Assertions.assertEquals("ららららら", values.get(8).getVar("value1").getValue().toString());
            Assertions.assertEquals("れれれれれ", values.get(8).getVar("value2").getValue().toString());

            Assertions.assertEquals("わわわわわ", values.get(9).getVar("value1").getValue().toString());
            Assertions.assertEquals(true, values.get(9).getVar("value2").isEmpty());

            Assertions.assertEquals("んんんんん", values.get(10).getVar("value1").getValue().toString());
            Assertions.assertEquals(true, values.get(10).getVar("value2").isEmpty());
        }
    }

    /**
     * v-forタグでbreakプロパティにall_blankがセットされたときのテスト
     */
    @Test
    public void vfor_all_blank() throws Exception {
        try (InputStream config = new FileInputStream(this.getClass().getClassLoader().getResource("reader/vfor/vfor_all_blank.xml").getPath());
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

            Assertions.assertEquals(7, values.size());
        }
    }
}
