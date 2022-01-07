package io.luchta.forma4j.reader.excel.objectreader;

import io.luchta.forma4j.context.databind.json.JsonObject;
import io.luchta.forma4j.reader.model.excel.Index;
import io.luchta.forma4j.reader.model.tag.TagTree;
import org.apache.poi.ss.usermodel.Sheet;

public interface ObjectReader {
    JsonObject read(Sheet sheet, Index rowIndex, Index colIndex, TagTree tagTree);
}
