package io.luchta.forma4j.reader.model.tag;

import io.luchta.forma4j.reader.model.tag.property.Index;
import io.luchta.forma4j.reader.model.tag.property.Name;

public class ListTag implements Tag {
    Index headerStartRow;
    Index headerStartCol;
    Index detailStartRow;
    Index detailStartCol;
    Name name;

    public ListTag(Index headerStartRow, Index headerStartCol, Index detailStartRow, Index detailStartCol, Name name) {
        this.headerStartRow = headerStartRow;
        this.headerStartCol = headerStartCol;
        this.detailStartRow = detailStartRow;
        this.detailStartCol = detailStartCol;
        this.name = name;
    }

    public Index getHeaderStartRow() {
        return headerStartRow;
    }

    public Index getHeaderStartCol() {
        return headerStartCol;
    }

    public Index getDetailStartRow() {
        return detailStartRow;
    }

    public Index getDetailStartCol() {
        return detailStartCol;
    }

    public Name name() {
        return name;
    }

    @Override
    public boolean isList() {
        return true;
    }
}
