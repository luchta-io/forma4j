package io.luchta.forma4j.writer.engine.handler.element;

import io.luchta.forma4j.writer.definition.schema.attribute.Name;
import io.luchta.forma4j.writer.definition.schema.attribute.cell.DataFormat;
import io.luchta.forma4j.writer.definition.schema.element.Cell;
import io.luchta.forma4j.writer.engine.buffer.BuildBuffer;
import io.luchta.forma4j.writer.engine.model.cell.XlsxCell;
import io.luchta.forma4j.writer.engine.model.cell.address.XlsxCellAddress;
import io.luchta.forma4j.writer.engine.model.cell.address.XlsxColumnNumber;
import io.luchta.forma4j.writer.engine.model.cell.address.XlsxRowNumber;
import io.luchta.forma4j.writer.engine.model.cell.address.XlsxSheetName;
import io.luchta.forma4j.writer.engine.model.cell.style.DataFormatProperty;
import io.luchta.forma4j.writer.engine.model.cell.style.XlsxCellStyle;
import io.luchta.forma4j.writer.engine.model.cell.style.XlsxCellType;
import io.luchta.forma4j.writer.engine.model.cell.value.Text;
import io.luchta.forma4j.writer.engine.model.cell.value.XlsxCellValue;
import io.luchta.forma4j.writer.engine.model.column.XlsxColumnAddress;
import io.luchta.forma4j.writer.engine.model.column.property.XlsxColumnProperties;
import io.luchta.forma4j.writer.engine.model.column.property.XlsxColumnProperty;
import io.luchta.forma4j.writer.engine.resolver.StyleResolver;

/**
 * Cellハンドルクラス
 */
public class CellHandler {
    BuildBuffer buffer;

    /**
     * コンストラクタ
     * @param buffer
     */
    public CellHandler(BuildBuffer buffer) {
        this.buffer = buffer;
    }

    /**
     * ハンドル
     * <p>
     * Cellへの書き込み内容の設定処理を行います。
     * </p>
     * @param cell
     */
    public void handle(Cell cell) {
        XlsxCellAddress address = buffer.addressStack().peek();
        if (!cell.rowIndex().isEmpty() || !cell.columnIndex().isEmpty()) {
            // rowIndex、columnIndexがcellタグに設定されている場合は、cellタグに設定された値を優先する
            XlsxRowNumber xlsxRowNumber = cell.rowIndex().isEmpty() ? address.rowNumber() : new XlsxRowNumber(cell.rowIndex());
            XlsxColumnNumber xlsxColumnNumber = cell.columnIndex().isEmpty() ? address.columnNumber() : new XlsxColumnNumber(cell.columnIndex());
            address = new XlsxCellAddress(address.sheetName(), xlsxRowNumber, xlsxColumnNumber);
        }

        // cellへの出力内容を設定
        XlsxCellValue<?> cellValue = value(cell);
		StyleResolver styleResolver = new StyleResolver();
        XlsxCellStyle cellStyle = styleResolver.get(cell.style(), buffer.variableResolver());
        XlsxCellType xlsxCellType = null;
        if (!cell.cellType().isEmpty()) {
            xlsxCellType = cellType(cell.cellType().toString());
        }
        dataFormat(xlsxCellType, cell.dataFormat(), cellStyle);
        XlsxCell xlsxCell = new XlsxCell(address, cellValue, cellStyle, xlsxCellType);

        // 列へのスタイル設定
        XlsxColumnAddress columnAddress = new XlsxColumnAddress(
                new XlsxSheetName(new Name(address.sheetName().toString())),
                new XlsxColumnNumber(address.columnNumber().toLong()));
        XlsxColumnProperties columnProperties = styleResolver.getColumnProperties(cell.style(), buffer.variableResolver());

        // 設定した内容をbufferへセットする
        buffer.accumulator().put(address, xlsxCell);
        for (XlsxColumnProperty columnProperty : columnProperties) {
            buffer.accumulator().putColumnProperties(columnAddress, columnProperty);
        }
    }

    /**
     * cellへの出力値を取得
     * <p>
     * #{hoge} 形式で入力されている場合は変数の評価をして値を取得する。
     * </p>
     * @param cell
     * @return cellへの出力値
     */
    private XlsxCellValue<?> value(Cell cell) {
        return cell.text().isVariable() ?
                buffer.variableResolver().get(cell.text().stripMarker()) :
                new Text(cell.text().toString());
    }

    /**
     * cellType を取得
     *
     * @param name cellType の設定値
     * @return XlsxCellType
     */
    private XlsxCellType cellType(String name) {
        try {
            return XlsxCellType.valueOf(name.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("不明な cellType が設定されています。" + name, e);
        }
    }

    /**
     * データフォマットを設定
     *
     * @param type セル種別
     * @param format データフォーマット設定値
     * @param style スタイル
     */
    private void dataFormat(XlsxCellType type, DataFormat format, XlsxCellStyle style) {
        if (type == null) {
            return;
        }

        String dataFormat = type.defaultFormat();

        if (dataFormat == null && format.isEmpty()) {
            return;
        }

        if (!format.isEmpty()) {
            dataFormat = format.toString();
        }

        style.add(new DataFormatProperty(dataFormat));
    }
}
