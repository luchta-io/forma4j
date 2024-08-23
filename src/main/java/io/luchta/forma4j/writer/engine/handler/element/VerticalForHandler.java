package io.luchta.forma4j.writer.engine.handler.element;

import io.luchta.forma4j.writer.definition.schema.Element;
import io.luchta.forma4j.writer.definition.schema.ElementList;
import io.luchta.forma4j.writer.definition.schema.element.Row;
import io.luchta.forma4j.writer.definition.schema.element.VerticalFor;
import io.luchta.forma4j.writer.engine.buffer.BuildBuffer;
import io.luchta.forma4j.writer.engine.model.cell.address.XlsxCellAddress;
import io.luchta.forma4j.writer.engine.model.cell.address.XlsxColumnNumber;
import io.luchta.forma4j.writer.engine.model.cell.address.XlsxRowNumber;
import io.luchta.forma4j.writer.engine.resolver.VariableResolver;

import java.util.List;

/**
 * vertical-forタグのハンドラクラス
 */
public class VerticalForHandler {
    /** バッファ */
    BuildBuffer buffer;

    /**
     * コンストラクタ
     * @param buffer
     */
    public VerticalForHandler(BuildBuffer buffer) {
        this.buffer = buffer;
    }

    /**
     * ハンドル
     * @param verticalFor
     */
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

    /**
     * ディスパッチ
     * @param children
     */
    private void dispatch(ElementList children) {
        for (Element element : children) {
            switch (element.type()) {
                case ROW:
                    new RowHandler(buffer).handle((Row) element);
                    break;
                default:
                    throw new IllegalStateException("vertical-forの子タグにはrowタグのみが設定可能です");
            }
        }
    }
}
