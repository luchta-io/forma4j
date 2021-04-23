package jp.co.actier.luchta.forma4j.reader.config.element;

import jp.co.actier.luchta.forma4j.reader.config.element.property.ElementName;

public class FRoot extends Element {

    @Override
    public ElementName getName() {
        return new ElementName("luchta-reader");
    }
}
