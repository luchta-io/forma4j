package io.luchta.forma4j.reader.compile.relation.taganalyzer;

import io.luchta.forma4j.context.syntax.SyntaxError;
import io.luchta.forma4j.context.syntax.SyntaxErrors;
import io.luchta.forma4j.reader.model.tag.Tag;
import io.luchta.forma4j.reader.model.tag.TagTree;
import io.luchta.forma4j.reader.model.tag.TagTrees;

public class SheetTagAnalyzer implements TagAnalyzer {

    @Override
    public void analyze(TagTree tagTree, SyntaxErrors syntaxErrors) {

        Tag immediateDominator = tagTree.getImmediateDominator();
        if (!immediateDominator.isFormaReader()) {
            SyntaxError syntaxError = new SyntaxError("sheet タグの親要素は forma-reader としてください");
            syntaxErrors.add(syntaxError);
        }

        TagTrees children = tagTree.getChildren();
        if (children.size() == 0) {
            SyntaxError syntaxError = new SyntaxError("sheet タグには子要素が必要です");
            syntaxErrors.add(syntaxError);
        }
    }
}
