package io.luchta.forma4j.writer.engine.model.sheet;

import io.luchta.forma4j.writer.definition.schema.attribute.Name;
import io.luchta.forma4j.writer.engine.buffer.accumulater.support.ColumnPropertyMap;
import io.luchta.forma4j.writer.engine.model.cell.XlsxCell;
import io.luchta.forma4j.writer.engine.model.cell.XlsxCellList;
import io.luchta.forma4j.writer.engine.model.cell.address.XlsxCellAddress;
import io.luchta.forma4j.writer.engine.model.cell.address.XlsxColumnNumber;
import io.luchta.forma4j.writer.engine.model.cell.address.XlsxRowNumber;
import io.luchta.forma4j.writer.engine.model.cell.address.XlsxSheetName;
import io.luchta.forma4j.writer.engine.model.cell.style.XlsxCellStyle;
import io.luchta.forma4j.writer.engine.model.cell.value.Text;
import io.luchta.forma4j.writer.engine.model.row.XlsxRow;
import io.luchta.forma4j.writer.engine.model.row.XlsxRowList;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class XlsxSheetTest {
    @Test
    void columnMetadataIsCalculatedOnceFromCellAddresses() {
        XlsxSheetName sheetName = new XlsxSheetName(new Name("sheet1"));
        XlsxCellStyle style = new XlsxCellStyle();

        XlsxCell nonEmptyCell = new XlsxCell(
                new XlsxCellAddress(sheetName, new XlsxRowNumber(0L), new XlsxColumnNumber(5L)),
                new Text("value"),
                style,
                null
        );
        XlsxCell emptyCell = new XlsxCell(
                new XlsxCellAddress(sheetName, new XlsxRowNumber(1L), new XlsxColumnNumber(5L)),
                new Text(""),
                style,
                null
        );

        XlsxRowList rows = new XlsxRowList(List.of(
                new XlsxRow(new XlsxRowNumber(0L), new XlsxCellList(List.of(nonEmptyCell))),
                new XlsxRow(new XlsxRowNumber(1L), new XlsxCellList(List.of(emptyCell)))
        ));

        XlsxSheet sheet = new XlsxSheet(sheetName, rows, new ColumnPropertyMap());

        Assertions.assertEquals(6, sheet.columnSize());
        Assertions.assertFalse(sheet.isEmptyColumn(5));
        Assertions.assertTrue(sheet.isEmptyColumn(4));
    }
}
