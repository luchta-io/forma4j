package io.luchta.forma4j.reader.model.tag;

import io.luchta.forma4j.reader.model.tag.property.Index;
import io.luchta.forma4j.reader.model.tag.property.Name;

public class HForTag implements Tag {

    Index from;
    Boolean fromIsUndefined;
    Index to;
    Boolean toIsUndefined;
    Index row;
    Boolean rowIsUndefined;
    Name name;

    public HForTag(Index from, Boolean fromIsUndefined, Index to, Boolean toIsUndefined, Index row, Boolean rowIsUndefined, Name name) {
        this.from = from;
        this.fromIsUndefined = fromIsUndefined;
        this.to = to;
        this.toIsUndefined = toIsUndefined;
        this.row = row;
        this.rowIsUndefined = rowIsUndefined;
        this.name = name;
    }

    public boolean fromIsUndefined() {
        return fromIsUndefined.booleanValue();
    }

    public boolean toIsUndefined() {
        return toIsUndefined.booleanValue();
    }

    public boolean rowIsUndefined() {
        return rowIsUndefined.booleanValue();
    }

    public Index from() {
        return from;
    }

    public Index to() {
        return to;
    }

    public Index row() {
        return row;
    }

    public Name name() {
        return name;
    }

    @Override
    public boolean isHFor() {
        return true;
    }
}
