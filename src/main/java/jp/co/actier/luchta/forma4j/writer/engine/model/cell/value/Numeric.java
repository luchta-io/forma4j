package jp.co.actier.luchta.forma4j.writer.engine.model.cell.value;

public class Numeric {
    Long value;

    public Numeric() {
    }

    public Numeric(Long value) {
        this.value = value;
    }

    @Override
    public String toString() {
        if (value == null) return "";
        return value.toString();
    }
}
