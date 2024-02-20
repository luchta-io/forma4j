package io.luchta.forma4j.writer.engine.model.column;

import io.luchta.forma4j.writer.engine.model.cell.address.XlsxColumnNumber;

public class XlsxColumnRange {
    XlsxColumnNumber firstColumnNumber;
    XlsxColumnNumber lastColumnNumber;

    public XlsxColumnRange() {
        firstColumnNumber = new XlsxColumnNumber();
        lastColumnNumber = new XlsxColumnNumber();
    }

    public XlsxColumnRange(XlsxColumnNumber firstColumnNumber, XlsxColumnNumber lastColumnNumber) {
        this.firstColumnNumber = firstColumnNumber;
        this.lastColumnNumber = lastColumnNumber;
    }

    public XlsxColumnNumber firstColumnNumber() {
        return firstColumnNumber;
    }

    public XlsxColumnNumber lastColumnNumber() {
        return lastColumnNumber;
    }
}
