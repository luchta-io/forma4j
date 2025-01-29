package io.luchta.forma4j.reader.xml.relation;

import io.luchta.forma4j.context.syntax.SyntaxError;
import io.luchta.forma4j.context.syntax.SyntaxErrors;
import io.luchta.forma4j.reader.xml.relation.taganalyzer.TagAnalyzer;
import io.luchta.forma4j.reader.xml.relation.taganalyzer.TagAnalyzerFactory;
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
                SyntaxError syntaxError = new SyntaxError("forma-reader が子要素に指定できるのは sheet タグのみです");
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
            analyzeChildren(t.getChildren(), syntaxErrors);
            analyzer.analyze(t, syntaxErrors);
        }
    }
}
