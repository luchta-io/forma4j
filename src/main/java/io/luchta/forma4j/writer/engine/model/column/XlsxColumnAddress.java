package io.luchta.forma4j.writer.engine.model.column;

import io.luchta.forma4j.writer.engine.model.cell.address.XlsxColumnNumber;
import io.luchta.forma4j.writer.engine.model.cell.address.XlsxSheetName;

import java.util.Objects;

public class XlsxColumnAddress {
    XlsxSheetName sheetName = new XlsxSheetName();
    XlsxColumnNumber columnNumber = new XlsxColumnNumber();

    public XlsxColumnAddress() {}

    public XlsxColumnAddress(XlsxSheetName sheetName, XlsxColumnNumber columnNumber) {
        this.sheetName = sheetName;
        this.columnNumber = columnNumber;
    }

    public boolean equalSheetName(XlsxSheetName sheetName) {
        return sheetName.equals(this.sheetName);
    }

    public XlsxSheetName sheetName() {
        return sheetName;
    }

    public XlsxColumnNumber columnNumber() {
        return columnNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof XlsxColumnAddress)) return false;
        XlsxColumnAddress that = (XlsxColumnAddress) o;
        return Objects.equals(sheetName, that.sheetName) && Objects.equals(columnNumber, that.columnNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sheetName, columnNumber);
    }
}
