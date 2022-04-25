package io.luchta.forma4j.writer.engine.model.cell;

import io.luchta.forma4j.writer.engine.model.cell.address.XlsxCellAddress;
import io.luchta.forma4j.writer.engine.model.cell.address.XlsxColumnNumber;
import io.luchta.forma4j.writer.engine.model.cell.style.XlsxCellStyles;
import io.luchta.forma4j.writer.engine.model.cell.value.XlsxCellValue;

public class XlsxCell {
    XlsxCellAddress address;
    XlsxCellValue value;
    XlsxCellStyles styles;

    public XlsxCell(XlsxCellAddress address, XlsxCellValue value, XlsxCellStyles styles) {
        this.address = address;
        this.value = value;
        this.styles = styles;
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
    
    public XlsxCellStyles styles() {
    	return styles;
    }
}
