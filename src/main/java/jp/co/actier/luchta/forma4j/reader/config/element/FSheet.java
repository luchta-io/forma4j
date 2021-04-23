package jp.co.actier.luchta.forma4j.reader.config.element;

import jp.co.actier.luchta.forma4j.reader.config.element.property.ElementName;
import jp.co.actier.luchta.forma4j.reader.config.element.property.SheetIndex;

public class FSheet extends Element {
    
    public SheetIndex sheetIndex;
    
    public FSheet() {}
    
    public FSheet(SheetIndex sheetIndex) {
        this.sheetIndex = sheetIndex;
    }
    
    public SheetIndex getSheetIndex() {
        return this.sheetIndex;
    }

    @Override
    public ElementName getName() {
        return new ElementName("sheet");
    }
}
