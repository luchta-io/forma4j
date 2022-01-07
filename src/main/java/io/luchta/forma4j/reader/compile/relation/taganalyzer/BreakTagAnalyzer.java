package io.luchta.forma4j.reader.compile.relation.taganalyzer;

import io.luchta.forma4j.context.syntax.SyntaxError;
import io.luchta.forma4j.context.syntax.SyntaxErrors;
import io.luchta.forma4j.reader.model.tag.Tag;
import io.luchta.forma4j.reader.model.tag.TagTree;
import io.luchta.forma4j.reader.model.tag.TagTrees;

public class BreakTagAnalyzer implements TagAnalyzer {

    @Override
    public void analyze(TagTree tagTree, SyntaxErrors syntaxErrors) {

        Tag immediateDominator = tagTree.getImmediateDominator();
        if (!immediateDominator.isVFor() && !immediateDominator.isHFor()) {
            SyntaxError syntaxError = new SyntaxError("break タグは v-for タグまたは h-for タグの子要素となるようにしてください");
            syntaxErrors.add(syntaxError);
        }

        TagTrees children = tagTree.getChildren();
        if (children.size() != 0) {
            SyntaxError syntaxError = new SyntaxError("break タグは子要素を持つことができません");
            syntaxErrors.add(syntaxError);
        }
    }
}
