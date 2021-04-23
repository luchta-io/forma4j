package jp.co.actier.luchta.forma4j.reader.config;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.xml.sax.SAXException;

import jp.co.actier.luchta.forma4j.reader.config.element.FBody;
import jp.co.actier.luchta.forma4j.reader.config.element.FCell;
import jp.co.actier.luchta.forma4j.reader.config.element.FCol;
import jp.co.actier.luchta.forma4j.reader.config.element.Elements;
import jp.co.actier.luchta.forma4j.reader.config.element.FHeader;
import jp.co.actier.luchta.forma4j.reader.config.element.FHorizontalFor;
import jp.co.actier.luchta.forma4j.reader.config.element.FRoot;
import jp.co.actier.luchta.forma4j.reader.config.element.FRow;
import jp.co.actier.luchta.forma4j.reader.config.element.FSheet;
import jp.co.actier.luchta.forma4j.reader.config.element.FVerticalFor;
import jp.co.actier.luchta.forma4j.reader.config.element.property.ColNumber;
import jp.co.actier.luchta.forma4j.reader.config.element.property.ElementName;
import jp.co.actier.luchta.forma4j.reader.config.element.property.RowNumber;
import jp.co.actier.luchta.forma4j.reader.config.element.property.SheetIndex;

public class ConfigReaderTest {

    @ParameterizedTest
    @ValueSource(strings = {
            "reader/excel/config/configreader-withlinebreak-test.xml",
            "reader/excel/config/configreader-nolinebreak-test.xml"
    })
    void test(String relativePath) throws ParserConfigurationException, SAXException, IOException {
        ConfigReader xmlReader = new ConfigReader();
        InputStream definitionXml = new FileInputStream(this.getClass().getClassLoader().getResource(relativePath).getPath());
        
        Elements result = xmlReader.read(definitionXml);
        assertEquals(1, result.size());
        assertEquals(FRoot.class, result.get(0).getClass());
        
        Elements resultChildren = result.get(0).getChildren();
        assertEquals(1, resultChildren.size());
        assertEquals(FSheet.class, resultChildren.get(0).getClass());
        assertEquals(new SheetIndex(0), ((FSheet)resultChildren.get(0)).getSheetIndex());
        assertEquals(new ElementName("sheet"), resultChildren.get(0).getName());
        
        Elements sheetChildren = resultChildren.get(0).getChildren();
        assertEquals(2, sheetChildren.size());
        assertEquals(FHeader.class, sheetChildren.get(0).getClass());
        assertEquals(new RowNumber(0), ((FHeader)sheetChildren.get(0)).getStartRow());
        assertEquals(new RowNumber(4), ((FHeader)sheetChildren.get(0)).getEndRow());
        assertEquals(new ElementName("header"), sheetChildren.get(0).getName());
        
        assertEquals(FBody.class, sheetChildren.get(1).getClass());
        assertEquals(new RowNumber(5), ((FBody)sheetChildren.get(1)).getStartRow());
        assertEquals(new ElementName("body"), sheetChildren.get(1).getName());
        
        Elements headerChildren = sheetChildren.get(0).getChildren();
        assertEquals(1, headerChildren.size());
        assertEquals(FCell.class, headerChildren.get(0).getClass());
        assertEquals(new RowNumber(3), ((FCell)headerChildren.get(0)).getRow());
        assertEquals(new ColNumber(2), ((FCell)headerChildren.get(0)).getCol());
        assertEquals(new ElementName("CreatedDate"), headerChildren.get(0).getName());
        
        Elements cellChildren = headerChildren.get(0).getChildren();
        assertEquals(0, cellChildren.size());
        
        Elements bodyChildren = sheetChildren.get(1).getChildren();
        assertEquals(1, bodyChildren.size());
        assertEquals(FVerticalFor.class, bodyChildren.get(0).getClass());
        assertEquals(new ElementName("Employees"), bodyChildren.get(0).getName());
        
        Elements vForChildren = bodyChildren.get(0).getChildren();
        assertEquals(1, vForChildren.size());
        assertEquals(FRow.class, vForChildren.get(0).getClass());
        assertEquals(new ElementName("Employee"), vForChildren.get(0).getName());
        
        Elements rowChildren = vForChildren.get(0).getChildren();
        assertEquals(4, rowChildren.size());
        assertEquals(FCol.class, rowChildren.get(0).getClass());
        assertEquals(new ElementName("EmployeeCode"), rowChildren.get(0).getName());
        assertEquals(new ColNumber(1), ((FCol)rowChildren.get(0)).getNum());
        
        assertEquals(FCol.class, rowChildren.get(1).getClass());
        assertEquals(new ElementName("FirstName"), rowChildren.get(1).getName());
        assertEquals(new ColNumber(2), ((FCol)rowChildren.get(1)).getNum());
        
        assertEquals(FCol.class, rowChildren.get(2).getClass());
        assertEquals(new ElementName("LastName"), rowChildren.get(2).getName());
        assertEquals(new ColNumber(3), ((FCol)rowChildren.get(2)).getNum());
        
        assertEquals(FHorizontalFor.class, rowChildren.get(3).getClass());
        assertEquals(new ElementName("Dynamics"), rowChildren.get(3).getName());
        
        Elements colChildren1 = rowChildren.get(0).getChildren();
        assertEquals(0, colChildren1.size());
        
        Elements colChildren2 = rowChildren.get(1).getChildren();
        assertEquals(0, colChildren2.size());
        
        Elements colChildren3 = rowChildren.get(2).getChildren();
        assertEquals(0, colChildren3.size());
        
        Elements hForChildren = rowChildren.get(3).getChildren();
        assertEquals(1, hForChildren.size());
        assertEquals(FCol.class, hForChildren.get(0).getClass());
        assertEquals(new ElementName("Dynamic"), hForChildren.get(0).getName());
    }
}
