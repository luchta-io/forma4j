package io.luchta.forma4j.reader.xml.relation.taganalyzer;

import io.luchta.forma4j.reader.model.tag.Tag;
import io.luchta.forma4j.reader.model.tag.TagTree;

public class TagAnalyzerFactory {

    public TagAnalyzer create(TagTree tree) {

        Tag tag = tree.getTag();

        if (tag.isSheet()) {
            return new SheetTagAnalyzer();
        }

        if (tag.isCell()) {
            return new CellTagAnalyzer();
        }

        if (tag.isVFor()) {
            return new VForTagAnalyzer();
        }

        if (tag.isHFor()) {
            return new HForTagAnalyzer();
        }

        if (tag.isList()) {
            return new ListTagAnalyzer();
        }

        throw new UnsupportedOperationException("存在しないタグが指定されています");
    }
}
