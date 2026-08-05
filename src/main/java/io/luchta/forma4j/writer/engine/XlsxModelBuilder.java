package io.luchta.forma4j.writer.engine;

import io.luchta.forma4j.writer.Context;
import io.luchta.forma4j.writer.definition.XmlDocument;
import io.luchta.forma4j.writer.definition.schema.Element;
import io.luchta.forma4j.writer.definition.schema.element.Sheet;
import io.luchta.forma4j.writer.engine.buffer.BuildBuffer;
import io.luchta.forma4j.writer.engine.buffer.accumulater.BuildAccumulator;
import io.luchta.forma4j.writer.engine.handler.element.SheetHandler;
import io.luchta.forma4j.writer.engine.model.book.XlsxBook;
import io.luchta.forma4j.writer.engine.resolver.VariableResolver;
import io.luchta.forma4j.writer.engine.strategy.AccumulatingOutputStrategy;
import io.luchta.forma4j.writer.engine.strategy.XlsxOutputStrategy;

public class XlsxModelBuilder {
    XmlDocument definition;
    Context context;

    public XlsxModelBuilder(XmlDocument definition, Context context) {
        this.definition = definition;
        this.context = context;
    }

    public BuildAccumulator accumulate() {
        AccumulatingOutputStrategy strategy = new AccumulatingOutputStrategy();
        write(strategy);
        return strategy.accumulator();
    }

    public XlsxBook build() {
        return accumulate().toXlsxBook();
    }

    /** 定義を指定した出力戦略へ展開します。 */
    public void write(XlsxOutputStrategy outputStrategy) {
        BuildBuffer buffer = new BuildBuffer(context, outputStrategy);
        rootHandler(buffer);
    }

    /** Sourceを開く処理を指定して定義を出力戦略へ展開します。 */
    public void write(
            XlsxOutputStrategy outputStrategy,
            VariableResolver.SequenceOpener sequenceOpener
    ) {
        BuildBuffer buffer = new BuildBuffer(context, outputStrategy, sequenceOpener);
        rootHandler(buffer);
    }

    private void rootHandler(BuildBuffer buffer) {
        for (Element sheet : definition.root().children()) {
            new SheetHandler(buffer).handle((Sheet) sheet);
        }
    }
}
