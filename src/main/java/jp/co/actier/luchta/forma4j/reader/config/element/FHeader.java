package jp.co.actier.luchta.forma4j.reader.config.element;

import jp.co.actier.luchta.forma4j.reader.config.element.property.ElementName;
import jp.co.actier.luchta.forma4j.reader.config.element.property.RowNumber;

public class FHeader extends Element {

    private RowNumber startRow;
    private RowNumber endRow;
    
    public FHeader() {}
    public FHeader(RowNumber startRow, RowNumber endRow) {
        this.startRow = startRow;
        this.endRow = endRow;
    }
    
    public RowNumber getStartRow() {
        return this.startRow;
    }
    
    public RowNumber getEndRow() {
        return this.endRow;
    }
    
    @Override
    public ElementName getName() {
        return new ElementName("header");
    }
}
