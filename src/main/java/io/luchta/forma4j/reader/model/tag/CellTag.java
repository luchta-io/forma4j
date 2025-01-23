package io.luchta.forma4j.reader.model.tag;

import io.luchta.forma4j.reader.model.tag.property.DisplayValue;
import io.luchta.forma4j.reader.model.tag.property.Index;
import io.luchta.forma4j.reader.model.tag.property.Name;

public class CellTag implements Tag {

    Index row;
    Boolean rowIsUndefined;
    Index col;
    Boolean colIsUndefined;
    Name name;
    DisplayValue displayValue;

    public CellTag(Index row, Boolean rowIsUndefined, Index col, Boolean colIsUndefined, Name name, DisplayValue displayValue) {
        this.row = row;
        this.rowIsUndefined = rowIsUndefined;
        this.col = col;
        this.colIsUndefined = colIsUndefined;
        this.name = name;
        this.displayValue = displayValue;
    }

    public Index row() {
        return row;
    }

    public boolean rowIsUndefined() {
        return rowIsUndefined;
    }

    public Index col() {
        return col;
    }

    public boolean colIsUndefined() {
        return colIsUndefined;
    }

    public Name name() {
        return name;
    }

    public DisplayValue displayValue() {
        return displayValue;
    }

    @Override
    public boolean isCell() {
        return true;
    }
}
