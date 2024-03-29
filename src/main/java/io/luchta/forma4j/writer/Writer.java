package io.luchta.forma4j.writer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import io.luchta.forma4j.context.databind.convert.JsonSerializer;
import io.luchta.forma4j.context.databind.json.JsonObject;
import io.luchta.forma4j.writer.definition.XmlDocument;
import io.luchta.forma4j.writer.definition.XmlDocumentReader;
import io.luchta.forma4j.writer.engine.XlsxModelBuilder;
import io.luchta.forma4j.writer.engine.model.book.XlsxBook;
import io.luchta.forma4j.writer.processor.XlsxWriteProcessor;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * {@code Writer} は設定ファイルに従って EXCEL の書き込みを行うクラスです。
 *
 * <p>
 * JSON 形式を扱う {@link JsonObject } クラスで渡されたデータを設定ファイルの内容に従って EXCEL に書き込みを行います。
 * </p>
 * <p>
 * 設定ファイルを記述せずに書き込みを行う場合は A1 セルから始まる一覧表であることを想定して EXCEL へ書き込みを行います。1 行目をヘッダ行として扱います。
 * </p>
 * <p>
 * バージョン 1.2.0 から非推奨となりました。{@link FormaWriter } を使用してください。
 * </p>
 *
 * @since 0.1.0
 */
@Deprecated
public class Writer {
    public void write(OutputStream outputXlsx, JsonObject jsonObject) throws JsonProcessingException {
        Context context = context(jsonObject);
        XmlDocument definition = XmlDocument.defaultXmlDocument();
        XlsxModelBuilder modelBuilder = new XlsxModelBuilder(definition, context);
        XlsxBook model = modelBuilder.build();
        XlsxWriteProcessor processor = new XlsxWriteProcessor(model);
        processor.process(outputXlsx);
    }

    public void write(InputStream definitionXml, OutputStream outputXlsx, JsonObject jsonObject) throws JsonProcessingException {
        Context context = context(jsonObject);

        XmlDocumentReader definitionReader = new XmlDocumentReader();
        XmlDocument definition = definitionReader.read(definitionXml);
        XlsxModelBuilder modelBuilder = new XlsxModelBuilder(definition, context);
        XlsxBook model = modelBuilder.build();
        XlsxWriteProcessor processor = new XlsxWriteProcessor(model);
        processor.process(outputXlsx);
    }

    private Context context(JsonObject jsonObject) throws JsonProcessingException {
        JsonSerializer serializer = new JsonSerializer();
        String json = serializer.serializeFromJsonObject(jsonObject);
        TypeReference<LinkedHashMap<String, Object>> reference = new TypeReference<LinkedHashMap<String, Object>>() {};
        ObjectMapper mapper = JsonMapper.builder()
                .enable(JsonReadFeature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER)
                .build();
        Map<String, Object> map = mapper.readValue(json, reference);
        return new Context(map);
    }
}
