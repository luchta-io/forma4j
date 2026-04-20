package io.luchta.forma4j.writer;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.luchta.forma4j.context.databind.json.JsonObject;
import io.luchta.forma4j.writer.definition.XmlDocument;
import io.luchta.forma4j.writer.definition.XmlDocumentReader;
import io.luchta.forma4j.writer.engine.XlsxModelBuilder;
import io.luchta.forma4j.writer.processor.XlsxWriteProcessor;

import java.io.InputStream;
import java.io.OutputStream;

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
        XmlDocument definition = XmlDocument.defaultXmlDocument();
        XlsxModelBuilder modelBuilder = new XlsxModelBuilder(definition, Context.from(jsonObject));
        XlsxWriteProcessor processor = new XlsxWriteProcessor(modelBuilder.accumulate());
        processor.process(outputXlsx);
    }

    public void write(InputStream definitionXml, OutputStream outputXlsx, JsonObject jsonObject) throws JsonProcessingException {
        XmlDocumentReader definitionReader = new XmlDocumentReader();
        XmlDocument definition = definitionReader.read(definitionXml);
        XlsxModelBuilder modelBuilder = new XlsxModelBuilder(definition, Context.from(jsonObject));
        XlsxWriteProcessor processor = new XlsxWriteProcessor(modelBuilder.accumulate());
        processor.process(outputXlsx);
    }
}
