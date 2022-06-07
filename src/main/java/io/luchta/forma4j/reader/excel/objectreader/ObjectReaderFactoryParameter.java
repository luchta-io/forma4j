package io.luchta.forma4j.reader.excel.objectreader;

import io.luchta.forma4j.reader.model.excel.Header;
import io.luchta.forma4j.reader.model.excel.Index;
import io.luchta.forma4j.reader.model.tag.Tag;
import io.luchta.forma4j.reader.model.tag.TagTree;
import org.apache.poi.ss.usermodel.Sheet;

public class ObjectReaderFactoryParameter {
    Sheet sheet;
    Index rowIndex;
    Index colIndex;
    Header header;
    TagTree tagTree;
    Tag tag;

    public ObjectReaderFactoryParameter(Sheet sheet, Index rowIndex, Index colIndex, Header header, TagTree tagTree, Tag tag) {
        this.sheet = sheet;
        this.rowIndex = rowIndex;
        this.colIndex = colIndex;
        this.header = header;
        this.tagTree = tagTree;
        this.tag = tag;
    }

    public Sheet getSheet() {
        return sheet;
    }

    public Index getRowIndex() {
        return rowIndex;
    }

    public Index getColIndex() {
        return colIndex;
    }

    public Header getHeader() {
        return header;
    }

    public TagTree getTagTree() {
        return tagTree;
    }

    public Tag getTag() {
        return tag;
    }
}
