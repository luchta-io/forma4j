package io.luchta.forma4j.writer.engine.buffer.loop;

import io.luchta.forma4j.writer.definition.schema.attribute.loop.Index;
import io.luchta.forma4j.writer.definition.schema.attribute.loop.Item;

import java.util.HashMap;
import java.util.Map;

public class LoopContext {
    Map<Item, Object> itemMap = new HashMap<>();
    Map<Index, Integer> indexMap = new HashMap<>();

    public void put(Item itemName, Object item) {
        itemMap.put(itemName, item);
    }

    public void put(Index indexName, int index) {
        indexMap.put(indexName, index);
    }

    public Object getItem(String itemName) {
        return itemMap.get(new Item(itemName));
    }

    public Object getIndex(String indexName) {
        return indexMap.get(new Index(indexName));
    }

    public void remove(Index index) {
        indexMap.remove(index);
    }

    public void remove(Item item) {
        itemMap.remove(item);
    }
}
