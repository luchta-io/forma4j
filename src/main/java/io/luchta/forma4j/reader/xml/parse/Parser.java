package io.luchta.forma4j.reader.xml.parse;

import io.luchta.forma4j.context.syntax.SyntaxErrors;
import io.luchta.forma4j.reader.xml.parse.tagbuilder.TagBuilder;
import io.luchta.forma4j.reader.xml.parse.tagbuilder.TagBuilderFactory;
import io.luchta.forma4j.reader.model.tag.Tag;
import io.luchta.forma4j.reader.model.tag.TagTree;
import io.luchta.forma4j.reader.model.tag.TagTrees;
import io.luchta.forma4j.reader.model.tag.Tags;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class Parser {

    public TagTree parse(InputStream xml, SyntaxErrors syntaxErrors) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newDefaultInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(xml);
        TagTrees tagTrees = parse(new Tags(), document.getDocumentElement(), syntaxErrors);

        if (tagTrees.size() > 0) {
            return tagTrees.get(0);
        }
        return new TagTree();
    }

    private TagTrees parse(Tags dominators, Node node, SyntaxErrors syntaxErrors) {

        TagBuilderFactory factory = new TagBuilderFactory();
        List<TagTree> tagTreeList = new ArrayList<>();
        while(node != null) {

            TagBuilder builder = factory.create(node);

            if (builder != null) {
                Tag tag = builder.build(node.getAttributes(), syntaxErrors);

                Tags clone = dominators.clone();
                Tags newTags = clone.add(tag);
                TagTrees children = parse(newTags, node.getFirstChild(), syntaxErrors);

                TagTree tree = new TagTree(dominators, tag, children);
                tagTreeList.add(tree);
            }

            node = node.getNextSibling();
        }
        return new TagTrees(tagTreeList);
    }
}
