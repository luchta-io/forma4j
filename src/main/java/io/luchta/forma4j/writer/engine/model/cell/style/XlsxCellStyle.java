package io.luchta.forma4j.writer.engine.model.cell.style;

import io.luchta.forma4j.writer.processor.poi.CellStyleBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class XlsxCellStyle {

    private List<XlsxCellStyleProperty> properties = new ArrayList<>();

    public XlsxCellStyle() {
    }

    public XlsxCellStyle(List<XlsxCellStyleProperty> properties) {
        this.properties = properties;
    }

    public void accept(CellStyleBuilder builder) {
        for (XlsxCellStyleProperty property : properties) {
            property.accept(builder);
        }
    }

    public void add(XlsxCellStyleProperty property) {
        properties.add(property);
    }

    public boolean isEmpty() {
        return properties.isEmpty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        XlsxCellStyle that = (XlsxCellStyle) o;
        return Objects.equals(properties, that.properties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(properties);
    }
}
