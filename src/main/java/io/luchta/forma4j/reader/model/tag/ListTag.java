package io.luchta.forma4j.reader.model.tag;

import io.luchta.forma4j.reader.model.tag.property.Index;

public class ListTag implements Tag {
    Index headerStartRow;
    Index headerStartCol;
    Index detailStartRow;
    Index detailStartCol;

    public ListTag(Index headerStartRow, Index headerStartCol, Index detailStartRow, Index detailStartCol) {
        this.headerStartRow = headerStartRow;
        this.headerStartCol = headerStartCol;
        this.detailStartRow = detailStartRow;
        this.detailStartCol = detailStartCol;
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

    @Override
    public boolean isList() {
        return true;
    }
}
