package io.luchta.forma4j.context.syntax;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SyntaxErrors implements Iterable<SyntaxError> {

    List<SyntaxError> list;

    public SyntaxErrors() {
        list = new ArrayList<>();
    }

    public SyntaxErrors(List<SyntaxError> list ) {
        this.list = list;
    }

    public void add(SyntaxError value) {
        list.add(value);
    }

    public Integer size() {
        return list.size();
    }

    public boolean hasErrors() {
        return size() != 0;
    }

    @Override
    public Iterator<SyntaxError> iterator() {
        return list.iterator();
    }
}
