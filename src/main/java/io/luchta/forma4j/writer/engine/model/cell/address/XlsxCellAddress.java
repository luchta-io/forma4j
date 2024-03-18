package io.luchta.forma4j.writer.engine.model.cell.address;

import java.util.Objects;

public class XlsxCellAddress {
    XlsxSheetName sheetName = new XlsxSheetName();
    XlsxRowNumber rowNumber = XlsxRowNumber.init();
    XlsxColumnNumber columnNumber = XlsxColumnNumber.init();

    public XlsxCellAddress() {
    }

    public XlsxCellAddress(XlsxSheetName sheetName, XlsxRowNumber rowNumber, XlsxColumnNumber columnNumber) {
        this.sheetName = sheetName;
        this.rowNumber = rowNumber;
        this.columnNumber = columnNumber;
    }

    public boolean is(XlsxSheetName other) {
        return sheetName.equals(other);
    }

    public boolean is(XlsxRowNumber other) {
        return rowNumber.equals(other);
    }

    public XlsxSheetName sheetName() {
        return sheetName;
    }

    public XlsxRowNumber rowNumber() {
        return rowNumber;
    }

    public XlsxColumnNumber columnNumber() {
        return columnNumber;
    }

    public XlsxCellAddress with(XlsxSheetName sheetName) {
        return new XlsxCellAddress(sheetName, rowNumber, columnNumber);
    }

    public XlsxCellAddress with(XlsxRowNumber rowNumber) {
        return new XlsxCellAddress(sheetName, rowNumber, columnNumber);
    }

    public XlsxCellAddress with(XlsxColumnNumber columnNumber) {
        return new XlsxCellAddress(sheetName, rowNumber, columnNumber);
    }

    public XlsxCellAddress with(XlsxRowNumber rowNumber, XlsxColumnNumber columnNumber) {
        return new XlsxCellAddress(sheetName, rowNumber, columnNumber);
    }

    public XlsxCellAddress columnNumberIncrement() {
        return new XlsxCellAddress(sheetName, rowNumber, columnNumber.increment());
    }

    public XlsxCellAddress rowNumberIncrement() {
        return new XlsxCellAddress(sheetName, rowNumber.increment(), columnNumber);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof XlsxCellAddress)) return false;
        XlsxCellAddress that = (XlsxCellAddress) o;
        return Objects.equals(sheetName, that.sheetName) && Objects.equals(rowNumber, that.rowNumber) && Objects.equals(columnNumber, that.columnNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sheetName, rowNumber, columnNumber);
    }
}
