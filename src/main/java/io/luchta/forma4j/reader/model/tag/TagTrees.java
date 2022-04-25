package io.luchta.forma4j.reader.model.tag;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TagTrees implements Iterable<TagTree> {

    List<TagTree> list;

    public TagTrees() {
        list = new ArrayList<>();
    }

    public TagTrees(List<TagTree> list) {
        this.list = list;
    }

    public TagTree get(Integer index) {
        return list.get(index);
    }

    public Integer size() {
        return list.size();
    }

    @Override
    public Iterator<TagTree> iterator() {
        return list.iterator();
    }
}
