package io.luchta.forma4j.reader.config.element;

import io.luchta.forma4j.reader.config.element.property.ColNumber;
import io.luchta.forma4j.reader.config.element.property.ElementName;

public class FCol extends Element {

    private ColNumber num;
    private ElementName name;
    
    public FCol() {}
    
    public FCol(ColNumber num, ElementName name) {
        this.num = num;
        this.name = name;
    }
    
    public ColNumber getNum() {
        return this.num;
    }
    
    @Override
    public ElementName getName() {
        return this.name;
    }
}
