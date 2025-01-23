package io.luchta.forma4j.reader.excel.objectreader;

import io.luchta.forma4j.context.databind.json.JsonNode;
import io.luchta.forma4j.context.databind.json.JsonObject;
import io.luchta.forma4j.reader.model.excel.Index;
import io.luchta.forma4j.reader.model.tag.CellTag;
import io.luchta.forma4j.reader.model.tag.TagTree;
import org.apache.poi.ss.usermodel.*;

import java.math.BigDecimal;
import java.text.Format;
import java.util.HashMap;
import java.util.Map;

/**
 * セル読み込みクラス
 */
public class CellReader implements ObjectReader {

    /**
     * シート
     */
    private Sheet sheet;

    /**
     * 行インデックス
     */
    private Index rowIndex;

    /**
     * 列インデックス
     */
    private Index colIndex;

    /**
     * タグ
     */
    private TagTree tagTree;

    /**
     * コンストラクタ
     * @param sheet
     * @param rowIndex
     * @param colIndex
     * @param tagTree
     */
    public CellReader(Sheet sheet, Index rowIndex, Index colIndex, TagTree tagTree) {
        this.sheet = sheet;
        this.rowIndex = rowIndex;
        this.colIndex = colIndex;
        this.tagTree = tagTree;
    }

    /**
     * 読み込み処理
     * <p>
     * セルの値を読み取り、読み取り結果を返す。
     * </p>
     * @return 読み込み結果
     */
    @Override
    public JsonNode read() {

        CellTag cellTag = (CellTag) tagTree.getTag();

        Integer row = cellTag.rowIsUndefined() ? rowIndex.toInteger() : cellTag.row().toInteger();
        Integer col = cellTag.colIsUndefined() ? colIndex.toInteger() : cellTag.col().toInteger();

        String[] names = cellTag.name().toString().split("\\.");

        Row r = sheet.getRow(row);
        if (r == null) {
            return setValue(names, new JsonObject());
        }
        Cell cell = sheet.getRow(row).getCell(col);

        if (cellTag.displayValue().truthy()) {
            return setValue(names, cellDisplayValue(cell));
        }

        return setValue(names, cellValue(cell));
    }

    /**
     * セルの値を読み取る
     * @param cell 読み取りを行うセル
     * @return 読み込み結果
     */
    private JsonObject cellValue(Cell cell) {
        FormulaEvaluator formulaEvaluator = sheet.getWorkbook().getCreationHelper().createFormulaEvaluator();

        CellValue cellValue = null;
        try {
            cellValue = formulaEvaluator.evaluate(cell);
        } catch (Exception ex) {
            return new JsonObject(cell.getErrorCellValue());
        }

        if (cellValue == null) {
            return new JsonObject();
        }

        // セルの種別に応じて読み込み処理を分岐する
        switch (cellValue.getCellType()) {
            case STRING:
                return new JsonObject(cellValue.getStringValue());
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    // セルの値が数値かつ日付フォーマットであれば日付として読み込みを行う
                    return new JsonObject(cell.getLocalDateTimeCellValue());
                } else {
                    double numericValue = cellValue.getNumberValue();
                    if (numericValue % 1 == 0) {
                        return new JsonObject(new BigDecimal(((Double) cellValue.getNumberValue()).intValue()));
                    }
                    return new JsonObject(new BigDecimal(String.valueOf(cellValue.getNumberValue())));
                }
            case BOOLEAN:
                return new JsonObject(cellValue.getBooleanValue());
            case ERROR:
                return new JsonObject(cellValue.getErrorValue());
            default:
                return new JsonObject();
        }
    }

    /**
     * セルの値を読み取る
     * <p>
     * セルに表示されている値を取得する
     * </p>
     * @param cell 読み取りを行うセル
     * @return 読み込み結果
     */
    private JsonObject cellDisplayValue(Cell cell) {
        FormulaEvaluator formulaEvaluator = sheet.getWorkbook().getCreationHelper().createFormulaEvaluator();
        DataFormatter formatter = new DataFormatter();
        String formattedCellValue = formatter.formatCellValue(cell, formulaEvaluator);
        return new JsonObject(formattedCellValue);
    }

    private JsonNode setValue(String[] names, JsonObject jsonObject) {
        Map<String, JsonNode> map = new HashMap<>();
        for (String name : names) {
            JsonNode node = new JsonNode();
            map.put(name, node);
        }

        String valueName = names[names.length - 1];
        JsonNode result = map.get(valueName);
        result.putVar(valueName, jsonObject);
        map.put(valueName, result);

        for (int i = map.size() - 2; i >= 0; i--) {
            JsonNode parent = map.get(names[i]);
            JsonNode child = map.get(names[i + 1]);
            parent.putVar(names[i], new JsonObject(child));
            map.put(names[i], parent);
        }

        return map.get(names[0]);
    }
}
