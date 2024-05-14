package io.luchta.forma4j.reader.cell;

import io.luchta.forma4j.context.databind.json.JsonNode;
import io.luchta.forma4j.context.databind.json.JsonNodes;
import io.luchta.forma4j.context.databind.json.JsonObject;
import io.luchta.forma4j.reader.FormaReader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.FileInputStream;
import java.io.InputStream;

/**
 * cellタグに関するテスト
 */
public class CellTest {
    /**
     * cellタグを使用して1つのセルの値を読み取るテスト
     */
    @ParameterizedTest
    @CsvSource({
            "reader/cell/cell.xml, reader/Test.xlsx, あああああ",
            "reader/cell/cell_2_3.xml, reader/Test.xlsx, せせせせせ",
    })
    public void cell(String configPath, String excelPath, String value) throws Exception {
        try (InputStream config = new FileInputStream(this.getClass().getClassLoader().getResource(configPath).getPath());
             InputStream excel = new FileInputStream(this.getClass().getClassLoader().getResource(excelPath).getPath());) {
            FormaReader formaReader = new FormaReader();
            JsonObject obj = formaReader.read(config, excel);

            Object root = obj.getValue();
            Assertions.assertEquals(true, root instanceof JsonNode);

            JsonObject sheet = ((JsonNode) root).getVar("forma");
            Assertions.assertEquals(true, sheet.getValue() instanceof JsonNode);

            JsonObject cell = ((JsonNode) sheet.getValue()).getVar("ひらがな");
            Assertions.assertEquals(true, cell.getValue() instanceof JsonNode);

            JsonNode values = ((JsonNode) cell.getValue());

            Assertions.assertEquals(true, values.getVar("value").getValue() instanceof String);
            Assertions.assertEquals(value, values.getVar("value").getValue().toString());
        }
    }

    /**
     * cellタグを使用して複数のセルの値を読み取るテスト
     */
    @Test
    public void cells() throws Exception {
        try (InputStream config = new FileInputStream(this.getClass().getClassLoader().getResource("reader/cell/cells.xml").getPath());
             InputStream excel = new FileInputStream(this.getClass().getClassLoader().getResource("reader/Test.xlsx").getPath());) {
            FormaReader formaReader = new FormaReader();
            JsonObject obj = formaReader.read(config, excel);

            Object root = obj.getValue();
            Assertions.assertEquals(true, root instanceof JsonNode);

            JsonObject sheet = ((JsonNode) root).getVar("forma");
            Assertions.assertEquals(true, sheet.getValue() instanceof JsonNode);

            JsonObject cells = ((JsonNode) sheet.getValue()).getVar("ひらがな");
            Assertions.assertEquals(true, cells.getValue() instanceof JsonNodes);

            JsonNodes values = ((JsonNodes) cells.getValue());

            Assertions.assertEquals("あああああ", values.get(0).getVar("value1").getValue().toString());
            Assertions.assertEquals("せせせせせ", values.get(1).getVar("value2").getValue().toString());
            Assertions.assertEquals("こここここ", values.get(2).getVar("value3").getValue().toString());
        }
    }

    /**
     * cellタグを使用して複数シートのセルの値を読み取るテスト
     */
    @Test
    public void cell_sheets() throws Exception {
        try (InputStream config = new FileInputStream(this.getClass().getClassLoader().getResource("reader/cell/cell_sheets.xml").getPath());
             InputStream excel = new FileInputStream(this.getClass().getClassLoader().getResource("reader/Test.xlsx").getPath());) {
            FormaReader formaReader = new FormaReader();
            JsonObject obj = formaReader.read(config, excel);

            Object root = obj.getValue();
            Assertions.assertEquals(true, root instanceof JsonNode);

            JsonObject sheets = ((JsonNode) root).getVar("forma");
            Assertions.assertEquals(true, sheets.getValue() instanceof JsonNodes);

            JsonObject cells1 = ((JsonNodes) sheets.getValue()).get(0).getVar("ひらがな");
            Assertions.assertEquals(true, cells1.getValue() instanceof JsonNodes);

            JsonNodes values1 = ((JsonNodes) cells1.getValue());

            Assertions.assertEquals("あああああ", values1.get(0).getVar("value1").getValue().toString());
            Assertions.assertEquals("せせせせせ", values1.get(1).getVar("value2").getValue().toString());
            Assertions.assertEquals("こここここ", values1.get(2).getVar("value3").getValue().toString());

            JsonObject cells2 = ((JsonNodes) sheets.getValue()).get(1).getVar("カタカナ");
            Assertions.assertEquals(true, cells2.getValue() instanceof JsonNode);

            JsonNode values2 = ((JsonNode) cells2.getValue());

            Assertions.assertEquals("ヒヒヒヒヒ", values2.getVar("value1").getValue().toString());

            JsonObject cells3 = ((JsonNodes) sheets.getValue()).get(2).getVar("半角カタカナ");
            Assertions.assertEquals(true, cells3.getValue() instanceof JsonNodes);

            JsonNodes values3 = ((JsonNodes) cells3.getValue());

            Assertions.assertEquals("ﾕﾕﾕﾕﾕ", values3.get(0).getVar("value1").getValue().toString());
            Assertions.assertEquals("ｷｷｷｷｷ", values3.get(1).getVar("value2").getValue().toString());
            Assertions.assertEquals("ﾅﾅﾅﾅﾅ", values3.get(2).getVar("value3").getValue().toString());
        }
    }

    /**
     * cellタグを使用して未入力のセルの値を読み取るテスト
     */
    @Test
    public void cell_empty() throws Exception {
        try (InputStream config = new FileInputStream(this.getClass().getClassLoader().getResource("reader/cell/cell_empty.xml").getPath());
             InputStream excel = new FileInputStream(this.getClass().getClassLoader().getResource("reader/Test.xlsx").getPath());) {
            FormaReader formaReader = new FormaReader();
            JsonObject obj = formaReader.read(config, excel);

            Object root = obj.getValue();
            Assertions.assertEquals(true, root instanceof JsonNode);

            JsonObject sheet = ((JsonNode) root).getVar("forma");
            Assertions.assertEquals(true, sheet.getValue() instanceof JsonNode);

            JsonObject cell = ((JsonNode) sheet.getValue()).getVar("ひらがな");
            Assertions.assertEquals(true, cell.getValue() instanceof JsonNode);

            JsonNode value = ((JsonNode) cell.getValue());

            Assertions.assertEquals(true, value.getVar("value1").isEmpty());
        }
    }
}
