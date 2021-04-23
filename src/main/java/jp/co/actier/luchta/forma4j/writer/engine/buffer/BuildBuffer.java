package jp.co.actier.luchta.forma4j.writer.engine.buffer;

import jp.co.actier.luchta.forma4j.writer.Context;
import jp.co.actier.luchta.forma4j.writer.engine.buffer.accumulater.BuildAccumulator;
import jp.co.actier.luchta.forma4j.writer.engine.buffer.loop.LoopContext;
import jp.co.actier.luchta.forma4j.writer.engine.buffer.stack.AddressStack;
import jp.co.actier.luchta.forma4j.writer.engine.resolver.VariableResolver;

public class BuildBuffer {
    BuildAccumulator accumulator = new BuildAccumulator();
    AddressStack addressStack = new AddressStack();
    LoopContext loopContext = new LoopContext();
    VariableResolver variableResolver;

    public BuildBuffer(Context context) {
        this.variableResolver = new VariableResolver(context, loopContext);
    }

    public BuildAccumulator accumulator() {
        return accumulator;
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
}
