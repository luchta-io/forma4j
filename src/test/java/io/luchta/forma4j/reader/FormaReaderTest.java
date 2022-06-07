package io.luchta.forma4j.reader;

import io.luchta.forma4j.context.databind.convert.JsonSerializer;
import io.luchta.forma4j.context.databind.json.JsonNode;
import io.luchta.forma4j.context.databind.json.JsonNodes;
import io.luchta.forma4j.context.databind.json.JsonObject;
import io.luchta.forma4j.reader.excel.Accumulator;
import io.luchta.forma4j.reader.model.excel.Header;
import io.luchta.forma4j.reader.model.excel.Headers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.FileInputStream;
import java.io.InputStream;

public class FormaReaderTest {

    @ParameterizedTest
    @CsvSource({
            "reader/simple_cell_read_test.xml, reader/FormaReaderTest.xlsx"
    })
    public void simple_cell_read_test(String configPath, String excelPath) throws Exception {
        try (InputStream config = new FileInputStream(this.getClass().getClassLoader().getResource(configPath).getPath());
             InputStream excel = new FileInputStream(this.getClass().getClassLoader().getResource(excelPath).getPath());) {
            FormaReader formaReader = new FormaReader();
            JsonObject obj = formaReader.read(config, excel);

            Object root = obj.getValue();
            Assertions.assertEquals(true, root instanceof JsonNode);

            JsonObject sheet = ((JsonNode) root).getVar("forma-reader");
            Assertions.assertEquals(true, sheet.getValue() instanceof JsonNode);

            JsonObject cell = ((JsonNode) sheet.getValue()).getVar("data");
            Assertions.assertEquals(true, cell.getValue() instanceof JsonNode);

            JsonObject value = ((JsonNode) cell.getValue()).getVar("title");
            Assertions.assertEquals(true, value.getValue() instanceof String);
            Assertions.assertEquals("サンプル帳票", value.toString());
        }
    }

    @ParameterizedTest
    @CsvSource({
            "reader/simple_vfor_test.xml, reader/FormaReaderTest.xlsx"
    })
    public void simple_vfor_test(String configPath, String excelPath) throws Exception {
        try (InputStream config = new FileInputStream(this.getClass().getClassLoader().getResource(configPath).getPath());
             InputStream excel = new FileInputStream(this.getClass().getClassLoader().getResource(excelPath).getPath());) {
            FormaReader formaReader = new FormaReader();
            JsonObject obj = formaReader.read(config, excel);

            Object root = obj.getValue();
            Assertions.assertEquals(true, root instanceof JsonNode);

            JsonObject sheet = ((JsonNode) root).getVar("forma-reader");
            Assertions.assertEquals(true, sheet.getValue() instanceof JsonNode);

            JsonObject cell = ((JsonNode) sheet.getValue()).getVar("data");
            Assertions.assertEquals(true, cell.getValue() instanceof JsonNode);

            JsonObject value = ((JsonNode) cell.getValue()).getVar("employee");
            Assertions.assertEquals(true, value.getValue() instanceof JsonNodes);
            JsonNodes employeeNodes = (JsonNodes) value.getValue();
            Assertions.assertEquals(10, employeeNodes.size());

            JsonNode node1 = employeeNodes.get(0);
            Assertions.assertEquals("0001", node1.getVar("employeeCode").toString());
            Assertions.assertEquals("山田", node1.getVar("lastName").toString());
            Assertions.assertEquals("太郎", node1.getVar("firstName").toString());

            JsonNode node2 = employeeNodes.get(1);
            Assertions.assertEquals("0002", node2.getVar("employeeCode").toString());
            Assertions.assertEquals("山田", node2.getVar("lastName").toString());
            Assertions.assertEquals("二郎", node2.getVar("firstName").toString());

            JsonNode node3 = employeeNodes.get(2);
            Assertions.assertEquals("0003", node3.getVar("employeeCode").toString());
            Assertions.assertEquals("山田", node3.getVar("lastName").toString());
            Assertions.assertEquals("三郎", node3.getVar("firstName").toString());

            JsonNode node4 = employeeNodes.get(3);
            Assertions.assertEquals("0004", node4.getVar("employeeCode").toString());
            Assertions.assertEquals("山田", node4.getVar("lastName").toString());
            Assertions.assertEquals("よし子", node4.getVar("firstName").toString());

            JsonNode node5 = employeeNodes.get(4);
            Assertions.assertEquals("0005", node5.getVar("employeeCode").toString());
            Assertions.assertEquals("山田", node5.getVar("lastName").toString());
            Assertions.assertEquals("いつこ", node5.getVar("firstName").toString());

            JsonNode node6 = employeeNodes.get(5);
            Assertions.assertEquals("0006", node6.getVar("employeeCode").toString());
            Assertions.assertEquals("山田", node6.getVar("lastName").toString());
            Assertions.assertEquals("六実", node6.getVar("firstName").toString());

            JsonNode node7 = employeeNodes.get(6);
            Assertions.assertEquals("0007", node7.getVar("employeeCode").toString());
            Assertions.assertEquals("山田", node7.getVar("lastName").toString());
            Assertions.assertEquals("七海", node7.getVar("firstName").toString());

            JsonNode node8 = employeeNodes.get(7);
            Assertions.assertEquals("0008", node8.getVar("employeeCode").toString());
            Assertions.assertEquals("山田", node8.getVar("lastName").toString());
            Assertions.assertEquals("やすみ", node8.getVar("firstName").toString());

            JsonNode node9 = employeeNodes.get(8);
            Assertions.assertEquals("0009", node9.getVar("employeeCode").toString());
            Assertions.assertEquals("山田", node9.getVar("lastName").toString());
            Assertions.assertEquals("久太", node9.getVar("firstName").toString());

            JsonNode node10 = employeeNodes.get(9);
            Assertions.assertEquals("0010", node10.getVar("employeeCode").toString());
            Assertions.assertEquals("山田", node10.getVar("lastName").toString());
            Assertions.assertEquals("遠子", node10.getVar("firstName").toString());
        }
    }

