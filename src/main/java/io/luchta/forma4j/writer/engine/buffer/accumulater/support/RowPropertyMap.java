package io.luchta.forma4j.writer.engine.buffer.accumulater.support;

import io.luchta.forma4j.writer.engine.model.row.address.XlsxRowAddress;
import io.luchta.forma4j.writer.engine.model.row.property.XlsxRowProperties;

import java.util.HashMap;
import java.util.Map;

public class RowPropertyMap {
    Map<XlsxRowAddress, XlsxRowProperties> map = new HashMap<>();

    public RowPropertyMap() {
    }

    public RowPropertyMap(Map<XlsxRowAddress, XlsxRowProperties> map) {
        this.map = map;
    }

    public void put(XlsxRowAddress address, XlsxRowProperties property) {
        map.put(address, property);
    }

    public XlsxRowProperties get(XlsxRowAddress address) {
        return map.get(address);
    }

    public boolean containsKey(XlsxRowAddress address) {
        return map.containsKey(address);
    }
}
