package io.luchta.forma4j.writer.engine.model.cell;

import io.luchta.forma4j.writer.engine.model.cell.address.XlsxCellAddress;
import io.luchta.forma4j.writer.engine.model.cell.address.XlsxColumnNumber;
import io.luchta.forma4j.writer.engine.model.cell.style.XlsxCellStyle;
import io.luchta.forma4j.writer.engine.model.cell.style.XlsxCellType;
import io.luchta.forma4j.writer.engine.model.cell.value.XlsxCellValue;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class XlsxCell {
    XlsxCellAddress address;
    XlsxCellValue<?> value;
    XlsxCellStyle style;
    XlsxCellType type;

    public XlsxCell(XlsxCellAddress address, XlsxCellValue<?> value, XlsxCellStyle style, XlsxCellType type) {
        this.address = address;
        this.value = value;
        this.style = style;
        this.type = type;
    }

    public XlsxCellAddress address() {
        return address;
    }

    public XlsxColumnNumber columnNumber() {
        return address.columnNumber();
    }

    public XlsxCellValue<?> value() {
        return value;
    }
    
    public XlsxCellStyle style() {
    	return style;
    }

    public boolean isEmpty() {
        return value.isEmpty();
    }

    public boolean isBoolean() {
        if (type == XlsxCellType.BOOLEAN) return true;
        if (type == null) return value.isBoolean();
        return false;
    }

    public boolean isDate() {
        if (type == XlsxCellType.DATE) return true;
        if (type == null) return value.isDate();
        return false;
    }

    public boolean isDateTime() {
        if (type == XlsxCellType.DATETIME) return true;
        if (type == null) return value.isDateTime();
        return false;
    }

    public boolean isFormula() {
        if (type == XlsxCellType.FORMULA) return true;
        if (type == null) return value.toString().startsWith("=");
        return false;
    }

    public boolean isNumeric() {
        if (type == XlsxCellType.NUMERIC) return true;
        if (type == null) return value.isNumeric();
        return false;
    }

    public boolean isText() {
        if (type == XlsxCellType.STRING) return true;
        return value.isText();
    }

    public boolean toBoolean() {
        Object obj = value.toValue();
        if (obj instanceof Boolean) {
            return (Boolean) obj;
        }
        return Boolean.parseBoolean(obj.toString());
    }

    public LocalDate toDate() {
        Object obj = value.toValue();
        if (obj instanceof LocalDate) {
            return (LocalDate) obj;
        }
        return LocalDate.parse(obj.toString(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    public LocalDateTime toDateTime() {
        Object obj = value.toValue();
        if (obj instanceof LocalDateTime) {
            return (LocalDateTime) obj;
        }
        return LocalDateTime.parse(obj.toString(), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS"));
    }

    public String toFormula() {
        if (value.toString().startsWith("=")) {
            return value.toString();
        }
        return "=" + value.toString();
    }

    public double toNumeric() {
        Object obj = value.toValue();
        if (obj instanceof Number) {
            return ((BigDecimal) obj).doubleValue();
        }
        return new BigDecimal(obj.toString()).doubleValue();
    }

    public String toText() {
        return value.toValue().toString();
    }
}
