package io.luchta.forma4j.writer.engine.strategy;

import io.luchta.forma4j.writer.engine.model.cell.XlsxCell;
import io.luchta.forma4j.writer.engine.model.cell.address.XlsxCellAddress;
import io.luchta.forma4j.writer.engine.model.cell.address.XlsxSheetName;
import io.luchta.forma4j.writer.engine.model.column.XlsxColumnAddress;
import io.luchta.forma4j.writer.engine.model.column.property.XlsxColumnProperty;
import io.luchta.forma4j.writer.engine.model.row.address.XlsxRowAddress;
import io.luchta.forma4j.writer.engine.model.row.property.XlsxRowProperty;

import java.io.IOException;
import java.io.OutputStream;

/** Excel 出力先を切り替えるための戦略です。 */
public interface XlsxOutputStrategy {
    void startSheet(XlsxSheetName sheetName, Boolean autoSizeColumnEnabled);

    void finishSheet(XlsxSheetName sheetName);

    void writeCell(XlsxCellAddress address, XlsxCell cell);

    void writeRowProperty(XlsxRowAddress address, XlsxRowProperty property);

    void writeColumnProperty(XlsxColumnAddress address, XlsxColumnProperty property);

    void write(OutputStream outputStream) throws IOException;
}
