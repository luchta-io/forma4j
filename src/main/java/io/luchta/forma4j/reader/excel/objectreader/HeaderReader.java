package io.luchta.forma4j.reader.excel.objectreader;

import io.luchta.forma4j.context.databind.json.JsonObject;
import io.luchta.forma4j.reader.excel.Accumulator;
import io.luchta.forma4j.reader.model.excel.Header;
import io.luchta.forma4j.reader.model.excel.HeaderValue;
import io.luchta.forma4j.reader.model.excel.Headers;
import io.luchta.forma4j.reader.model.excel.Index;
import io.luchta.forma4j.reader.model.tag.HeaderTag;
import io.luchta.forma4j.reader.model.tag.TagTree;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.ArrayList;
import java.util.List;

public class HeaderReader implements ObjectReader {

    Sheet sheet;
    TagTree tagTree;

    public HeaderReader(Sheet sheet, TagTree tagTree) {
        this.sheet = sheet;
        this.tagTree = tagTree;
    }

    @Override
    public JsonObject read() {

        HeaderTag headerTag = (HeaderTag) tagTree.getTag();

        Integer headerRowIndex = headerTag.getRowIndex().toInteger();
        Integer headerColIndex = headerTag.getColIndex().toInteger();

        Row r = sheet.getRow(headerRowIndex);
        if (r == null) {
            return new JsonObject();
        }

        Integer i = headerColIndex;
        List<Header> headerList = new ArrayList<>();
        while (r.getLastCellNum() > i) {
            Cell cell = r.getCell(i);
            String cellValue = cell.toString();
            Header header = new Header(new Index(i), new HeaderValue(cellValue));
            headerList.add(header);
            i++;
        }

        Accumulator.setData(headerTag.name().toString(), new Headers(headerList));

        return new JsonObject();
    }
}
