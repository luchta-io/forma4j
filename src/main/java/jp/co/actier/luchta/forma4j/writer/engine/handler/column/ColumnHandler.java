package jp.co.actier.luchta.forma4j.writer.engine.handler.column;

import jp.co.actier.luchta.forma4j.writer.definition.schema.element.Column;
import jp.co.actier.luchta.forma4j.writer.engine.buffer.BuildBuffer;

public class ColumnHandler {
    BuildBuffer buffer;

    public ColumnHandler(BuildBuffer buffer) {
        this.buffer = buffer;
    }

    public void handle(Column column) {

    }
}
