package jp.co.actier.luchta.forma4j.writer.engine.model.cell.style;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;

public class XlsxFontColorStyle implements XlsxCellStyle {

	private XlsxFontColorType fontColorType = XlsxFontColorType.BLACK;
	
	public XlsxFontColorStyle() {}
	
	public XlsxFontColorStyle(XlsxFontColorType fontColorType) {
		this.fontColorType = fontColorType;
	}
	
	public boolean isBlack() {
		return this.fontColorType == XlsxFontColorType.BLACK;
	}
	
	public boolean isWhite() {
		return this.fontColorType == XlsxFontColorType.WHITE;
	}
	
	public boolean isRed() {
		return this.fontColorType == XlsxFontColorType.RED;
	}
	
	public boolean isBlue() {
		return this.fontColorType == XlsxFontColorType.BLUE;
	}
	
	public boolean isGreen() {
		return this.fontColorType == XlsxFontColorType.GREEN;
	}
	
	public boolean isYellow() {
		return this.fontColorType == XlsxFontColorType.YELLOW;
	}

	@Override
	public Cell addCellStyle(Cell cell) {
		Font font = cell.getSheet().getWorkbook().getFontAt(cell.getCellStyle().getFontIndexAsInt());
		if (isBlack()) {
			font.setColor(IndexedColors.BLACK.index);
		} else if (isWhite()) {
			font.setColor(IndexedColors.WHITE.index);
		} else if (isRed()) {
			font.setColor(IndexedColors.RED.index);
		} else if (isBlue()) {
			font.setColor(IndexedColors.BLUE.index);
		} else if (isGreen()) {
			font.setColor(IndexedColors.GREEN.index);
		} else if (isYellow()) {
			font.setColor(IndexedColors.YELLOW.index);
		} else {
			font.setColor(IndexedColors.BLACK.index);
		}
		CellStyle cellStyle = cell.getCellStyle();
		cellStyle.setFont(font);
		cell.setCellStyle(cellStyle);
		return cell;
	}
}
