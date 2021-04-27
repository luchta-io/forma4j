package io.luchta.forma4j.reader;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;

import io.luchta.forma4j.databind.json.JsonObject;
import io.luchta.forma4j.reader.config.ConfigReader;
import io.luchta.forma4j.reader.config.element.Elements;
import io.luchta.forma4j.reader.excel.XlsxReader;
import org.xml.sax.SAXException;

public class Reader {
    
    public JsonObject read(InputStream definitionXml, InputStream inputXlsx) throws ParserConfigurationException, SAXException, IOException {
    	
    	ConfigReader configReader = new ConfigReader();
    	Elements elements = configReader.read(definitionXml);
    	
    	XlsxReader xlsxReader = new XlsxReader();
    	JsonObject jsonObject = xlsxReader.read(inputXlsx, elements);
    	return jsonObject;
    }
}
