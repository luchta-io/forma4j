package jp.co.actier.luchta.forma4j.reader.excel;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.xml.sax.SAXException;

class XlsxReaderTest {

    @ParameterizedTest
    @ValueSource(strings = {
            "reader/excel/excel/configreader-withlinebreak-test.xml",
            "reader/excel/excel/configreader-nolinebreak-test.xml"
    })
    void test(String relativePath) throws ParserConfigurationException, SAXException, IOException {
//        ConfigReader configReader = new ConfigReader();
//        Elements elements = configReader.read(new ConfigPath(this.getClass().getClassLoader().getResource(relativePath).getPath()));
//        XlsxReader xlsxReader = new XlsxReader();
//        XlsxPath xlsxPath  =new XlsxPath(this.getClass().getClassLoader().getResource("reader/excel/excel/ExcelReaderTest.xlsx").getPath());
//        try (InputStream inputStream = new FileInputStream(xlsxPath.toString())) {
//            Map<String, Object> result = xlsxReader.read(inputStream, elements);
//            assertEquals(1, result.size());
//            assertEquals(true, result.containsKey("luchta-reader"));
//            
//            Map<String, Object> rootMap = (Map<String, Object>) result.get("luchta-reader");
//            assertEquals(1, rootMap.size());
//            assertEquals(true, rootMap.containsKey("sheet"));
//            
//            List<Map<String, Object>> sheetList = (List<Map<String, Object>>) rootMap.get("sheet");
//            assertEquals(1, sheetList.size());
//            assertEquals(true, sheetList.get(0).containsKey("index"));
//            assertEquals(true, sheetList.get(0).containsKey("header"));
//            assertEquals(true, sheetList.get(0).containsKey("body"));
//            
//            Integer index = (Integer) sheetList.get(0).get("index");
//            assertEquals(0, index);
//            
//            Map<String, Object> headerMap = (Map<String, Object>) sheetList.get(0).get("header");
//            assertEquals(1, headerMap.size());
//            assertEquals(true, headerMap.containsKey("CreatedDate"));
//            
//            Map<String, Object> createdDateMap = (Map<String, Object>) headerMap.get("CreatedDate");
//            assertEquals(1, createdDateMap.size());
//            assertEquals(true, createdDateMap.containsKey("value"));
//            
//            assertEquals("2020年11月6日", createdDateMap.get("value").toString());
//            
//            Map<String, Object> bodyMap = (Map<String, Object>) sheetList.get(0).get("body");
//            assertEquals(1, bodyMap.size());
//            assertEquals(true, bodyMap.containsKey("Employees"));
//            
//            List<Map<String, Object>> employeeList = (List<Map<String, Object>>) bodyMap.get("Employees");
//            assertEquals(10, employeeList.size());
//            assertEquals(true, employeeList.get(0).containsKey("Employee"));
//            
//            Map<String, Object> employeeMap = (Map<String, Object>) employeeList.get(0).get("Employee");
//            assertEquals(4, employeeMap.size());
//            assertEquals(true, employeeMap.containsKey("EmployeeCode"));
//            assertEquals(true, employeeMap.containsKey("FirstName"));
//            assertEquals(true, employeeMap.containsKey("LastName"));
//            assertEquals(true, employeeMap.containsKey("Dynamics"));
//            
//            Map<String, Object> employeeCodeMap = (Map<String, Object>) employeeMap.get("EmployeeCode");
//            assertEquals("0001", employeeCodeMap.get("value").toString());
//            Map<String, Object> firstNameMap = (Map<String, Object>) employeeMap.get("FirstName");
//            assertEquals("山田", firstNameMap.get("value").toString());
//            Map<String, Object> lastNameMap = (Map<String, Object>) employeeMap.get("LastName");
//            assertEquals("太郎", lastNameMap.get("value").toString());
//            
//            List<Map<String, Object>> dynamicsList = (List<Map<String, Object>>) employeeMap.get("Dynamics");
//            assertEquals(6, dynamicsList.size());
//            
//            Map<String, Object> dynamicMap1 = (Map<String, Object>) dynamicsList.get(0).get("Dynamic");
//            assertEquals(1, dynamicMap1.size());
//            assertEquals("1", dynamicMap1.get("value").toString());
//            
//            Map<String, Object> dynamicMap2 = (Map<String, Object>) dynamicsList.get(1).get("Dynamic");
//            assertEquals(1, dynamicMap2.size());
//            assertEquals("2", dynamicMap2.get("value").toString());
//            
//            Map<String, Object> dynamicMap3 = (Map<String, Object>) dynamicsList.get(2).get("Dynamic");
//            assertEquals(1, dynamicMap3.size());
//            assertEquals("3", dynamicMap3.get("value").toString());
//            
//            Map<String, Object> dynamicMap4 = (Map<String, Object>) dynamicsList.get(3).get("Dynamic");
//            assertEquals(1, dynamicMap4.size());
//            assertEquals("4", dynamicMap4.get("value").toString());
//            
//            Map<String, Object> dynamicMap5 = (Map<String, Object>) dynamicsList.get(4).get("Dynamic");
//            assertEquals(1, dynamicMap5.size());
//            assertEquals("5", dynamicMap5.get("value").toString());
//            
//            Map<String, Object> dynamicMap6 = (Map<String, Object>) dynamicsList.get(5).get("Dynamic");
//            assertEquals(1, dynamicMap6.size());
//            assertEquals("6", dynamicMap6.get("value").toString());
//        }
    }
}
