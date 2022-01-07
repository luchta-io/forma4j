package io.luchta.forma4j.reader.model.tag;

import io.luchta.forma4j.reader.model.tag.property.Index;
import io.luchta.forma4j.reader.model.tag.property.Name;

public class CellTag implements Tag {

    Index row;
    Boolean rowIsUndefined;
    Index col;
    Boolean colIsUndefined;
    Name name;

    public CellTag(Index row, Boolean rowIsUndefined, Index col, Boolean colIsUndefined, Name name) {
        this.row = row;
        this.rowIsUndefined = rowIsUndefined;
        this.col = col;
        this.colIsUndefined = colIsUndefined;
        this.name = name;
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

    @Override
    public boolean isCell() {
        return true;
    }
}
