package io.luchta.forma4j.reader.model.tag;

import io.luchta.forma4j.reader.model.tag.property.Name;

public class SheetTag implements Tag {

    Name name;

    public SheetTag(Name name) {
        this.name = name;
    }

    public Name name() {
        return name;
    }

    @Override
    public boolean isSheet() {
        return true;
    }
}
