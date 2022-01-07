package io.luchta.forma4j.reader.model.tag;

public class TagTree {

    Tags dominators;
    Tag tag;
    TagTrees children;

    public TagTree() {
        dominators = new Tags();
        tag = new Tag() {};
        children = new TagTrees();
    }

    public TagTree(Tags dominators, Tag tag, TagTrees children) {
        this.dominators = dominators;
        this.tag = tag;
        this.children = children;
    }

    public Tag getImmediateDominator() {
        Integer size = dominators.size();
        if (size > 0) {
            return dominators.list.get(size - 1);
        }
        return new Tag() {};
    }

    public Tags getDominators() {
        return dominators;
    }

    public Tag getTag() {
        return tag;
    }

    public TagTrees getChildren() {
        return children;
    }
}
