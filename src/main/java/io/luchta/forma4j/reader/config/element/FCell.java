package io.luchta.forma4j.reader.config.element;

import io.luchta.forma4j.reader.config.element.property.ColNumber;
import io.luchta.forma4j.reader.config.element.property.ElementName;
import io.luchta.forma4j.reader.config.element.property.RowNumber;

public class FCell extends Element {
    
    private RowNumber row;
    private ColNumber col;
    private ElementName name;
    
    public FCell() {}
    
    public FCell(RowNumber row, ColNumber col, ElementName name) {
        this.row = row;
        this.col = col;
        this.name = name;
    }
    
    public RowNumber getRow() {
        return this.row;
    }
    
    public ColNumber getCol() {
        return this.col;
    }
    
    @Override
    public ElementName getName() {
        return this.name;
    }
}
