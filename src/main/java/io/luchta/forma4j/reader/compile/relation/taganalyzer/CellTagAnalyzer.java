package io.luchta.forma4j.reader.compile.relation.taganalyzer;

import io.luchta.forma4j.context.syntax.SyntaxError;
import io.luchta.forma4j.context.syntax.SyntaxErrors;
import io.luchta.forma4j.reader.model.tag.Tag;
import io.luchta.forma4j.reader.model.tag.TagTree;
import io.luchta.forma4j.reader.model.tag.TagTrees;
import io.luchta.forma4j.reader.model.tag.Tags;

public class CellTagAnalyzer implements TagAnalyzer {

    @Override
    public void analyze(TagTree tagTree, SyntaxErrors syntaxErrors) {

        Tags dominators = tagTree.getDominators();
        boolean hasSheet = false;
        for (Tag tag : dominators) {
            if (tag.isSheet()) {
                hasSheet = true;
                break;
            }
        }

        if (!hasSheet) {
            SyntaxError syntaxError = new SyntaxError("cell タグは sheet タグの子孫要素となるようにしてください");
            syntaxErrors.add(syntaxError);
        }

        TagTrees children = tagTree.getChildren();
        if (children.size() != 0) {
            SyntaxError syntaxError = new SyntaxError("cell タグは子要素を持つことができません");
            syntaxErrors.add(syntaxError);
        }
    }
}
