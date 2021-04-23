package jp.co.actier.luchta.forma4j.writer.engine.model.cell.style;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class XlsxCellStyles implements Iterable<XlsxCellStyle> {

	private List<XlsxCellStyle> list = new ArrayList<>();
	
	public XlsxCellStyles() {}
	
	public XlsxCellStyles(List<XlsxCellStyle> list) {
		this.list = list;
	}

	@Override
	public Iterator<XlsxCellStyle> iterator() {
		return this.list.iterator();
	}
}
