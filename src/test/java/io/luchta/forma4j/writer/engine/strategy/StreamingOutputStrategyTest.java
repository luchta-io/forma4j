package io.luchta.forma4j.writer.engine.strategy;

import io.luchta.forma4j.writer.definition.schema.attribute.Name;
import io.luchta.forma4j.writer.engine.model.cell.XlsxCell;
import io.luchta.forma4j.writer.engine.model.cell.address.XlsxCellAddress;
import io.luchta.forma4j.writer.engine.model.cell.address.XlsxColumnNumber;
import io.luchta.forma4j.writer.engine.model.cell.address.XlsxRowNumber;
import io.luchta.forma4j.writer.engine.model.cell.address.XlsxSheetName;
import io.luchta.forma4j.writer.engine.model.cell.style.XlsxCellStyle;
import io.luchta.forma4j.writer.engine.model.cell.value.Text;
import org.apache.poi.ss.usermodel.Cell;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StreamingOutputStrategyTest {
    @Test
    void deletesTemporaryDirectoryWhenClosed() throws Exception {
        TestStreamingOutputStrategy strategy = new TestStreamingOutputStrategy();
        Path temporaryDirectory = strategy.temporaryDirectory();
        assertTrue(Files.exists(temporaryDirectory));

        strategy.close();

        assertFalse(Files.exists(temporaryDirectory));
    }

    @Test
    void acceptsWritesToEarlierRowsAndUsesTheLastValue() throws Exception {
        StreamingOutputStrategy strategy = new StreamingOutputStrategy();
        XlsxSheetName sheet = new XlsxSheetName(new Name("result"));
        strategy.startSheet(sheet, null);
        for (int row = 0; row <= 5000; row++) {
            strategy.writeCell(address(sheet, row), cell(sheet, row));
        }

        XlsxCellAddress first = address(sheet, 0);
        strategy.writeCell(
                first,
                new XlsxCell(first, new Text("replacement"), new XlsxCellStyle(), null)
        );
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        strategy.write(output);

        try (XSSFWorkbook workbook = new XSSFWorkbook(
                new ByteArrayInputStream(output.toByteArray()))) {
            Cell firstCell = workbook.getSheet("result").getRow(0).getCell(0);
            Cell lastCell = workbook.getSheet("result").getRow(5000).getCell(0);
            assertEquals(
                    "replacement",
                    firstCell.getStringCellValue()
            );
            assertEquals(
                    "value",
                    lastCell.getStringCellValue()
            );
            assertNotEquals(0, firstCell.getCellStyle().getIndex());
            assertEquals(firstCell.getCellStyle().getIndex(), lastCell.getCellStyle().getIndex());
            XSSFFont font = workbook.getFontAt(firstCell.getCellStyle().getFontIndexAsInt());
            assertNull(font.getXSSFColor());
        }
    }

    private XlsxCellAddress address(XlsxSheetName sheet, int row) {
        return new XlsxCellAddress(sheet, new XlsxRowNumber((long) row), XlsxColumnNumber.init());
    }

    private XlsxCell cell(XlsxSheetName sheet, int row) {
        XlsxCellAddress address = address(sheet, row);
        return new XlsxCell(address, new Text("value"), new XlsxCellStyle(), null);
    }

    private static final class TestStreamingOutputStrategy extends StreamingOutputStrategy {
        private Path temporaryDirectory() {
            return temporaryDirectory;
        }
    }
}
