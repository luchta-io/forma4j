package io.luchta.forma4j.writer.engine.model.cell.style;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;

public class XlsxBorderStyle implements XlsxCellStyle {

	private XlsxBorderType borderType = XlsxBorderType.NONE;
	
	public XlsxBorderStyle() {}
	
	public XlsxBorderStyle(XlsxBorderType borderType) {
		this.borderType = borderType;
	}
	
	public boolean isNone() {
		return this.borderType == XlsxBorderType.NONE;
	}
	
	public boolean isSolid() {
		return this.borderType == XlsxBorderType.SOLID;
	}

	@Override
	public Cell addCellStyle(Cell cell) {
		CellStyle cellStyle = cell.getCellStyle();
		if (isSolid()) {
			cellStyle.setBorderTop(BorderStyle.THIN);
			cellStyle.setBorderBottom(BorderStyle.THIN);
			cellStyle.setBorderLeft(BorderStyle.THIN);
			cellStyle.setBorderRight(BorderStyle.THIN);
		} else {
			cellStyle.setBorderTop(BorderStyle.NONE);
			cellStyle.setBorderBottom(BorderStyle.NONE);
			cellStyle.setBorderLeft(BorderStyle.NONE);
			cellStyle.setBorderRight(BorderStyle.NONE);
		}
		cell.setCellStyle(cellStyle);
		return cell;
	}
}
