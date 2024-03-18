package io.luchta.forma4j.writer.engine.model.column.property;

import java.util.ArrayList;
import java.util.List;

public class XlsxColumnStyle {
    private List<XlsxColumnProperty> properties = new ArrayList<>();

    public XlsxColumnStyle() {
    }

    public XlsxColumnStyle(List<XlsxColumnProperty> properties) {
        this.properties = properties;
    }
}
