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
        XlsxCellAddress baseAddress = buffer.addressStack().peek();
        XlsxRowNumber startRowNumber = new XlsxRowNumber(verticalFor.startRowIndex());
        XlsxColumnNumber startColumnNumber = new XlsxColumnNumber(verticalFor.startColumnIndex());
        try (VariableResolver.Iteration collection =
                     variableResolver.openIteration(verticalFor.collection().toString())) {
            int i = 0;
            while (collection.hasNext()) {
                Object item = collection.next();
                XlsxCellAddress currentAddress = baseAddress.with(
                        new XlsxRowNumber((long) startRowNumber.toInt() + i),
                        startColumnNumber
                );
                buffer.addressStack().push(currentAddress);
                buffer.loopContext().put(verticalFor.index(), i);
                buffer.loopContext().put(verticalFor.item(), item);
                try {
                    dispatch(verticalFor.children());
                } finally {
                    buffer.addressStack().pop();
                }
                i++;
            }
        } finally {
            buffer.loopContext().remove(verticalFor.index());
            buffer.loopContext().remove(verticalFor.item());
        }
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
