package io.luchta.forma4j.reader.config.element;

import io.luchta.forma4j.reader.config.element.property.ElementName;

public abstract class Element {

    protected Elements children = new Elements();
    
    public void addChildren(Elements children) {
        children.forEach(element -> this.children.add(element));
    }
    
    public Elements getChildren() {
        return this.children;
    }
    
    public abstract ElementName getName();
}
