package io.luchta.forma4j.writer.engine.model.cell.style;

import org.apache.poi.ss.usermodel.BorderStyle;

public class XlsxBorderStyle implements XlsxCellStyle {
	public static final String NAME = "BORDER";

	private BorderStyle borderStyle = BorderStyle.NONE;
	
	public XlsxBorderStyle() {}
	
	public XlsxBorderStyle(BorderStyle borderStyle) {
		this.borderStyle = borderStyle;
	}

	public BorderStyle getBorderStyle() {
		return borderStyle;
	}

	@Override
	public boolean isBorder() {
		return true;
	}
}
