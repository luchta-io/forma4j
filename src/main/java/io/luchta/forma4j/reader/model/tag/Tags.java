package io.luchta.forma4j.reader.model.tag;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Tags implements Iterable<Tag> {

    List<Tag> list;

    public Tags() {
        list = new ArrayList<>();
    }

    public Tags(List<Tag> list) {
        this.list = list;
    }

    public Tags clone() {
        List<Tag> clone = new ArrayList<>();
        for (Tag tag : list) {
            clone.add(tag);
        }
        return new Tags(clone);
    }

    public Tags add(Tag tag) {
        list.add(tag);
        return new Tags(list);
    }

    public Integer size() {
        return list.size();
    }

    @Override
    public Iterator<Tag> iterator() {
        return list.iterator();
    }
}
