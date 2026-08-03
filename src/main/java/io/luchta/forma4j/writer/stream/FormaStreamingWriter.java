package io.luchta.forma4j.writer.stream;

import io.luchta.forma4j.writer.Context;
import io.luchta.forma4j.context.databind.json.JsonObject;
import io.luchta.forma4j.writer.definition.XmlDocument;
import io.luchta.forma4j.writer.definition.XmlDocumentReader;
import io.luchta.forma4j.writer.engine.XlsxModelBuilder;
import io.luchta.forma4j.writer.engine.strategy.StreamingOutputStrategy;
import io.luchta.forma4j.writer.engine.strategy.ooxml.AbstractOoxmlOutputStrategy;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UncheckedIOException;

/**
 * Context、JsonObject、またはルートJSON配列からXLSXを逐次生成するWriterです。
 * セル命令を一時ファイルへ退避し、OOXMLパッケージを低メモリで逐次生成します。
 * テンプレート指定時は未変更のOOXMLパーツを保持し、対象シートだけを変換します。
 * {@code autoSizeColumn} は無効です。テンプレートは非暗号化の {@code .xlsx} のみを対象とし、
 * マクロまたは電子署名を含むパッケージは受け付けません。
 */
public class FormaStreamingWriter {
    /**
     * ルートJSON配列を固定名 {@code list} として出力します。
     *
     * <p>引数として渡された全Streamは呼び出し側が所有し、このメソッドは閉じません。</p>
     */
    public void write(InputStream definitionXml, OutputStream outputXlsx, InputStream json) throws IOException {
        XmlDocument definition = readDefinition(definitionXml);
        Context context = new Context();
        context.putSequence("list", JsonArraySequenceSource.borrowed(json));
        write(definition, outputXlsx, context, null);
    }

    /**
     * ルート JSON 配列をテンプレート XLSX へ出力します。
     * JSON 配列は {@code list} として一度だけ列挙されます。
     * 引数として渡された全Streamは呼び出し側が所有し、このメソッドは閉じません。
     */
    public void write(
            InputStream definitionXml,
            OutputStream outputXlsx,
            InputStream templateXlsx,
            InputStream json
    ) throws IOException {
        XmlDocument definition = readDefinition(definitionXml);
        Context context = new Context();
        context.putSequence("list", JsonArraySequenceSource.borrowed(json));
        write(definition, outputXlsx, context, templateXlsx);
    }

    /**
     * Contextを再帰コピーせず、そのまま使用してXLSXを出力します。
     *
     * <p>引数として渡された全Streamは呼び出し側が所有し、このメソッドは閉じません。
     * SourceのSupplierから開いたStreamとiteratorはWriterが閉じます。</p>
     */
    public void write(
            InputStream definitionXml,
            OutputStream outputXlsx,
            Context context
    ) throws IOException {
        XmlDocument definition = readDefinition(definitionXml);
        write(definition, outputXlsx, context, null);
    }

    /**
     * Contextを再帰コピーせず、テンプレートXLSXへ出力します。
     *
     * <p>引数として渡された全Streamは呼び出し側が所有し、このメソッドは閉じません。
     * SourceのSupplierから開いたStreamとiteratorはWriterが閉じます。</p>
     */
    public void write(
            InputStream definitionXml,
            OutputStream outputXlsx,
            InputStream templateXlsx,
            Context context
    ) throws IOException {
        XmlDocument definition = readDefinition(definitionXml);
        write(definition, outputXlsx, context, templateXlsx);
    }

    /**
     * 既存の JsonObject を入力として、OOXMLパッケージを逐次出力します。
     * JSON データ自体は呼び出し元が保持します。
     * 引数として渡されたStreamは閉じません。
     */
    public void write(InputStream definitionXml, OutputStream outputXlsx, JsonObject jsonObject) throws IOException {
        XmlDocument definition = readDefinition(definitionXml);
        write(definition, outputXlsx, Context.from(jsonObject), null);
    }

    /**
     * 既存の JsonObject をテンプレート XLSX へ出力します。
     * JSONデータ自体は呼び出し元が保持し、Excelパッケージだけを逐次変換します。
     * 引数として渡されたStreamは閉じません。
     */
    public void write(
            InputStream definitionXml,
            OutputStream outputXlsx,
            InputStream templateXlsx,
            JsonObject jsonObject
    ) throws IOException {
        XmlDocument definition = readDefinition(definitionXml);
        write(definition, outputXlsx, Context.from(jsonObject), templateXlsx);
    }

    private void write(
            XmlDocument definition,
            OutputStream outputXlsx,
            Context context,
            InputStream templateXlsx
    ) throws IOException {
        try (SequenceSourceSession sources = new SequenceSourceSession(definition, context);
             AbstractOoxmlOutputStrategy strategy = templateXlsx == null
                     ? new StreamingOutputStrategy()
                     : new PackagePreservingTemplateOutputStrategy(templateXlsx)) {
            new XlsxModelBuilder(definition, context).write(strategy, sources::open);
            strategy.write(outputXlsx);
        } catch (UncheckedIOException e) {
            IOException cause = e.getCause();
            for (Throwable suppressed : e.getSuppressed()) {
                cause.addSuppressed(suppressed);
            }
            throw cause;
        }
    }

    private XmlDocument readDefinition(InputStream definitionXml) throws IOException {
        return new XmlDocumentReader().read(nonClosing(definitionXml));
    }

    private InputStream nonClosing(InputStream input) {
        return new FilterInputStream(input) {
            @Override
            public void close() {
                // 呼び出し側が所有するStreamは閉じません。
            }
        };
    }
}
