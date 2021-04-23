package jp.co.actier.luchta.forma4j.writer.definition;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertTrue;

class XmlDocumentReaderTest {
    String[] sampleXmls = {
            "writer/一覧.xml"
    };

    @Test
    void read() throws IOException {
        for (String xml : sampleXmls) {
            ClassLoader classLoader = getClass().getClassLoader();
            InputStream in = classLoader.getResource(xml).openStream();
            XmlDocument read = new XmlDocumentReader().read(in);
            assertTrue(true);
        }
    }
}
