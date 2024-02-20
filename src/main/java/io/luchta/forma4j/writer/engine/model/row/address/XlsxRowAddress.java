package io.luchta.forma4j.writer.engine.model.row.address;

import io.luchta.forma4j.writer.engine.model.cell.address.XlsxRowNumber;
import io.luchta.forma4j.writer.engine.model.cell.address.XlsxSheetName;

import java.util.Objects;

public class XlsxRowAddress {
    XlsxSheetName sheetName = new XlsxSheetName();
    XlsxRowNumber rowNumber = new XlsxRowNumber();

    public XlsxRowAddress() {}

    public XlsxRowAddress(XlsxSheetName sheetName, XlsxRowNumber rowNumber) {
        this.sheetName = sheetName;
        this.rowNumber = rowNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof XlsxRowAddress)) return false;
        XlsxRowAddress that = (XlsxRowAddress) o;
        return Objects.equals(sheetName, that.sheetName) && Objects.equals(rowNumber, that.rowNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sheetName, rowNumber);
    }
}
