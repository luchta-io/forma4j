package io.luchta.forma4j.util.diff;

import io.luchta.forma4j.context.databind.json.JsonNode;
import io.luchta.forma4j.context.databind.json.JsonNodes;
import io.luchta.forma4j.context.databind.json.JsonObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import util.diff.FormaDiffer;

import java.io.FileInputStream;
import java.io.InputStream;

public class FormaDifferTest {
    /**
     * 内容が同じワークブックを比較するテスト
     * @throws Exception
     */
    @Test
    public void compareWorkbooksWithSameContentTest() throws Exception {
        try (InputStream comparing = new FileInputStream(this.getClass().getClassLoader().getResource("util/diff/compareWorkbooksWithSameContentTest1.xlsx").getPath());
             InputStream compared = new FileInputStream(this.getClass().getClassLoader().getResource("util/diff/compareWorkbooksWithSameContentTest2.xlsx").getPath());) {
            FormaDiffer differ = new FormaDiffer();
            JsonObject jsonObject = differ.diff(comparing, compared);

            Assertions.assertEquals(true, jsonObject.isJsonNodes());

            JsonNodes jsonNodes = (JsonNodes) jsonObject.getValue();
            Assertions.assertEquals(0, jsonNodes.size());
        }
    }

    /**
     * ワークブックの比較で差異がある場合のテスト
     * @throws Exception
     */
    @ParameterizedTest
    @CsvSource({
            "util/diff/diffWorkbookTest1.xlsx, util/diff/diffWorkbookTest2.xlsx",
            "util/diff/diffWorkbookTest2.xlsx, util/diff/diffWorkbookTest1.xlsx"
    })
    public void diffWorkbookTest() throws Exception {
        try (InputStream comparing = new FileInputStream(this.getClass().getClassLoader().getResource("util/diff/diffWorkbookTest1.xlsx").getPath());
             InputStream compared = new FileInputStream(this.getClass().getClassLoader().getResource("util/diff/diffWorkbookTest2.xlsx").getPath());) {
            FormaDiffer differ = new FormaDiffer();
            JsonObject jsonObject = differ.diff(comparing, compared);

            Assertions.assertEquals(true, jsonObject.isJsonNodes());

            JsonNodes jsonNodes = (JsonNodes) jsonObject.getValue();
            JsonNode jsonNode = jsonNodes.get(0);
            Assertions.assertEquals("ワークブック", jsonNode.getVar("target").getValue());
            Assertions.assertEquals("", jsonNode.getVar("sheet").getValue());
            Assertions.assertEquals("", jsonNode.getVar("range").getValue());
            Assertions.assertEquals("", jsonNode.getVar("comparingValue").getValue());
            Assertions.assertEquals("", jsonNode.getVar("comparedValue").getValue());
            Assertions.assertEquals("シートの数が異なります", jsonNode.getVar("description").getValue());
        }
    }

    /**
     * シートに差異がある場合のテスト
     * @param comparingFilePath
     * @param comparedFilePath
     * @param comparingSheetName
     * @param comparedSheetName
     * @param comparingRow
     * @param comparedRow
     * @throws Exception
     */
    @ParameterizedTest
    @CsvSource({
            "util/diff/diffSheetTest1.xlsx, util/diff/diffSheetTest2.xlsx, テスト, テストテスト, 1, 0",
            "util/diff/diffSheetTest2.xlsx, util/diff/diffSheetTest1.xlsx, テストテスト, テスト, 0, 1"
    })
    public void diffSheetTest(String comparingFilePath, String comparedFilePath, String comparingSheetName, String comparedSheetName, String comparingRow, String comparedRow) throws Exception {
        try (InputStream comparing = new FileInputStream(this.getClass().getClassLoader().getResource(comparingFilePath).getPath());
             InputStream compared = new FileInputStream(this.getClass().getClassLoader().getResource(comparedFilePath).getPath());) {
            FormaDiffer differ = new FormaDiffer();
            JsonObject jsonObject = differ.diff(comparing, compared);

            Assertions.assertEquals(true, jsonObject.isJsonNodes());

            JsonNodes jsonNodes = (JsonNodes) jsonObject.getValue();
            JsonNode jsonNode1 = jsonNodes.get(0);
            Assertions.assertEquals("シート", jsonNode1.getVar("target").getValue());
            Assertions.assertEquals("", jsonNode1.getVar("sheet").getValue());
            Assertions.assertEquals("", jsonNode1.getVar("range").getValue());
            Assertions.assertEquals(comparingSheetName, jsonNode1.getVar("comparingValue").getValue());
            Assertions.assertEquals(comparedSheetName, jsonNode1.getVar("comparedValue").getValue());
            Assertions.assertEquals("シート名が異なります", jsonNode1.getVar("description").getValue());

            JsonNode jsonNode2 = jsonNodes.get(1);
            Assertions.assertEquals("シート", jsonNode2.getVar("target").getValue());
            Assertions.assertEquals(comparingSheetName, jsonNode2.getVar("sheet").getValue());
            Assertions.assertEquals("", jsonNode2.getVar("range").getValue());
            Assertions.assertEquals(comparingRow, jsonNode2.getVar("comparingValue").getValue());
            Assertions.assertEquals(comparedRow, jsonNode2.getVar("comparedValue").getValue());
            Assertions.assertEquals("シートの行数が異なります", jsonNode2.getVar("description").getValue());
        }
    }

