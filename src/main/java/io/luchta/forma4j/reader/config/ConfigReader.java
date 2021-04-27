package io.luchta.forma4j.reader.config;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import io.luchta.forma4j.reader.config.element.Element;
import io.luchta.forma4j.reader.config.element.Elements;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import io.luchta.forma4j.reader.config.elementfactory.ElementBuilder;
import io.luchta.forma4j.reader.config.elementfactory.ElementBuilderFactory;

public class ConfigReader {
    
    public Elements read(InputStream definitionXml) throws ParserConfigurationException, SAXException, IOException {
    	
    	DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
    	DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
    	Document document = documentBuilder.parse(definitionXml);
    	
    	Node node = document.getDocumentElement();
    	return this.read(node);
    }
    
    private Elements read(Node node) {
        
    	Elements elements = new Elements();
        while(node != null) {
            ElementBuilder elementBuilder = ElementBuilderFactory.newElementBuilder(node.getNodeName());
            if (elementBuilder != null) {
                Element element = elementBuilder.build(node.getAttributes());
                elements.add(element);
                element.addChildren(this.read(node.getFirstChild()));
            }
            node = node.getNextSibling();
        }        
        return elements;
    }
}
