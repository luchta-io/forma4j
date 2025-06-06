package io.luchta.forma4j.reader.xml.relation.taganalyzer;

import io.luchta.forma4j.context.syntax.SyntaxError;
import io.luchta.forma4j.context.syntax.SyntaxErrors;
import io.luchta.forma4j.reader.model.tag.Tag;
import io.luchta.forma4j.reader.model.tag.TagTree;
import io.luchta.forma4j.reader.model.tag.TagTrees;

public class ListTagAnalyzer implements TagAnalyzer {
    @Override
    public void analyze(TagTree tagTree, SyntaxErrors syntaxErrors) {
        Tag immediateDominator = tagTree.getImmediateDominator();
        if (!immediateDominator.isSheet()) {
            SyntaxError syntaxError = new SyntaxError("list タグの親要素は sheet タグとしてください");
            syntaxErrors.add(syntaxError);
        }

        TagTrees children = tagTree.getChildren();
        if (children.size() != 0) {
            SyntaxError syntaxError = new SyntaxError("list タグは子要素を持つことができません");
            syntaxErrors.add(syntaxError);
        }
    }
}
