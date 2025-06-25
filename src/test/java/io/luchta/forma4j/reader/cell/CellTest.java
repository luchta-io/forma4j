package io.luchta.forma4j.reader.cell;

import io.luchta.forma4j.context.databind.convert.JsonSerializer;
import io.luchta.forma4j.context.databind.json.JsonNode;
import io.luchta.forma4j.context.databind.json.JsonNodes;
import io.luchta.forma4j.context.databind.json.JsonObject;
import io.luchta.forma4j.reader.FormaReader;
import org.apache.poi.ss.usermodel.FormulaError;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;

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

            JsonSerializer serializer = new JsonSerializer();
            System.out.println(serializer.serializeFromJsonObject(obj));

            Object root = obj.getValue();
            Assertions.assertEquals(true, root instanceof JsonNode);

            JsonObject sheet = ((JsonNode) root).getVar("forma");
            Assertions.assertEquals(true, sheet.getValue() instanceof JsonNode);

            JsonObject cells = ((JsonNode) sheet.getValue()).getVar("ひらがな");
            Assertions.assertEquals(true, cells.getValue() instanceof JsonNode);

            JsonNode value = ((JsonNode) cells.getValue());

            Assertions.assertEquals("あああああ", value.getVar("value1").getValue().toString());
            Assertions.assertEquals("せせせせせ", value.getVar("value2").getValue().toString());
            Assertions.assertEquals("こここここ", value.getVar("value3").getValue().toString());
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
            Assertions.assertEquals(true, cells1.getValue() instanceof JsonNode);

            JsonNode values1 = ((JsonNode) cells1.getValue());

            Assertions.assertEquals("あああああ", values1.getVar("value1").getValue().toString());
            Assertions.assertEquals("せせせせせ", values1.getVar("value2").getValue().toString());
            Assertions.assertEquals("こここここ", values1.getVar("value3").getValue().toString());

            JsonObject cells2 = ((JsonNodes) sheets.getValue()).get(1).getVar("カタカナ");
            Assertions.assertEquals(true, cells2.getValue() instanceof JsonNode);

            JsonNode values2 = ((JsonNode) cells2.getValue());

            Assertions.assertEquals("ヒヒヒヒヒ", values2.getVar("value1").getValue().toString());

            JsonObject cells3 = ((JsonNodes) sheets.getValue()).get(2).getVar("半角カタカナ");
            Assertions.assertEquals(true, cells3.getValue() instanceof JsonNode);

            JsonNode values3 = ((JsonNode) cells3.getValue());

            Assertions.assertEquals("ﾕﾕﾕﾕﾕ", values3.getVar("value1").getValue().toString());
            Assertions.assertEquals("ｷｷｷｷｷ", values3.getVar("value2").getValue().toString());
            Assertions.assertEquals("ﾅﾅﾅﾅﾅ", values3.getVar("value3").getValue().toString());
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

    /**
     * cellタグを使用して未入力行のセルの値を読み取るテスト
     */
    @Test
    public void cell_empty_row() throws Exception {
        try (InputStream config = new FileInputStream(this.getClass().getClassLoader().getResource("reader/cell/cell_empty_row.xml").getPath());
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

            Assertions.assertEquals(true, value.getVar("value").isEmpty());
        }
    }

    /**
     * 数値が入力されているcellの値を読み取るテスト
     */
    @Test
    public void cell_read_numeric() throws Exception {
        try (InputStream config = new FileInputStream(this.getClass().getClassLoader().getResource("reader/cell/cell_read_numeric.xml").getPath());
             InputStream excel = new FileInputStream(this.getClass().getClassLoader().getResource("reader/Test.xlsx").getPath());) {
            FormaReader formaReader = new FormaReader();
            JsonObject obj = formaReader.read(config, excel);

            Object root = obj.getValue();
            Assertions.assertEquals(true, root instanceof JsonNode);

            JsonObject sheet = ((JsonNode) root).getVar("forma");
            Assertions.assertEquals(true, sheet.getValue() instanceof JsonNode);

            JsonObject cell = ((JsonNode) sheet.getValue()).getVar("数値");
            Assertions.assertEquals(true, cell.getValue() instanceof JsonNode);

            JsonNode value = ((JsonNode) cell.getValue());

            Assertions.assertEquals(new BigDecimal("1"), value.getVar("value1").getValue());
            Assertions.assertEquals(new BigDecimal("1.3"), value.getVar("value2").getValue());
            Assertions.assertEquals(new BigDecimal("18000000"), value.getVar("value3").getValue());
        }
    }

    /**
     * 日時が入力されているcellの値を読み取るテスト
     */
    @Test
    public void cell_read_date() throws Exception {
        try (InputStream config = new FileInputStream(this.getClass().getClassLoader().getResource("reader/cell/cell_read_date.xml").getPath());
             InputStream excel = new FileInputStream(this.getClass().getClassLoader().getResource("reader/Test.xlsx").getPath());) {
            FormaReader formaReader = new FormaReader();
            JsonObject obj = formaReader.read(config, excel);

            Object root = obj.getValue();
            Assertions.assertEquals(true, root instanceof JsonNode);

            JsonObject sheet = ((JsonNode) root).getVar("forma");
            Assertions.assertEquals(true, sheet.getValue() instanceof JsonNode);

            JsonObject cell = ((JsonNode) sheet.getValue()).getVar("日付");
            Assertions.assertEquals(true, cell.getValue() instanceof JsonNode);

            JsonNode value = ((JsonNode) cell.getValue());

            Assertions.assertEquals(LocalDateTime.of(2024, 6, 1, 0, 0, 0, 0), value.getVar("value").getValue());
        }
    }

    /**
     * 真理値が入力されているcellの値を読み取るテスト
     */
    @Test
    public void cell_read_bool() throws Exception {
        try (InputStream config = new FileInputStream(this.getClass().getClassLoader().getResource("reader/cell/cell_read_bool.xml").getPath());
             InputStream excel = new FileInputStream(this.getClass().getClassLoader().getResource("reader/Test.xlsx").getPath());) {
            FormaReader formaReader = new FormaReader();
            JsonObject obj = formaReader.read(config, excel);

            Object root = obj.getValue();
            Assertions.assertEquals(true, root instanceof JsonNode);

            JsonObject sheet = ((JsonNode) root).getVar("forma");
            Assertions.assertEquals(true, sheet.getValue() instanceof JsonNode);

            JsonObject cell = ((JsonNode) sheet.getValue()).getVar("真理値");
            Assertions.assertEquals(true, cell.getValue() instanceof JsonNode);

            JsonNode value = ((JsonNode) cell.getValue());

            Assertions.assertEquals(true, value.getVar("value").getValue());
        }
    }

    /**
     * 計算式が入力されているcellの値を読み取るテスト
     */
    @Test
    public void cell_read_fomula() throws Exception {
        try (InputStream config = new FileInputStream(this.getClass().getClassLoader().getResource("reader/cell/cell_read_fomula.xml").getPath());
             InputStream excel = new FileInputStream(this.getClass().getClassLoader().getResource("reader/Test.xlsx").getPath());) {
            FormaReader formaReader = new FormaReader();
            JsonObject obj = formaReader.read(config, excel);

            Object root = obj.getValue();
            Assertions.assertEquals(true, root instanceof JsonNode);

            JsonObject sheet = ((JsonNode) root).getVar("forma");
            Assertions.assertEquals(true, sheet.getValue() instanceof JsonNode);

            JsonObject cell = ((JsonNode) sheet.getValue()).getVar("計算式");
            Assertions.assertEquals(true, cell.getValue() instanceof JsonNode);

            JsonNode values = ((JsonNode) cell.getValue());

            Assertions.assertEquals("あああああ", values.getVar("string").getValue());
            Assertions.assertEquals(new BigDecimal(50), values.getVar("number").getValue());
            Assertions.assertEquals(LocalDateTime.of(2024, 7, 21, 0, 0, 0, 0), values.getVar("date").getValue());
            Assertions.assertEquals(true, values.getVar("bool").getValue());
            Assertions.assertEquals(FormulaError.DIV0.getLongCode(), values.getVar("error").getValue());
            Assertions.assertEquals(FormulaError.NAME.getLongCode(), values.getVar("exception").getValue());
            Assertions.assertEquals(null, values.getVar("blank").getValue());
        }
    }

    /**
     * displayValueプロパティのテスト
     */
    @Test
    public void cell_display_value_property() throws Exception {
        try (InputStream config = new FileInputStream(this.getClass().getClassLoader().getResource("reader/cell/cell_display_value_property.xml").getPath());
             InputStream excel = new FileInputStream(this.getClass().getClassLoader().getResource("reader/Test.xlsx").getPath());) {
            FormaReader formaReader = new FormaReader();
            JsonObject obj = formaReader.read(config, excel);

            Object root = obj.getValue();
            Assertions.assertEquals(true, root instanceof JsonNode);

            JsonObject sheet = ((JsonNode) root).getVar("forma");
            Assertions.assertEquals(true, sheet.getValue() instanceof JsonNode);

            JsonObject cell = ((JsonNode) sheet.getValue()).getVar("表示形式");
            Assertions.assertEquals(true, cell.getValue() instanceof JsonNode);

            JsonNode value = ((JsonNode) cell.getValue());
            Assertions.assertEquals("1.1", value.getVar("value1").getValue());
            Assertions.assertEquals(new BigDecimal("1.051234"), value.getVar("value2").getValue());
            Assertions.assertEquals(new BigDecimal("1.051234"), value.getVar("value3").getValue());
            Assertions.assertEquals("1.123456789", value.getVar("value4").getValue());
            Assertions.assertEquals("1.123456789", value.getVar("value5").getValue());
            Assertions.assertEquals("1,234", value.getVar("value6").getValue());
            Assertions.assertEquals("¥1,234", value.getVar("value7").getValue());
        }
    }
}