    @ParameterizedTest
    @CsvSource({
            "reader/simple_hfor_test.xml, reader/FormaReaderTest.xlsx"
    })
    public void simple_hfor_test(String configPath, String excelPath) throws Exception {
        try (InputStream config = new FileInputStream(this.getClass().getClassLoader().getResource(configPath).getPath());
             InputStream excel = new FileInputStream(this.getClass().getClassLoader().getResource(excelPath).getPath());) {
            FormaReader formaReader = new FormaReader();
            JsonObject obj = formaReader.read(config, excel);

            Object root = obj.getValue();
            Assertions.assertEquals(true, root instanceof JsonNode);

            JsonObject sheet = ((JsonNode) root).getVar("forma-reader");
            Assertions.assertEquals(true, sheet.getValue() instanceof JsonNode);

            JsonObject cell = ((JsonNode) sheet.getValue()).getVar("data");
            Assertions.assertEquals(true, cell.getValue() instanceof JsonNode);

            JsonObject value = ((JsonNode) cell.getValue()).getVar("hfor");
            Assertions.assertEquals(true, value.getValue() instanceof JsonNode);
            JsonNode valueNode = (JsonNode) value.getValue();
            Assertions.assertEquals(13, valueNode.size());

            JsonObject obj1 = valueNode.getVar("value1");
            Assertions.assertEquals("社員コード", obj1.toString());

            JsonObject obj2 = valueNode.getVar("value2");
            Assertions.assertEquals("氏", obj2.toString());

            JsonObject obj3 = valueNode.getVar("value3");
            Assertions.assertEquals("名", obj3.toString());

            JsonObject obj4 = valueNode.getVar("value4");
            Assertions.assertEquals("不定１", obj4.toString());

            JsonObject obj5 = valueNode.getVar("value5");
            Assertions.assertEquals("不定２", obj5.toString());

            JsonObject obj6 = valueNode.getVar("value6");
            Assertions.assertEquals("不定３", obj6.toString());

            JsonObject obj7 = valueNode.getVar("value7");
            Assertions.assertEquals("不定４", obj7.toString());

            JsonObject obj8 = valueNode.getVar("value8");
            Assertions.assertEquals("不定５", obj8.toString());

            JsonObject obj9 = valueNode.getVar("value9");
            Assertions.assertEquals("不定６", obj9.toString());

            JsonObject obj10 = valueNode.getVar("value10");
            Assertions.assertEquals("不定７", obj10.toString());

            JsonObject obj11 = valueNode.getVar("value11");
            Assertions.assertEquals("不定８", obj11.toString());

            JsonObject obj12 = valueNode.getVar("value12");
            Assertions.assertEquals("不定９", obj12.toString());

            JsonObject obj13 = valueNode.getVar("value13");
            Assertions.assertEquals("不定１０", obj13.toString());
        }
    }

