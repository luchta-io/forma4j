package jp.co.actier.luchta.forma4j.writer.engine.buffer.stack;

import jp.co.actier.luchta.forma4j.writer.engine.model.cell.address.XlsxCellAddress;

import java.util.Stack;

public class AddressStack {
    Stack<XlsxCellAddress> stack = new Stack<>();

    public void push(XlsxCellAddress address) {
        stack.push(address);
    }

    public XlsxCellAddress peek() {
        return stack.peek();
    }
}
