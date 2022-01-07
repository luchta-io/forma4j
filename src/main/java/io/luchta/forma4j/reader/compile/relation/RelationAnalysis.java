package io.luchta.forma4j.reader.compile.relation;

import io.luchta.forma4j.context.syntax.SyntaxError;
import io.luchta.forma4j.context.syntax.SyntaxErrors;
import io.luchta.forma4j.reader.compile.relation.taganalyzer.TagAnalyzer;
import io.luchta.forma4j.reader.compile.relation.taganalyzer.TagAnalyzerFactory;
import io.luchta.forma4j.reader.model.tag.Tag;
import io.luchta.forma4j.reader.model.tag.TagTree;
import io.luchta.forma4j.reader.model.tag.TagTrees;

public class RelationAnalysis {

    public void analyze(TagTree tree, SyntaxErrors syntaxErrors) {

        Tag tag = tree.getTag();

        if (!tag.isFormaReader()) {
            SyntaxError syntaxError = new SyntaxError("ルート要素が forma-reader ではありません");
            syntaxErrors.add(syntaxError);
        }

        TagTrees children = tree.getChildren();
        for (TagTree child : children) {
            if (!child.getTag().isSheet()) {
                SyntaxError syntaxError = new SyntaxError("forma-reader は子要素に指定できるのは sheet タグのみです");
                syntaxErrors.add(syntaxError);
                break;
            }
        }
        analyzeChildren(children, syntaxErrors);
    }

    private void analyzeChildren(TagTrees trees, SyntaxErrors syntaxErrors) {

        TagAnalyzerFactory factory = new TagAnalyzerFactory();
        for (TagTree t : trees) {
            TagAnalyzer analyzer = factory.create(t);
            analyzer.analyze(t, syntaxErrors);
        }
    }
}
