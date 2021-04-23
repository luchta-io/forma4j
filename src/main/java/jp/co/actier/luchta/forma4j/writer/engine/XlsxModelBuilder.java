package jp.co.actier.luchta.forma4j.writer.engine;

import jp.co.actier.luchta.forma4j.writer.Context;
import jp.co.actier.luchta.forma4j.writer.definition.XmlDocument;
import jp.co.actier.luchta.forma4j.writer.definition.schema.Element;
import jp.co.actier.luchta.forma4j.writer.definition.schema.element.Sheet;
import jp.co.actier.luchta.forma4j.writer.engine.buffer.BuildBuffer;
import jp.co.actier.luchta.forma4j.writer.engine.handler.sheet.SheetHandler;
import jp.co.actier.luchta.forma4j.writer.engine.model.book.XlsxBook;

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
