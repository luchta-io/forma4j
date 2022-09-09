package io.luchta.forma4j.writer.engine.handler.list;

import io.luchta.forma4j.writer.definition.schema.attribute.Style;
import io.luchta.forma4j.writer.definition.schema.attribute.index.ColumnIndex;
import io.luchta.forma4j.writer.definition.schema.attribute.index.RowIndex;
import io.luchta.forma4j.writer.definition.schema.element.ListElement;
import io.luchta.forma4j.writer.engine.buffer.BuildBuffer;
import io.luchta.forma4j.writer.engine.model.cell.XlsxCell;
import io.luchta.forma4j.writer.engine.model.cell.address.XlsxCellAddress;
import io.luchta.forma4j.writer.engine.model.cell.address.XlsxColumnNumber;
import io.luchta.forma4j.writer.engine.model.cell.address.XlsxRowNumber;
import io.luchta.forma4j.writer.engine.model.cell.style.XlsxCellStylesBuilder;
import io.luchta.forma4j.writer.engine.model.cell.value.Text;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class ListHandler {
    BuildBuffer buffer;

    public ListHandler(BuildBuffer buffer) {
        this.buffer = buffer;
    }

    public void handle(ListElement listElement) {
        Long rowIndex = startRowIndex(listElement);
        Long columnIndex = startColumnIndex(listElement);
        Style headerStyle = listElement.headerStyle();
        Style detailStyle = listElement.detailStyle();

        header(address(new XlsxRowNumber(rowIndex), new XlsxColumnNumber(columnIndex)), headerStyle);
        detail(address(new XlsxRowNumber(rowIndex + 1), new XlsxColumnNumber(columnIndex)), detailStyle);
    }

    private void header(XlsxCellAddress address, Style style) {
        Set<String> keySet = buffer.variableResolver().getKeySet();
        String key = keySet.iterator().next();
        List<Object> collection = buffer.variableResolver().getList(key);
        if (collection.size() == 0 || !(collection.get(0) instanceof Map<?, ?>)) {
            return;
        }

        XlsxCellStylesBuilder stylesBuilder = new XlsxCellStylesBuilder();
        Map<String, Object> map = (Map<String, Object>) collection.get(0);
        for (String headerName : map.keySet()) {
            buffer.accumulator().put(
                    address,
                    new XlsxCell(address, new Text(headerName), stylesBuilder.build(style)));
            address = address.columnNumberIncrement();
        }
    }

    private void detail(XlsxCellAddress address, Style style) {
        Set<String> keySet = buffer.variableResolver().getKeySet();
        String key = keySet.iterator().next();
        List<Object> collection = buffer.variableResolver().getList(key);
        if (collection.size() == 0 || !(collection.get(0) instanceof Map<?, ?>)) {
            return;
        }

        XlsxCellStylesBuilder stylesBuilder = new XlsxCellStylesBuilder();
        Long columnIndex = address.columnNumber().toLong();
        for (Object obj : collection) {
            if (!(obj instanceof Map<?, ?>)) {
                continue;
            }

            Map<String, Object> line = (Map<String, Object>) obj;
            for (Object value : line.values()) {
                buffer.accumulator().put(
                        address,
                        new XlsxCell(address, new Text(value.toString()), stylesBuilder.build(style)));
                address = address.columnNumberIncrement();
            }

            address = address.rowNumberIncrement().with(new XlsxColumnNumber(columnIndex));
        }
    }

    private Long startRowIndex(ListElement listElement) {
        RowIndex startRowIndex = listElement.startRowIndex();
        Long rowIndex = 0L;
        if (!startRowIndex.isEmpty()) {
            rowIndex = startRowIndex.value();
        }
        return rowIndex;
    }

    private Long startColumnIndex(ListElement listElement) {
        ColumnIndex startColumnIndex = listElement.startColumnIndex();
        Long columnIndex = 0L;
        if (!startColumnIndex.isEmpty()) {
            columnIndex = startColumnIndex.value();
        }
        return columnIndex;
    }

    private XlsxCellAddress address(XlsxRowNumber rowNumber, XlsxColumnNumber columnNumber) {
        XlsxCellAddress address = buffer.addressStack().peek();
        return address.with(rowNumber).with(columnNumber);
    }
}
