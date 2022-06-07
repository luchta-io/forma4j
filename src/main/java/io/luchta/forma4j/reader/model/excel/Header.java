package io.luchta.forma4j.reader.model.excel;

public class Header {
    Index colIndex;
    HeaderValue headerValue;

    public Header() {
        colIndex = new Index();
        headerValue = new HeaderValue();
    }

    public Header(Index colIndex, HeaderValue headerValue) {
        this.colIndex = colIndex;
        this.headerValue = headerValue;
    }

    public Index getColIndex() {
        return colIndex;
    }

    public HeaderValue getHeaderValue() {
        return headerValue;
    }
}
