package io.luchta.forma4j.reader.model.tag;

import io.luchta.forma4j.reader.model.tag.property.IF;

public class BreakTag implements Tag {

    IF condition;

    public BreakTag(IF condition) {
        this.condition = condition;
    }

    @Override
    public boolean isBreak() {
        return true;
    }
}
