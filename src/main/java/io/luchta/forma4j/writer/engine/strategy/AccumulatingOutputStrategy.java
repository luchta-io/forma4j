package io.luchta.forma4j.writer.engine.strategy;

import io.luchta.forma4j.writer.engine.buffer.accumulater.BuildAccumulator;
import io.luchta.forma4j.writer.engine.model.cell.XlsxCell;
import io.luchta.forma4j.writer.engine.model.cell.address.XlsxCellAddress;
import io.luchta.forma4j.writer.engine.model.cell.address.XlsxSheetName;
import io.luchta.forma4j.writer.engine.model.column.XlsxColumnAddress;
import io.luchta.forma4j.writer.engine.model.column.property.XlsxColumnProperty;
import io.luchta.forma4j.writer.engine.model.row.address.XlsxRowAddress;
import io.luchta.forma4j.writer.engine.model.row.property.XlsxRowProperty;
import io.luchta.forma4j.writer.processor.poi.WorkbookBuilder;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.IOException;
import java.io.OutputStream;

/** 既存の DOM 用データを蓄積する出力戦略です。 */
public class AccumulatingOutputStrategy implements XlsxOutputStrategy {
    private final BuildAccumulator accumulator = new BuildAccumulator();

    public BuildAccumulator accumulator() {
        return accumulator;
    }

    @Override
    public void startSheet(XlsxSheetName sheetName, Boolean autoSizeColumnEnabled) {
        accumulator.add(sheetName, autoSizeColumnEnabled);
    }

    @Override
    public void finishSheet(XlsxSheetName sheetName) {
        // DOM 出力ではシート全体を最後に組み立てるため処理は不要です。
    }

    @Override
    public void writeCell(XlsxCellAddress address, XlsxCell cell) {
        accumulator.put(address, cell);
    }

    @Override
    public void writeRowProperty(XlsxRowAddress address, XlsxRowProperty property) {
        accumulator.putRowProperty(address, property);
    }

    @Override
    public void writeColumnProperty(XlsxColumnAddress address, XlsxColumnProperty property) {
        accumulator.putColumnProperties(address, property);
    }

    @Override
    public void write(OutputStream outputStream) throws IOException {
        try (Workbook workbook = new WorkbookBuilder(accumulator).build()) {
            workbook.write(outputStream);
        }
    }
}
