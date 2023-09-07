package io.luchta.forma4j.writer.engine.model.cell.style;

import org.apache.poi.ss.usermodel.CellStyle;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class XlsxCellStyle {

	private List<XlsxCellStyleProperty> properties = new ArrayList<>();
	
	public XlsxCellStyle() {}
	
	public XlsxCellStyle(List<XlsxCellStyleProperty> properties) {
		this.properties = properties;
	}

	public void overwriteTo(CellStyle cellStyle) {
		// TODO このWrapTextをデフォルトで行うのはイマイチな気がするので仕様を見直す
		cellStyle.setWrapText(true);
		for (XlsxCellStyleProperty property : properties) {
			property.overwriteTo(cellStyle);
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		XlsxCellStyle that = (XlsxCellStyle) o;
		return Objects.equals(properties, that.properties);
	}

	@Override
	public int hashCode() {
		return Objects.hash(properties);
	}
}
