package io.luchta.forma4j.writer.engine.buffer;

import io.luchta.forma4j.writer.Context;
import io.luchta.forma4j.writer.engine.buffer.accumulater.BuildAccumulator;
import io.luchta.forma4j.writer.engine.buffer.loop.LoopContext;
import io.luchta.forma4j.writer.engine.buffer.stack.AddressStack;
import io.luchta.forma4j.writer.engine.resolver.StyleResolver;
import io.luchta.forma4j.writer.engine.resolver.VariableResolver;
import io.luchta.forma4j.writer.engine.strategy.AccumulatingOutputStrategy;
import io.luchta.forma4j.writer.engine.strategy.XlsxOutputStrategy;

public class BuildBuffer {
    XlsxOutputStrategy outputStrategy;
    AddressStack addressStack = new AddressStack();
    LoopContext loopContext = new LoopContext();
    VariableResolver variableResolver;
    StyleResolver styleResolver = new StyleResolver();

    public BuildBuffer(Context context) {
        this(context, new AccumulatingOutputStrategy());
    }

    public BuildBuffer(Context context, XlsxOutputStrategy outputStrategy) {
        this(context, outputStrategy, null);
    }

    public BuildBuffer(
            Context context,
            XlsxOutputStrategy outputStrategy,
            VariableResolver.SequenceOpener sequenceOpener
    ) {
        this.outputStrategy = outputStrategy;
        this.variableResolver = sequenceOpener == null
                ? new VariableResolver(context, loopContext)
                : new VariableResolver(context, loopContext, sequenceOpener);
    }

    /** @deprecated ハンドラからは {@link #outputStrategy()} を使用してください。 */
    @Deprecated
    public BuildAccumulator accumulator() {
        if (outputStrategy instanceof AccumulatingOutputStrategy) {
            return ((AccumulatingOutputStrategy) outputStrategy).accumulator();
        }
        throw new IllegalStateException("蓄積方式ではない出力戦略に accumulator はありません。");
    }

    public XlsxOutputStrategy outputStrategy() {
        return outputStrategy;
    }

    public AddressStack addressStack() {
        return addressStack;
    }

    public LoopContext loopContext() {
        return loopContext;
    }

    public VariableResolver variableResolver() {
        return variableResolver;
    }

    public StyleResolver styleResolver() {
        return styleResolver;
    }
}
