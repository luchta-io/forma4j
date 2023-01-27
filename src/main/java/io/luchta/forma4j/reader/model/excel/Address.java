package io.luchta.forma4j.reader.model.excel;

import java.util.Objects;

public class Address {
    private Index rowIndex;
    private Index columnIndex;

    public Address(Index rowIndex, Index columnIndex) {
        this.rowIndex = rowIndex;
        this.columnIndex = columnIndex;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return Objects.equals(rowIndex, address.rowIndex) && Objects.equals(columnIndex, address.columnIndex);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rowIndex, columnIndex);
    }
}