    /**
     * 行に差異がある場合のテスト
     * @param comparingFilePath
     * @param comparedFilePath
     * @throws Exception
     */
    @ParameterizedTest
    @CsvSource({
            "util/diff/diffRowTest1.xlsx, util/diff/diffRowTest2.xlsx",
            "util/diff/diffRowTest2.xlsx, util/diff/diffRowTest1.xlsx"
    })
    public void diffRowTest(String comparingFilePath, String comparedFilePath) throws Exception {
        try (InputStream comparing = new FileInputStream(this.getClass().getClassLoader().getResource(comparingFilePath).getPath());
             InputStream compared = new FileInputStream(this.getClass().getClassLoader().getResource(comparedFilePath).getPath());) {
            FormaDiffer differ = new FormaDiffer();
            JsonObject jsonObject = differ.diff(comparing, compared);

            Assertions.assertEquals(true, jsonObject.isJsonNodes());

            JsonNodes jsonNodes = (JsonNodes) jsonObject.getValue();
            JsonNode jsonNode = jsonNodes.get(0);
            Assertions.assertEquals("行", jsonNode.getVar("target").getValue());
            Assertions.assertEquals("テスト", jsonNode.getVar("sheet").getValue());
            Assertions.assertEquals("1", jsonNode.getVar("range").getValue());
            Assertions.assertEquals("", jsonNode.getVar("comparingValue").getValue());
            Assertions.assertEquals("", jsonNode.getVar("comparedValue").getValue());
            Assertions.assertEquals("行が存在しません", jsonNode.getVar("description").getValue());
        }
    }

    /**
     * セルに差異がある場合のテスト
     * @throws Exception
     */
    @ParameterizedTest
    @CsvSource({
            "util/diff/diffCellTest1.xlsx, util/diff/diffCellTest2.xlsx, テスト, テストテスト",
            "util/diff/diffCellTest2.xlsx, util/diff/diffCellTest1.xlsx, テストテスト, テスト"
    })
    public void diffCellTest(String comparingFilePath, String comparedFilePath, String comparingValue, String comparedValue) throws Exception {
        try (InputStream comparing = new FileInputStream(this.getClass().getClassLoader().getResource(comparingFilePath).getPath());
             InputStream compared = new FileInputStream(this.getClass().getClassLoader().getResource(comparedFilePath).getPath());) {
            FormaDiffer differ = new FormaDiffer();
            JsonObject jsonObject = differ.diff(comparing, compared);

            Assertions.assertEquals(true, jsonObject.isJsonNodes());

            JsonNodes jsonNodes = (JsonNodes) jsonObject.getValue();
            JsonNode jsonNode1 = jsonNodes.get(0);
            Assertions.assertEquals("セル", jsonNode1.getVar("target").getValue());
            Assertions.assertEquals("テスト", jsonNode1.getVar("sheet").getValue());
            Assertions.assertEquals("B1", jsonNode1.getVar("range").getValue());
            Assertions.assertEquals("", jsonNode1.getVar("comparingValue").getValue());
            Assertions.assertEquals("", jsonNode1.getVar("comparedValue").getValue());
            Assertions.assertEquals("セルが存在しません", jsonNode1.getVar("description").getValue());

            JsonNode jsonNode2 = jsonNodes.get(1);
            Assertions.assertEquals("セル", jsonNode2.getVar("target").getValue());
            Assertions.assertEquals("テスト", jsonNode2.getVar("sheet").getValue());
            Assertions.assertEquals("C1", jsonNode2.getVar("range").getValue());
            Assertions.assertEquals(comparingValue, jsonNode2.getVar("comparingValue").getValue());
            Assertions.assertEquals(comparedValue, jsonNode2.getVar("comparedValue").getValue());
            Assertions.assertEquals("セルの値が異なります", jsonNode2.getVar("description").getValue());
        }
    }

