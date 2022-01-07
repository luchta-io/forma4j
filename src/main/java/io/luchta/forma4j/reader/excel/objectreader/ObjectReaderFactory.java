package io.luchta.forma4j.reader.excel.objectreader;

import io.luchta.forma4j.reader.model.tag.Tag;

public class ObjectReaderFactory {

    public ObjectReader create(Tag tag) {

        if (tag.isCell()) {
            return new CellReader();
        }

        if (tag.isHFor()) {
            return new HForReader();
        }

        if (tag.isVFor()) {
            return new VForReader();
        }

        if (tag.isBreak()) {

        }

        throw new UnsupportedOperationException("存在しないタグが指定されています");
    }
}
