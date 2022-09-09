package io.luchta.forma4j.writer.engine.model.cell.style;

public interface XlsxCellStyle {
	default boolean isBorder() {
		return false;
	}
}
