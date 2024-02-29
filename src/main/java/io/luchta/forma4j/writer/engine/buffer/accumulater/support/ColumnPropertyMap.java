package io.luchta.forma4j.writer.engine.buffer.accumulater.support;

import io.luchta.forma4j.writer.engine.model.cell.address.XlsxSheetName;
import io.luchta.forma4j.writer.engine.model.column.XlsxColumnAddress;
import io.luchta.forma4j.writer.engine.model.column.property.XlsxColumnProperties;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ColumnPropertyMap {
    Map<XlsxColumnAddress, XlsxColumnProperties> map = new HashMap<>();

    public ColumnPropertyMap() {
    }

    public ColumnPropertyMap(Map<XlsxColumnAddress, XlsxColumnProperties> map) {
        this.map = map;
    }

    public void put(XlsxColumnAddress address, XlsxColumnProperties property) {
        map.put(address, property);
    }

    public XlsxColumnProperties get(XlsxColumnAddress address) {
        return map.get(address);
    }

    public ColumnPropertyMap getBySheetName(XlsxSheetName sheetName) {
        ColumnPropertyMap propertyMap = new ColumnPropertyMap();
        for (Map.Entry<XlsxColumnAddress, XlsxColumnProperties> entry : map.entrySet()) {
            if (entry.getKey().equalSheetName(sheetName)) {
                XlsxColumnProperties properties = new XlsxColumnProperties();
                if (propertyMap.containsKey(entry.getKey())) {
                    properties = propertyMap.get(entry.getKey());
                }
                properties.addAll(entry.getValue());
                propertyMap.put(entry.getKey(), properties);
            }
        }
        return propertyMap;
    }

    public boolean containsKey(XlsxColumnAddress address) {
        return map.containsKey(address);
    }

    public int size() {
        return map.size();
    }

    public Set<Map.Entry<XlsxColumnAddress, XlsxColumnProperties>> entrySet() {
        return map.entrySet();
    }
}
