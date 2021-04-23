package jp.co.actier.luchta.forma4j.reader.config.element;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Elements implements Iterable<Element>, Serializable {

    private static final long serialVersionUID = 1L;
    
    private List<Element> list = new ArrayList<>();
    
    public Elements() {}
    
    public Elements(List<Element> list) {
        this.list = list;
    }
    
    public int size() {
        return this.list.size();
    }
    
    public boolean isEmpty() {
        return this.size() == 0;
    }
    
    public void add(Element element) {
        this.list.add(element);
    }
    
    public Element get(int index) {
        return this.list.get(index);
    }

    @Override
    public Iterator<Element> iterator() {
        return this.list.iterator();
    }
}
