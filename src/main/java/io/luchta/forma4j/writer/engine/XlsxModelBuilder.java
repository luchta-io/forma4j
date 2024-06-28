package io.luchta.forma4j.writer.engine;

import io.luchta.forma4j.writer.Context;
import io.luchta.forma4j.writer.definition.XmlDocument;
import io.luchta.forma4j.writer.definition.schema.Element;
import io.luchta.forma4j.writer.definition.schema.element.Sheet;
import io.luchta.forma4j.writer.engine.buffer.BuildBuffer;
import io.luchta.forma4j.writer.engine.handler.element.SheetHandler;
import io.luchta.forma4j.writer.engine.model.book.XlsxBook;

public class XlsxModelBuilder {
    XmlDocument definition;
    Context context;

    public XlsxModelBuilder(XmlDocument definition, Context context) {
        this.definition = definition;
        this.context = context;
    }

    public XlsxBook build() {
        BuildBuffer buffer = new BuildBuffer(context);
        rootHandler(buffer);
        return buffer.accumulator().toXlsxBook();
    }

    private void rootHandler(BuildBuffer buffer) {
        for (Element sheet : definition.root().children()) {
            new SheetHandler(buffer).handle((Sheet) sheet);
        }
    }
}
