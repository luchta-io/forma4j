package io.luchta.forma4j.reader.specification.excel;

import io.luchta.forma4j.reader.model.excel.Header;
import io.luchta.forma4j.reader.model.excel.Index;

public class RetrieveHeaderValueSpec {
    Index colIndex;

    public RetrieveHeaderValueSpec(Index colIndex) {
        this.colIndex = colIndex;
    }

    public boolean isSatisfiedBy(Header header) {
        return header.getColIndex().equals(colIndex);
    }
}
