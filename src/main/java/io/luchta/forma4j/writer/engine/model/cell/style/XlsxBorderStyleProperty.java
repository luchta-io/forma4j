package io.luchta.forma4j.writer.engine.model.cell.style;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;

import java.util.Objects;

public class XlsxBorderStyleProperty implements XlsxCellStyleProperty {
	public static final String NAME = "BORDER";

	private BorderStyle borderStyle = BorderStyle.NONE;
	
	public XlsxBorderStyleProperty() {}
	
	public XlsxBorderStyleProperty(BorderStyle borderStyle) {
		this.borderStyle = borderStyle;
	}

	public BorderStyle getBorderStyle() {
		return borderStyle;
	}

	@Override
	public void overwriteTo(CellStyle cellStyle) {
		cellStyle.setBorderLeft(borderStyle);
		cellStyle.setBorderTop(borderStyle);
		cellStyle.setBorderRight(borderStyle);
		cellStyle.setBorderBottom(borderStyle);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		XlsxBorderStyleProperty that = (XlsxBorderStyleProperty) o;
		return borderStyle == that.borderStyle;
	}

	@Override
	public int hashCode() {
		return Objects.hash(borderStyle);
	}
}