    /**
     * セルスタイルに差異がある場合のテスト
     * @param comparingFilePath
     * @param comparedFilePath
     * @throws Exception
     */
    @ParameterizedTest
    @CsvSource({
            "util/diff/diffCellStyleTest1.xlsx, util/diff/diffCellStyleTest2.xlsx"
    })
    public void diffCellStyleTest(String comparingFilePath, String comparedFilePath) throws Exception {
        try (InputStream comparing = new FileInputStream(this.getClass().getClassLoader().getResource(comparingFilePath).getPath());
             InputStream compared = new FileInputStream(this.getClass().getClassLoader().getResource(comparedFilePath).getPath());) {
            FormaDiffer differ = new FormaDiffer();
            JsonObject jsonObject = differ.diff(comparing, compared);

            Assertions.assertEquals(true, jsonObject.isJsonNodes());

            JsonNodes jsonNodes = (JsonNodes) jsonObject.getValue();
            JsonNode jsonNode1 = jsonNodes.get(0);
            Assertions.assertEquals("セルスタイル", jsonNode1.getVar("target").getValue());
            Assertions.assertEquals("テスト", jsonNode1.getVar("sheet").getValue());
            Assertions.assertEquals("A1", jsonNode1.getVar("range").getValue());
            Assertions.assertEquals("FFFF00", jsonNode1.getVar("comparingValue").getValue());
            Assertions.assertEquals("FFC000", jsonNode1.getVar("comparedValue").getValue());
            Assertions.assertEquals("セルの背景色が異なります", jsonNode1.getVar("description").getValue());

            JsonNode jsonNode2 = jsonNodes.get(1);
            Assertions.assertEquals("セルスタイル", jsonNode2.getVar("target").getValue());
            Assertions.assertEquals("テスト", jsonNode2.getVar("sheet").getValue());
            Assertions.assertEquals("B1", jsonNode2.getVar("range").getValue());
            Assertions.assertEquals("THIN", jsonNode2.getVar("comparingValue").getValue());
            Assertions.assertEquals("HAIR", jsonNode2.getVar("comparedValue").getValue());
            Assertions.assertEquals("セルの上罫線が異なります", jsonNode2.getVar("description").getValue());

            JsonNode jsonNode3 = jsonNodes.get(2);
            Assertions.assertEquals("セルスタイル", jsonNode3.getVar("target").getValue());
            Assertions.assertEquals("テスト", jsonNode3.getVar("sheet").getValue());
            Assertions.assertEquals("B1", jsonNode3.getVar("range").getValue());
            Assertions.assertEquals("THIN", jsonNode3.getVar("comparingValue").getValue());
            Assertions.assertEquals("HAIR", jsonNode3.getVar("comparedValue").getValue());
            Assertions.assertEquals("セルの下罫線が異なります", jsonNode3.getVar("description").getValue());

            JsonNode jsonNode4 = jsonNodes.get(3);
            Assertions.assertEquals("セルスタイル", jsonNode4.getVar("target").getValue());
            Assertions.assertEquals("テスト", jsonNode4.getVar("sheet").getValue());
            Assertions.assertEquals("B1", jsonNode4.getVar("range").getValue());
            Assertions.assertEquals("THIN", jsonNode4.getVar("comparingValue").getValue());
            Assertions.assertEquals("HAIR", jsonNode4.getVar("comparedValue").getValue());
            Assertions.assertEquals("セルの左罫線が異なります", jsonNode4.getVar("description").getValue());

            JsonNode jsonNode5 = jsonNodes.get(4);
            Assertions.assertEquals("セルスタイル", jsonNode5.getVar("target").getValue());
            Assertions.assertEquals("テスト", jsonNode5.getVar("sheet").getValue());
            Assertions.assertEquals("B1", jsonNode5.getVar("range").getValue());
            Assertions.assertEquals("THIN", jsonNode5.getVar("comparingValue").getValue());
            Assertions.assertEquals("HAIR", jsonNode5.getVar("comparedValue").getValue());
            Assertions.assertEquals("セルの右罫線が異なります", jsonNode5.getVar("description").getValue());

            JsonNode jsonNode6 = jsonNodes.get(5);
            Assertions.assertEquals("セルスタイル", jsonNode6.getVar("target").getValue());
            Assertions.assertEquals("テスト", jsonNode6.getVar("sheet").getValue());
            Assertions.assertEquals("C1", jsonNode6.getVar("range").getValue());
            Assertions.assertEquals("FF0000", jsonNode6.getVar("comparingValue").getValue());
            Assertions.assertEquals("00B0F0", jsonNode6.getVar("comparedValue").getValue());
            Assertions.assertEquals("セルの文字色が異なります", jsonNode6.getVar("description").getValue());

            JsonNode jsonNode7 = jsonNodes.get(6);
            Assertions.assertEquals("セルスタイル", jsonNode7.getVar("target").getValue());
            Assertions.assertEquals("テスト", jsonNode7.getVar("sheet").getValue());
            Assertions.assertEquals("D1", jsonNode7.getVar("range").getValue());
            Assertions.assertEquals("游ゴシック", jsonNode7.getVar("comparingValue").getValue());
            Assertions.assertEquals("ＭＳ Ｐゴシック", jsonNode7.getVar("comparedValue").getValue());
            Assertions.assertEquals("セルのフォントが異なります", jsonNode7.getVar("description").getValue());

            JsonNode jsonNode8 = jsonNodes.get(7);
            Assertions.assertEquals("セルスタイル", jsonNode8.getVar("target").getValue());
            Assertions.assertEquals("テスト", jsonNode8.getVar("sheet").getValue());
            Assertions.assertEquals("E1", jsonNode8.getVar("range").getValue());
            Assertions.assertEquals("12", jsonNode8.getVar("comparingValue").getValue());
            Assertions.assertEquals("9", jsonNode8.getVar("comparedValue").getValue());
            Assertions.assertEquals("セルのフォントサイズが異なります", jsonNode8.getVar("description").getValue());

            JsonNode jsonNode9 = jsonNodes.get(8);
            Assertions.assertEquals("セルスタイル", jsonNode9.getVar("target").getValue());
            Assertions.assertEquals("テスト", jsonNode9.getVar("sheet").getValue());
            Assertions.assertEquals("F1", jsonNode9.getVar("range").getValue());
            Assertions.assertEquals("TOP", jsonNode9.getVar("comparingValue").getValue());
            Assertions.assertEquals("BOTTOM", jsonNode9.getVar("comparedValue").getValue());
            Assertions.assertEquals("セルの垂直方向の文字位置が異なります", jsonNode9.getVar("description").getValue());

            JsonNode jsonNode10 = jsonNodes.get(9);
            Assertions.assertEquals("セルスタイル", jsonNode10.getVar("target").getValue());
            Assertions.assertEquals("テスト", jsonNode10.getVar("sheet").getValue());
            Assertions.assertEquals("G1", jsonNode10.getVar("range").getValue());
            Assertions.assertEquals("RIGHT", jsonNode10.getVar("comparingValue").getValue());
            Assertions.assertEquals("CENTER", jsonNode10.getVar("comparedValue").getValue());
            Assertions.assertEquals("セルの水平方向の文字位置が異なります", jsonNode10.getVar("description").getValue());

//            JsonNode jsonNode11 = jsonNodes.get(10);
//            Assertions.assertEquals("セルスタイル", jsonNode11.getVar("target").getValue());
//            Assertions.assertEquals("テスト", jsonNode11.getVar("sheet").getValue());
//            Assertions.assertEquals("H1", jsonNode11.getVar("range").getValue());
//            Assertions.assertEquals("10", jsonNode11.getVar("comparingValue").getValue());
//            Assertions.assertEquals("21", jsonNode11.getVar("comparedValue").getValue());
//            Assertions.assertEquals("セルの幅が異なります", jsonNode11.getVar("description").getValue());

//            JsonNode jsonNode12 = jsonNodes.get(11);
//            Assertions.assertEquals("セルスタイル", jsonNode12.getVar("target").getValue());
//            Assertions.assertEquals("テスト", jsonNode12.getVar("sheet").getValue());
//            Assertions.assertEquals("I1", jsonNode12.getVar("range").getValue());
//            Assertions.assertEquals("true", jsonNode12.getVar("comparingValue").getValue());
//            Assertions.assertEquals("false", jsonNode12.getVar("comparedValue").getValue());
//            Assertions.assertEquals("セルの折り返し設定が異なります", jsonNode12.getVar("description").getValue());

            JsonNode jsonNode11 = jsonNodes.get(10);
            Assertions.assertEquals("セルスタイル", jsonNode11.getVar("target").getValue());
            Assertions.assertEquals("テスト", jsonNode11.getVar("sheet").getValue());
            Assertions.assertEquals("I1", jsonNode11.getVar("range").getValue());
            Assertions.assertEquals("true", jsonNode11.getVar("comparingValue").getValue());
            Assertions.assertEquals("false", jsonNode11.getVar("comparedValue").getValue());
            Assertions.assertEquals("セルの折り返し設定が異なります", jsonNode11.getVar("description").getValue());
        }
    }
}
