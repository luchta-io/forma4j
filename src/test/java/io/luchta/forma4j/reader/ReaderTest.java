package io.luchta.forma4j.reader;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;

import io.luchta.forma4j.databind.convert.JsonSerializer;
import io.luchta.forma4j.databind.json.JsonNode;
import io.luchta.forma4j.databind.json.JsonNodes;
import io.luchta.forma4j.databind.json.JsonObject;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

class ReaderTest {

    @Test
    void test() throws ParserConfigurationException, SAXException, IOException {

        JsonObject jsonObject = null;
    	try (InputStream inputXlsx = new FileInputStream(this.getClass().getClassLoader().getResource("reader/ExcelReaderTest.xlsx").getPath());
    	     InputStream definitionXml = new FileInputStream(this.getClass().getClassLoader().getResource("reader/configreader-withlinebreak-test.xml").getPath());) {
            Reader reader = new Reader();
            jsonObject = reader.read(definitionXml, inputXlsx);
        }

    	JsonSerializer jsonSerializer = new JsonSerializer();
        String json = jsonSerializer.serializeFromJsonObject(jsonObject);
        assertEquals("{\"luchta-reader\" : {\"sheet\" : {\"index\" : \"0\", \"header\" : {\"CreatedDate\" : \"2020-11-06T00:00\"}, \"body\" : {\"Employees\" : [{\"Employee\" : {\"EmployeeCode\" : \"0001\", \"Dynamics\" : [{\"Dynamic\" : \"1.05\"},{\"Dynamic\" : \"2.0\"},{\"Dynamic\" : \"3.0\"},{\"Dynamic\" : \"4.0\"},{\"Dynamic\" : \"5.0\"},{\"Dynamic\" : \"6.0\"},{\"Dynamic\" : \"\"},{\"Dynamic\" : \"\"},{\"Dynamic\" : \"\"},{\"Dynamic\" : \"\"}], \"FirstName\" : \"山田\", \"LastName\" : \"太郎\"}},{\"Employee\" : {\"EmployeeCode\" : \"0002\", \"Dynamics\" : [{\"Dynamic\" : \"7.0\"},{\"Dynamic\" : \"8.0\"},{\"Dynamic\" : \"9.0\"},{\"Dynamic\" : \"10.0\"},{\"Dynamic\" : \"\"},{\"Dynamic\" : \"\"},{\"Dynamic\" : \"\"},{\"Dynamic\" : \"\"},{\"Dynamic\" : \"\"},{\"Dynamic\" : \"\"}], \"FirstName\" : \"山田\", \"LastName\" : \"二郎\"}},{\"Employee\" : {\"EmployeeCode\" : \"0003\", \"Dynamics\" : [{\"Dynamic\" : \"\"},{\"Dynamic\" : \"\"},{\"Dynamic\" : \"\"},{\"Dynamic\" : \"\"},{\"Dynamic\" : \"\"},{\"Dynamic\" : \"\"},{\"Dynamic\" : \"\"},{\"Dynamic\" : \"\"},{\"Dynamic\" : \"\"},{\"Dynamic\" : \"\"}], \"FirstName\" : \"山田\", \"LastName\" : \"三郎\"}},{\"Employee\" : {\"EmployeeCode\" : \"0004\", \"Dynamics\" : [{\"Dynamic\" : \"11.0\"},{\"Dynamic\" : \"12.0\"},{\"Dynamic\" : \"\"},{\"Dynamic\" : \"\"},{\"Dynamic\" : \"\"},{\"Dynamic\" : \"\"},{\"Dynamic\" : \"\"},{\"Dynamic\" : \"\"},{\"Dynamic\" : \"\"},{\"Dynamic\" : \"\"}], \"FirstName\" : \"山田\", \"LastName\" : \"よし子\"}},{\"Employee\" : {\"EmployeeCode\" : \"0005\", \"Dynamics\" : [{\"Dynamic\" : \"13.0\"},{\"Dynamic\" : \"14.0\"},{\"Dynamic\" : \"\"},{\"Dynamic\" : \"\"},{\"Dynamic\" : \"\"},{\"Dynamic\" : \"\"},{\"Dynamic\" : \"\"},{\"Dynamic\" : \"\"},{\"Dynamic\" : \"\"},{\"Dynamic\" : \"\"}], \"FirstName\" : \"山田\", \"LastName\" : \"いつこ\"}},{\"Employee\" : {\"EmployeeCode\" : \"0006\", \"Dynamics\" : [{\"Dynamic\" : \"\"},{\"Dynamic\" : \"\"},{\"Dynamic\" : \"\"},{\"Dynamic\" : \"\"},{\"Dynamic\" : \"\"},{\"Dynamic\" : \"\"},{\"Dynamic\" : \"\"},{\"Dynamic\" : \"\"},{\"Dynamic\" : \"\"},{\"Dynamic\" : \"\"}], \"FirstName\" : \"山田\", \"LastName\" : \"六実\"}},{\"Employee\" : {\"EmployeeCode\" : \"0007\", \"Dynamics\" : [{\"Dynamic\" : \"\"},{\"Dynamic\" : \"\"},{\"Dynamic\" : \"\"},{\"Dynamic\" : \"\"},{\"Dynamic\" : \"\"},{\"Dynamic\" : \"\"},{\"Dynamic\" : \"\"},{\"Dynamic\" : \"\"},{\"Dynamic\" : \"\"},{\"Dynamic\" : \"\"}], \"FirstName\" : \"山田\", \"LastName\" : \"七海\"}},{\"Employee\" : {\"EmployeeCode\" : \"0008\", \"Dynamics\" : [{\"Dynamic\" : \"\"},{\"Dynamic\" : \"\"},{\"Dynamic\" : \"\"},{\"Dynamic\" : \"\"},{\"Dynamic\" : \"\"},{\"Dynamic\" : \"\"},{\"Dynamic\" : \"\"},{\"Dynamic\" : \"\"},{\"Dynamic\" : \"\"},{\"Dynamic\" : \"\"}], \"FirstName\" : \"山田\", \"LastName\" : \"やすみ\"}},{\"Employee\" : {\"EmployeeCode\" : \"0009\", \"Dynamics\" : [{\"Dynamic\" : \"15.0\"},{\"Dynamic\" : \"16.0\"},{\"Dynamic\" : \"17.0\"},{\"Dynamic\" : \"18.0\"},{\"Dynamic\" : \"19.0\"},{\"Dynamic\" : \"20.0\"},{\"Dynamic\" : \"21.0\"},{\"Dynamic\" : \"22.0\"},{\"Dynamic\" : \"23.0\"},{\"Dynamic\" : \"24.0\"}], \"FirstName\" : \"山田\", \"LastName\" : \"久太\"}},{\"Employee\" : {\"EmployeeCode\" : \"0010\", \"Dynamics\" : [{\"Dynamic\" : \"24.0\"},{\"Dynamic\" : \"\"},{\"Dynamic\" : \"\"},{\"Dynamic\" : \"\"},{\"Dynamic\" : \"\"},{\"Dynamic\" : \"\"},{\"Dynamic\" : \"\"},{\"Dynamic\" : \"\"},{\"Dynamic\" : \"\"},{\"Dynamic\" : \"\"}], \"FirstName\" : \"山田\", \"LastName\" : \"遠子\"}}]}}}}", json);
        
        JsonNode root = (JsonNode) jsonObject.getValue();
        assertEquals(1, root.size());
        
        JsonObject rootObject = (JsonObject) root.getVar("luchta-reader");
        JsonNode rootNode = (JsonNode) rootObject.getValue();
        assertEquals(1, rootNode.size());
        
        JsonObject sheetObject = (JsonObject) rootNode.getVar("sheet");
        JsonNode sheetNode = (JsonNode) sheetObject.getValue();
        assertEquals(3, sheetNode.size());
        
        JsonObject indexObject = (JsonObject) sheetNode.getVar("index");
        assertEquals(0, indexObject.getValue());
        
        JsonObject headerObject = (JsonObject) sheetNode.getVar("header");
        JsonNode headerNode = (JsonNode) headerObject.getValue();
        assertEquals(1, headerNode.size());
        
        JsonObject createDateObject = (JsonObject) headerNode.getVar("CreatedDate");
        assertEquals("2020-11-06T00:00", createDateObject.getValue());
        
        JsonObject bodyObject = (JsonObject) sheetNode.getVar("body");
        JsonNode bodyNode = (JsonNode) bodyObject.getValue();
        assertEquals(1, bodyNode.size());
        
        JsonObject employeesObject = (JsonObject) bodyNode.getVar("Employees");
        JsonNodes employeesNodes = (JsonNodes) employeesObject.getValue();
        assertEquals(10, employeesNodes.size());
        
        JsonObject employeeObject = (JsonObject) employeesNodes.get(0).getVar("Employee");
        JsonNode employeeNode = (JsonNode) employeeObject.getValue();
        assertEquals(4, employeeNode.size());
        
        JsonObject employeeCodeObject = (JsonObject) employeeNode.getVar("EmployeeCode");
        assertEquals("0001", employeeCodeObject.getValue());
        
        JsonObject firstNameObject = (JsonObject) employeeNode.getVar("FirstName");
        assertEquals("山田", firstNameObject.getValue());
        
        JsonObject lastNameObject = (JsonObject) employeeNode.getVar("LastName");
        assertEquals("太郎", lastNameObject.getValue());
        
        JsonObject dynamicsObject = (JsonObject) employeeNode.getVar("Dynamics");
        JsonNodes dynamicsNodes = (JsonNodes) dynamicsObject.getValue();
        assertEquals(10, dynamicsNodes.size());
        
        JsonObject dynamicObject1 = (JsonObject) dynamicsNodes.get(0).getVar("Dynamic");
        assertEquals("1.05", dynamicObject1.getValue());
        
        JsonObject dynamicObject2 = (JsonObject) dynamicsNodes.get(1).getVar("Dynamic");
        assertEquals("2.0", dynamicObject2.getValue());
        
        JsonObject dynamicObject3 = (JsonObject) dynamicsNodes.get(2).getVar("Dynamic");
        assertEquals("3.0", dynamicObject3.getValue());
        
        JsonObject dynamicObject4 = (JsonObject) dynamicsNodes.get(3).getVar("Dynamic");
        assertEquals("4.0", dynamicObject4.getValue());
        
        JsonObject dynamicObject5 = (JsonObject) dynamicsNodes.get(4).getVar("Dynamic");
        assertEquals("5.0", dynamicObject5.getValue());
        
        JsonObject dynamicObject6 = (JsonObject) dynamicsNodes.get(5).getVar("Dynamic");
        assertEquals("6.0", dynamicObject6.getValue());
        
        JsonObject dynamicObject7 = (JsonObject) dynamicsNodes.get(6).getVar("Dynamic");
        assertEquals("", dynamicObject7.getValue());
        
        JsonObject dynamicObject8 = (JsonObject) dynamicsNodes.get(7).getVar("Dynamic");
        assertEquals("", dynamicObject8.getValue());
        
        JsonObject dynamicObject9 = (JsonObject) dynamicsNodes.get(8).getVar("Dynamic");
        assertEquals("", dynamicObject9.getValue());
        
        JsonObject dynamicObject10 = (JsonObject) dynamicsNodes.get(9).getVar("Dynamic");
        assertEquals("", dynamicObject10.getValue());
    }
}
