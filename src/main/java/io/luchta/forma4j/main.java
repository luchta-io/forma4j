package io.luchta.forma4j;

import io.luchta.forma4j.context.databind.json.JsonNode;
import io.luchta.forma4j.context.databind.json.JsonNodes;
import io.luchta.forma4j.context.databind.json.JsonObject;
import io.luchta.forma4j.writer.Writer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

public class main {
    public static void main(String[] args) throws Exception {
        JsonNodes jsonNodes = new JsonNodes();
        JsonNode jsonNode1 = new JsonNode();
        jsonNode1.putVar("col1", new JsonObject("value1-1"));
        jsonNode1.putVar("col2", new JsonObject("value1-2"));
        jsonNode1.putVar("col3", new JsonObject("value1-3"));

        jsonNodes.add(jsonNode1);

        JsonNode jsonNode2 = new JsonNode();
        jsonNode1.putVar("col1", new JsonObject("value2-1"));
        jsonNode1.putVar("col2", new JsonObject("value2-2"));
        jsonNode1.putVar("col3", new JsonObject("value2-3"));

        jsonNodes.add(jsonNode2);

        InputStream in = new FileInputStream("writer/example.xml");
        File outFile = new File("example.xlsx");
        FileOutputStream out = new FileOutputStream(outFile);

        JsonNode jsonNode = new JsonNode();
        jsonNode.putVar("データリスト", new JsonObject(jsonNodes));

        Writer writer = new Writer();
        writer.write(in, out, new JsonObject(jsonNode));
    }
}
