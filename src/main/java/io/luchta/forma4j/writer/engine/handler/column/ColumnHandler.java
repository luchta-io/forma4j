package io.luchta.forma4j.writer.engine.handler.column;

import io.luchta.forma4j.writer.definition.schema.element.Column;
import io.luchta.forma4j.writer.engine.buffer.BuildBuffer;

public class ColumnHandler {
    BuildBuffer buffer;

    public ColumnHandler(BuildBuffer buffer) {
        this.buffer = buffer;
    }

    public void handle(Column column) {

    }
}
