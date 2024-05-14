package io.luchta.forma4j.writer.engine.model.cell;

import io.luchta.forma4j.writer.engine.model.cell.address.XlsxCellAddress;
import io.luchta.forma4j.writer.engine.model.cell.address.XlsxColumnNumber;
import io.luchta.forma4j.writer.engine.model.cell.style.XlsxCellStyle;
import io.luchta.forma4j.writer.engine.model.cell.value.XlsxCellValue;

public class XlsxCell {
    XlsxCellAddress address;
    XlsxCellValue value;
    XlsxCellStyle style;

    public XlsxCell(XlsxCellAddress address, XlsxCellValue value, XlsxCellStyle style) {
        this.address = address;
        this.value = value;
        this.style = style;
    }

    public XlsxCellAddress address() {
        return address;
    }

    public XlsxColumnNumber columnNumber() {
        return address.columnNumber();
    }

    public XlsxCellValue value() {
        return value;
    }
    
    public XlsxCellStyle style() {
    	return style;
    }

    public boolean isEmpty() {
        return value.isEmpty();
    }
}
