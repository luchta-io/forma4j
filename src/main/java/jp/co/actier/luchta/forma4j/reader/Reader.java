package jp.co.actier.luchta.forma4j.reader;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import jp.co.actier.luchta.forma4j.reader.config.ConfigReader;
import jp.co.actier.luchta.forma4j.reader.config.element.Elements;
import jp.co.actier.luchta.forma4j.reader.excel.XlsxReader;
import jp.co.actier.luchta.forma4j.databind.json.JsonObject;

public class Reader {
    
    public JsonObject read(InputStream definitionXml, InputStream inputXlsx) throws ParserConfigurationException, SAXException, IOException {
    	
    	ConfigReader configReader = new ConfigReader();
    	Elements elements = configReader.read(definitionXml);
    	
    	XlsxReader xlsxReader = new XlsxReader();
    	JsonObject jsonObject = xlsxReader.read(inputXlsx, elements);
    	return jsonObject;
    }
}
