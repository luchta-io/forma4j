package io.luchta.forma4j.reader.excel.objectreader;

import io.luchta.forma4j.reader.model.tag.Tag;

public class ObjectReaderFactory {

    public ObjectReader create(ObjectReaderFactoryParameter param) {

        Tag tag = param.getTag();

        if (tag.isCell()) {
            return new CellReader(param.getSheet(), param.getRowIndex(), param.getColIndex(), param.getTagTree());
        }

        if (tag.isHFor()) {
            return new HForReader(param.getSheet(), param.getRowIndex(), param.getTagTree());
        }

        if (tag.isVFor()) {
            return new VForReader(
                    param.getSheet(), param.getRowIndex(), param.getColIndex(), param.getTagTree()
            );
        }

        if (tag.isBreak()) {

        }

        if (tag.isHeader()) {
            return new HeaderReader(param.getSheet(), param.getTagTree());
        }

        throw new UnsupportedOperationException("存在しないタグが指定されています");
    }
}
