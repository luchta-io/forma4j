package io.luchta.forma4j.writer.engine.model.cell.style.border;

import io.luchta.forma4j.writer.processor.poi.CellStyleBuilder;

public class BorderProperty extends Border {
    public static final String NAME = "BORDER";

    public BorderProperty(String value) {
        super(value);
    }

    /**
     * 罫線のスタイルを設定する
     * @param builder CellStyleBuilder
     */
    @Override
    public void accept(CellStyleBuilder builder) {
        builder.setBorder(borderStyle());
    }
}
