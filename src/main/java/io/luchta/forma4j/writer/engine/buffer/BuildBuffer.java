package io.luchta.forma4j.writer.engine.buffer;

import io.luchta.forma4j.writer.Context;
import io.luchta.forma4j.writer.engine.buffer.accumulater.BuildAccumulator;
import io.luchta.forma4j.writer.engine.buffer.loop.LoopContext;
import io.luchta.forma4j.writer.engine.buffer.stack.AddressStack;
import io.luchta.forma4j.writer.engine.resolver.VariableResolver;

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
