package io.luchta.forma4j.writer;

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
import io.luchta.forma4j.writer.processor.XlsxPassword;
import io.luchta.forma4j.writer.processor.XlsxWriteProcessor;

import java.io.FileOutputStream;
import java.io.IOException;
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
 *
 * @since 1.2.0
 */
public class FormaWriter {
    /**
     * EXCEL の書き込みを行うメソッドです
     * <p>
     * 設定ファイルなしで書き込みを行います。A1 セルから始まる一覧表であることを想定しています。
     * </p>
     * <p>
     * JSON の構造は以下のような構造になっていることを想定しています。
     * </p>
     * <p>
     * {"list" : [{"key1" : "value1-1", "key2" : "value1-2"},{"key1" : "value2-1", "key2" : "value2-2"}]}
     * </p>
     * <p>
     * key1, key2 となっている箇所が一覧表のヘッダとして扱われます。また、list となっている箇所はどのような値でも構いません。JSON 形式のテキストを {@link JsonObject} に変換する方法は {@link io.luchta.forma4j.context.databind.convert.JsonDeserializer} を確認してください。
     * </p>
     * @param outputXlsx EXCEL ファイルの書き込み先です
     * @param jsonObject 書き込むデータを JSON 形式にしたオブジェクトです
     * @throws IOException
     */
    public void write(OutputStream outputXlsx, JsonObject jsonObject) throws IOException {
        XlsxWriteProcessor processor = processor(null, jsonObject);
        processor.process(outputXlsx);
    }

    /**
     * EXCEL の書き込みを行うメソッドです
     * <p>
     * 設定ファイルの内容に従って書き込みを行います。
     * </p>
     * @param definitionXml 設定ファイル
     * @param outputXlsx EXCEL の書き込み先
     * @param jsonObject 書き込むデータを JSON 形式にしたオブジェクト
     * @throws IOException
     */
    public void write(InputStream definitionXml, OutputStream outputXlsx, JsonObject jsonObject) throws IOException {
        XlsxWriteProcessor processor = processor(definitionXml, jsonObject);
        processor.process(outputXlsx);
    }

    /**
     * EXCEL の書き込みを行うメソッドです
     * <p>
     * 設定ファイルの内容に従い、Excelで作成されたテンプレートへ書き込みを行います
     * </p>
     * @param definitionXml 設定ファイル
     * @param outputXlsx EXCEL の書き込み先
     * @param templateXlsx EXCEL のテンプレート
     * @param jsonObject 書き込むデータを JSON 形式にしたオブジェクト
     * @throws IOException
     */
    public void write(InputStream definitionXml, OutputStream outputXlsx, InputStream templateXlsx, JsonObject jsonObject) throws IOException {
        XlsxWriteProcessor processor = processor(definitionXml, jsonObject);
        processor.process(outputXlsx, templateXlsx);
    }

    /**
     * EXCEL の書き込みを行うメソッドです
     * <p>
     * 設定ファイルの内容に従い、Excelで作成されたテンプレートへ書き込みを行います
     * </p>
     * @param definitionXml 設定ファイル
     * @param outputXlsxPath EXCEL の書き込み先
     * @param password 書き込み先のEXCELに設定するパスワード
     * @param jsonObject 書き込むデータを JSON 形式にしたオブジェクト
     * @throws IOException
     */
    public void write(InputStream definitionXml, String outputXlsxPath, String password, JsonObject jsonObject) throws IOException {
        XlsxWriteProcessor processor = processor(definitionXml, jsonObject);
        processor.process(new FileOutputStream(outputXlsxPath));
        XlsxPassword xlsxPassword = new XlsxPassword();
        xlsxPassword.set(outputXlsxPath, password);
    }

    /**
     * EXCEL の書き込みを行うメソッドです
     * <p>
     * 設定ファイルの内容に従い、Excelで作成されたテンプレートへ書き込みを行います
     * </p>
     * @param definitionXml 設定ファイル
     * @param outputXlsxPath EXCEL の書き込み先
     * @param password 書き込み先のEXCELに設定するパスワード
     * @param templateXlsx EXCEL のテンプレート
     * @param jsonObject 書き込むデータを JSON 形式にしたオブジェクト
     * @throws IOException
     */
    public void write(InputStream definitionXml, String outputXlsxPath, String password, InputStream templateXlsx, JsonObject jsonObject) throws IOException {
        XlsxWriteProcessor processor = processor(definitionXml, jsonObject);
        processor.process(new FileOutputStream(outputXlsxPath), templateXlsx);
        XlsxPassword xlsxPassword = new XlsxPassword();
        xlsxPassword.set(outputXlsxPath, password);
    }

    private XlsxWriteProcessor processor(InputStream definitionXml, JsonObject jsonObject) throws IOException {
        Context context = context(jsonObject);

        XmlDocumentReader definitionReader = new XmlDocumentReader();
        XmlDocument definition = definitionXml == null ? XmlDocument.defaultXmlDocument() : definitionReader.read(definitionXml);
        XlsxModelBuilder modelBuilder = new XlsxModelBuilder(definition, context);
        XlsxBook model = modelBuilder.build();
        XlsxWriteProcessor processor = new XlsxWriteProcessor(model);
        return processor;
    }

    private Context context(JsonObject jsonObject) throws IOException {
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
