package io.luchta.forma4j.reader.model.tag;

import io.luchta.forma4j.reader.model.tag.property.Index;
import io.luchta.forma4j.reader.model.tag.property.Name;

public class HeaderTag implements Tag {

    Index rowIndex;
    Index colIndex;
    Name name;

    public HeaderTag(Index rowIndex, Index colIndex, Name name) {
        this.rowIndex = rowIndex;
        this.colIndex = colIndex;
        this.name = name;
    }

    public Index getRowIndex() {
        return rowIndex;
    }

    public Index getColIndex() {
        return colIndex;
    }

    public Name name() {
        return name;
    }

    @Override
    public boolean isHeader() {
        return true;
    }
}
