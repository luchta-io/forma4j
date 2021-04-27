package io.luchta.forma4j.reader.config.element;

import io.luchta.forma4j.reader.config.element.property.ElementName;
import io.luchta.forma4j.reader.config.element.property.RowNumber;

public class FBody extends Element {

    private RowNumber startRow;
    
    public FBody() {}
    public FBody(RowNumber startRow) {
        this.startRow = startRow;
    }
    
    public RowNumber getStartRow() {
        return this.startRow;
    }
    
    @Override
    public ElementName getName() {
        return new ElementName("body");
    }
}
