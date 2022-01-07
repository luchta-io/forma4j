package io.luchta.forma4j.reader.compile;

import io.luchta.forma4j.context.syntax.SyntaxError;
import io.luchta.forma4j.context.syntax.SyntaxErrors;
import io.luchta.forma4j.reader.compile.parse.Parser;
import io.luchta.forma4j.reader.compile.relation.RelationAnalysis;
import io.luchta.forma4j.reader.model.tag.TagTree;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;

public class FormaReaderCompiler {

    public TagTree compile(InputStream xml, SyntaxErrors syntaxErrors) throws ParserConfigurationException, IOException, SAXException {

        Parser parser = new Parser();
        TagTree tree = new TagTree();
        try {
            tree = parser.parse(xml, syntaxErrors);
        } catch (ParserConfigurationException | IOException | SAXException e) {
            SyntaxError syntaxError = new SyntaxError(e.getMessage(), e);
            syntaxErrors.add(syntaxError);
            return tree;
        }

        RelationAnalysis relationAnalysis = new RelationAnalysis();
        relationAnalysis.analyze(tree, syntaxErrors);

        return tree;
    }
}
