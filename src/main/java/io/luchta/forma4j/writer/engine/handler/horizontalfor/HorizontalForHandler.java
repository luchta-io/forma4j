package io.luchta.forma4j.writer.engine.handler.horizontalfor;

import io.luchta.forma4j.writer.definition.schema.Element;
import io.luchta.forma4j.writer.definition.schema.ElementList;
import io.luchta.forma4j.writer.definition.schema.element.Cell;
import io.luchta.forma4j.writer.definition.schema.element.HorizontalFor;
import io.luchta.forma4j.writer.engine.buffer.BuildBuffer;
import io.luchta.forma4j.writer.engine.resolver.VariableResolver;
import io.luchta.forma4j.writer.engine.handler.cell.CellHandler;

import java.util.List;

public class HorizontalForHandler {
    BuildBuffer buffer;

    public HorizontalForHandler(BuildBuffer buffer) {
        this.buffer = buffer;
    }

    public void handle(HorizontalFor horizontalFor) {
        VariableResolver variableResolver = buffer.variableResolver();
        List<Object> collection = variableResolver.getList(horizontalFor.collection().toString());
        for (int i = 0; i < collection.size(); i++) {
            buffer.loopContext().put(horizontalFor.index(), i);
            buffer.loopContext().put(horizontalFor.item(), collection.get(i));
            dispatch(horizontalFor.children());
        }
        buffer.loopContext().remove(horizontalFor.index());
        buffer.loopContext().remove(horizontalFor.item());
    }

    private void dispatch(ElementList children) {
        for (Element element : children) {
            switch (element.type()) {
                case CELL:
                    new CellHandler(buffer)
                            .handle((Cell) element);
                    break;
                case VERTICAL_FOR:
                case HORIZONTAL_FOR:
                case ROW:
                case COLUMN:
                case SHEET:
                case LIST:
                default:
                    // TODO
                    throw new IllegalStateException();
            }
        }
    }
}
