package io.luchta.forma4j.writer.engine.model.cell.style;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class XlsxCellStyle implements Iterable<XlsxCellStyleProperty> {

	private List<XlsxCellStyleProperty> properties = new ArrayList<>();
	
	public XlsxCellStyle() {}
	
	public XlsxCellStyle(List<XlsxCellStyleProperty> properties) {
		this.properties = properties;
	}

	public int size() {
		return properties.size();
	}

	@Override
	public Iterator<XlsxCellStyleProperty> iterator() {
		return this.properties.iterator();
	}
}
