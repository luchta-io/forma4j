package io.luchta.forma4j.writer.engine.model.cell.style;

import org.apache.poi.ss.usermodel.CellStyle;

import java.util.Objects;
import java.util.logging.Logger;

public class NotSupportProperty implements XlsxCellStyleProperty {
    Logger logger = Logger.getLogger(NotSupportProperty.class.getName());

    String name;
    String value;

    public NotSupportProperty(String name, String value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public void overwriteTo(CellStyle cellStyle) {
        // do nothing
        logger.warning("not support property [" + name + ":" + value + ";]");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NotSupportProperty that = (NotSupportProperty) o;
        return Objects.equals(name, that.name) && Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, value);
    }
}
