package jp.co.actier.luchta.forma4j.writer.engine.handler.verticalfor;

import jp.co.actier.luchta.forma4j.writer.definition.schema.Element;
import jp.co.actier.luchta.forma4j.writer.definition.schema.ElementList;
import jp.co.actier.luchta.forma4j.writer.definition.schema.element.VerticalFor;
import jp.co.actier.luchta.forma4j.writer.definition.schema.element.Row;
import jp.co.actier.luchta.forma4j.writer.engine.buffer.BuildBuffer;
import jp.co.actier.luchta.forma4j.writer.engine.handler.row.RowHandler;
import jp.co.actier.luchta.forma4j.writer.engine.model.cell.address.XlsxCellAddress;
import jp.co.actier.luchta.forma4j.writer.engine.model.cell.address.XlsxColumnNumber;
import jp.co.actier.luchta.forma4j.writer.engine.model.cell.address.XlsxRowNumber;
import jp.co.actier.luchta.forma4j.writer.engine.resolver.VariableResolver;

import java.util.List;

public class VerticalForHandler {
    BuildBuffer buffer;

    public VerticalForHandler(BuildBuffer buffer) {
        this.buffer = buffer;
    }

    public void handle(VerticalFor verticalFor) {
        VariableResolver variableResolver = buffer.variableResolver();
        List<Object> collection = variableResolver.getList(verticalFor.collection().toString());
        for (int i = 0; i < collection.size(); i++) {
            XlsxCellAddress address = buffer.addressStack().peek();
            if (i == 0) {
                buffer.addressStack().push(address.with(
                        new XlsxRowNumber(verticalFor.startRowIndex()),
                        new XlsxColumnNumber(verticalFor.startColumnIndex())
                ));
            } else {
                buffer.addressStack().push(
                        address.with(
                                address.rowNumber().increment(),
                                new XlsxColumnNumber(verticalFor.startColumnIndex())
                        ));
            }
            buffer.loopContext().put(verticalFor.index(), i);
            buffer.loopContext().put(verticalFor.item(), collection.get(i));
            dispatch(verticalFor.children());
        }
        buffer.loopContext().remove(verticalFor.index());
        buffer.loopContext().remove(verticalFor.item());
    }

    private void dispatch(ElementList children) {
        for (Element element : children) {
            switch (element.type()) {
                case ROW:
                    new RowHandler(buffer)
                            .handle((Row) element);
                    break;
                case CELL:
                case VERTICAL_FOR:
                case HORIZONTAL_FOR:
                case COLUMN:
                case SHEET:
                default:
                    // TODO
                    throw new IllegalStateException();
            }
        }
    }
}
