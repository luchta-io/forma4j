package jp.co.actier.luchta.forma4j.writer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jp.co.actier.luchta.forma4j.databind.convert.JsonSerializer;
import jp.co.actier.luchta.forma4j.databind.json.JsonObject;
import jp.co.actier.luchta.forma4j.writer.definition.XmlDocument;
import jp.co.actier.luchta.forma4j.writer.definition.XmlDocumentReader;
import jp.co.actier.luchta.forma4j.writer.engine.XlsxModelBuilder;
import jp.co.actier.luchta.forma4j.writer.engine.model.book.XlsxBook;
import jp.co.actier.luchta.forma4j.writer.processor.XlsxWriteProcessor;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class Writer {

    public void write(InputStream definitionXml, OutputStream outputXlsx, JsonObject jsonObject) throws JsonProcessingException {

        JsonSerializer serializer = new JsonSerializer();
        String json = serializer.serializeFromJsonObject(jsonObject);
        TypeReference<HashMap<String, Object>> reference = new TypeReference<HashMap<String, Object>>() {};
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = mapper.readValue(json, reference);
        Context context = new Context(map);

        XmlDocumentReader definitionReader = new XmlDocumentReader();
        XmlDocument definition = definitionReader.read(definitionXml);
        XlsxModelBuilder modelBuilder = new XlsxModelBuilder(definition, context);
        XlsxBook model = modelBuilder.build();
        XlsxWriteProcessor processor = new XlsxWriteProcessor(model);
        processor.process(outputXlsx);
    }
}
