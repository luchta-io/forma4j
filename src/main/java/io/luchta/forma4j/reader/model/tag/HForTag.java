package io.luchta.forma4j.reader.model.tag;

import io.luchta.forma4j.reader.model.tag.property.Index;
import io.luchta.forma4j.reader.model.tag.property.Name;

public class HForTag implements Tag {

    Index from;
    Boolean fromIsUndefined;
    Index to;
    Boolean toIsUndefined;
    Name name;

    public HForTag(Index from, Boolean fromIsUndefined, Index to, Boolean toIsUndefined, Name name) {
        this.from = from;
        this.fromIsUndefined = fromIsUndefined;
        this.to = to;
        this.toIsUndefined = toIsUndefined;
        this.name = name;
    }

    public boolean fromIsUndefined() {
        return fromIsUndefined.booleanValue();
    }

    public boolean toIsUndefined() {
        return toIsUndefined.booleanValue();
    }

    public Index from() {
        return from;
    }

    public Index to() {
        return to;
    }

    public Name name() {
        return name;
    }

    @Override
    public boolean isHFor() {
        return true;
    }
}
