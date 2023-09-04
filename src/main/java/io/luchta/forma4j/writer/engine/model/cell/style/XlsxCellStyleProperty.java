package io.luchta.forma4j.writer.engine.model.cell.style;

public interface XlsxCellStyleProperty {
   // TODO propertyのtypeを識別するためのメソッドは不要になるはず（対応プロパティが増えてくるとこの仕組みだと扱いづらくなる）
	default boolean isBorder() {
		return false;
	}
}
