package io.luchta.forma4j.reader.compile.relation.taganalyzer;

import io.luchta.forma4j.context.syntax.SyntaxErrors;
import io.luchta.forma4j.reader.model.tag.TagTree;

public interface TagAnalyzer {
    void analyze(TagTree tagTree, SyntaxErrors syntaxErrors);
}
