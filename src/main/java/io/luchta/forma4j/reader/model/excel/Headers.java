package io.luchta.forma4j.reader.model.excel;

import io.luchta.forma4j.reader.specification.excel.RetrieveHeaderValueSpec;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class Headers implements Iterable<Header> {
    List<Header> list;

    public Headers() {
        list = new ArrayList<>();
    }

    public Headers(List<Header> list) {
        this.list = list;
    }

    public Header retrieveHeaderBy(Index colIndex) {
        RetrieveHeaderValueSpec retrieveHeaderValueSpec = new RetrieveHeaderValueSpec(colIndex);
        List<Header> list = this.list.stream().filter(retrieveHeaderValueSpec::isSatisfiedBy).collect(Collectors.toList());
        if (list.isEmpty()) return new Header();
        return list.get(0);
    }

    public Integer size() {
        return list.size();
    }

    public Header get(Integer index) {
        return list.get(index);
    }

    @Override
    public Iterator<Header> iterator() {
        return list.iterator();
    }
}
