package io.luchta.forma4j.writer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.luchta.forma4j.writer.engine.XlsxModelBuilder;
import io.luchta.forma4j.writer.definition.XmlDocumentReader;
import io.luchta.forma4j.writer.processor.XlsxWriteProcessor;
import io.luchta.forma4j.context.databind.convert.JsonSerializer;
import io.luchta.forma4j.context.databind.json.JsonObject;
import com.fasterxml.jackson.databind.json.JsonMapper;
import io.luchta.forma4j.writer.definition.XmlDocument;
import io.luchta.forma4j.writer.engine.model.book.XlsxBook;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class Writer {

    public void write(InputStream definitionXml, OutputStream outputXlsx, JsonObject jsonObject) throws JsonProcessingException {

        JsonSerializer serializer = new JsonSerializer();
        String json = serializer.serializeFromJsonObject(jsonObject);
        TypeReference<HashMap<String, Object>> reference = new TypeReference<HashMap<String, Object>>() {};
        ObjectMapper mapper = JsonMapper.builder()
                .enable(JsonReadFeature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER)
                .build();
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
