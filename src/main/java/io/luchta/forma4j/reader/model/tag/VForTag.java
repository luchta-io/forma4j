package io.luchta.forma4j.reader.model.tag;

import io.luchta.forma4j.reader.model.tag.property.Header;
import io.luchta.forma4j.reader.model.tag.property.Index;
import io.luchta.forma4j.reader.model.tag.property.Name;

public class VForTag implements Tag {

    Index from;
    Boolean fromIsUndefined;
    Index to;
    Boolean toIsUndefined;
    Name name;
    Header header;

    public VForTag(Index from, Boolean fromIsUndefined, Index to, Boolean toIsUndefined, Name name, Header header) {
        this.from = from;
        this.fromIsUndefined = fromIsUndefined;
        this.to = to;
        this.toIsUndefined = toIsUndefined;
        this.name = name;
        this.header = header;
    }

    public Index from() {
        return from;
    }

    public boolean fromIsUndefined() {
        return fromIsUndefined.booleanValue();
    }

    public Index to() {
        return to;
    }

    public boolean toIsUndefined() {
        return toIsUndefined.booleanValue();
    }

    public Name name() {
        return name;
    }

    public Header header() {
        return header;
    }

    @Override
    public boolean isVFor() {
        return true;
    }
}