    @ParameterizedTest
    @CsvSource({
            "reader/simple_vfor_hfor_test.xml, reader/FormaReaderTest.xlsx"
    })
    public void simple_vfor_hfor_test(String configPath, String excelPath) throws Exception {
        try (InputStream config = new FileInputStream(this.getClass().getClassLoader().getResource(configPath).getPath());
             InputStream excel = new FileInputStream(this.getClass().getClassLoader().getResource(excelPath).getPath());) {
            FormaReader formaReader = new FormaReader();
            JsonObject obj = formaReader.read(config, excel);

            JsonSerializer serializer = new JsonSerializer();
            System.out.println(serializer.serializeFromJsonObject(obj));

            Object root = obj.getValue();
            Assertions.assertEquals(true, root instanceof JsonNode);

            JsonObject sheet = ((JsonNode) root).getVar("forma-reader");
            Assertions.assertEquals(true, sheet.getValue() instanceof JsonNode);

            JsonObject cell = ((JsonNode) sheet.getValue()).getVar("data");
            Assertions.assertEquals(true, cell.getValue() instanceof JsonNode);

            JsonObject value = ((JsonNode) cell.getValue()).getVar("employee");
            Assertions.assertEquals(true, value.getValue() instanceof JsonNodes);
            JsonNodes employeeNodes = (JsonNodes) value.getValue();
            Assertions.assertEquals(10, employeeNodes.size());

            JsonNode node1 = employeeNodes.get(0);
            Assertions.assertEquals(4, node1.size());
            JsonNode node1HFor = (JsonNode) node1.getVar("hfor").getValue();
            Assertions.assertEquals(10, node1HFor.size());

            JsonNode node2 = employeeNodes.get(1);
            Assertions.assertEquals(4, node1.size());
            JsonNode node2HFor = (JsonNode) node2.getVar("hfor").getValue();
            Assertions.assertEquals(4, node2HFor.size());

            JsonNode node3 = employeeNodes.get(2);
            Assertions.assertEquals(4, node3.size());
            JsonNode node3HFor = (JsonNode) node3.getVar("hfor").getValue();
            Assertions.assertEquals(0, node3HFor.size());

            JsonNode node4 = employeeNodes.get(3);
            Assertions.assertEquals(4, node4.size());
            JsonNode node4HFor = (JsonNode) node4.getVar("hfor").getValue();
            Assertions.assertEquals(2, node4HFor.size());

            JsonNode node5 = employeeNodes.get(4);
            Assertions.assertEquals(4, node5.size());
            JsonNode node5HFor = (JsonNode) node5.getVar("hfor").getValue();
            Assertions.assertEquals(2, node5HFor.size());

            JsonNode node6 = employeeNodes.get(5);
            Assertions.assertEquals(4, node6.size());
            JsonNode node6HFor = (JsonNode) node6.getVar("hfor").getValue();
            Assertions.assertEquals(0, node6HFor.size());

            JsonNode node7 = employeeNodes.get(6);
            Assertions.assertEquals(4, node7.size());
            JsonNode node7HFor = (JsonNode) node7.getVar("hfor").getValue();
            Assertions.assertEquals(0, node7HFor.size());

            JsonNode node8 = employeeNodes.get(7);
            Assertions.assertEquals(4, node8.size());
            JsonNode node8HFor = (JsonNode) node8.getVar("hfor").getValue();
            Assertions.assertEquals(0, node8HFor.size());

            JsonNode node9 = employeeNodes.get(8);
            Assertions.assertEquals(4, node9.size());
            JsonNode node9HFor = (JsonNode) node9.getVar("hfor").getValue();
            Assertions.assertEquals(10, node9HFor.size());

            JsonNode node10 = employeeNodes.get(9);
            Assertions.assertEquals(4, node10.size());
            JsonNode node10HFor = (JsonNode) node10.getVar("hfor").getValue();
            Assertions.assertEquals(1, node10HFor.size());
        }
    }

    @ParameterizedTest
    @CsvSource({
            "reader/simple_header_test.xml, reader/FormaReaderTest.xlsx"
    })
    public void simple_header_test(String configPath, String excelPath) throws Exception {
        try (InputStream config = new FileInputStream(this.getClass().getClassLoader().getResource(configPath).getPath());
             InputStream excel = new FileInputStream(this.getClass().getClassLoader().getResource(excelPath).getPath());) {
            FormaReader formaReader = new FormaReader();
            formaReader.read(config, excel);

            Headers headers = (Headers) Accumulator.getData("employeeHeader");
            Assertions.assertEquals(13, headers.size());

            Header header1 = headers.get(0);
            Assertions.assertEquals("社員コード", header1.getHeaderValue().toString());

            Header header2 = headers.get(1);
            Assertions.assertEquals("氏", header2.getHeaderValue().toString());

            Header header3 = headers.get(2);
            Assertions.assertEquals("名", header3.getHeaderValue().toString());

            Header header4 = headers.get(3);
            Assertions.assertEquals("不定１", header4.getHeaderValue().toString());

            Header header5 = headers.get(4);
            Assertions.assertEquals("不定２", header5.getHeaderValue().toString());

            Header header6 = headers.get(5);
            Assertions.assertEquals("不定３", header6.getHeaderValue().toString());

            Header header7 = headers.get(6);
            Assertions.assertEquals("不定４", header7.getHeaderValue().toString());

            Header header8 = headers.get(7);
            Assertions.assertEquals("不定５", header8.getHeaderValue().toString());

            Header header9 = headers.get(8);
            Assertions.assertEquals("不定６", header9.getHeaderValue().toString());

            Header header10 = headers.get(9);
            Assertions.assertEquals("不定７", header10.getHeaderValue().toString());

            Header header11 = headers.get(10);
            Assertions.assertEquals("不定８", header11.getHeaderValue().toString());

            Header header12 = headers.get(11);
            Assertions.assertEquals("不定９", header12.getHeaderValue().toString());

            Header header13 = headers.get(12);
            Assertions.assertEquals("不定１０", header13.getHeaderValue().toString());
        }
    }
}
