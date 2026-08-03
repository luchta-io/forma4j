package io.luchta.forma4j.writer.stream;

import io.luchta.forma4j.writer.engine.strategy.ooxml.AbstractOoxmlOutputStrategy;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/**
 * テンプレートのOOXMLパーツを保持し、対象ワークシートだけを逐次変換する内部戦略です。
 */
final class PackagePreservingTemplateOutputStrategy
        extends AbstractOoxmlOutputStrategy {
    private final Path template;

    PackagePreservingTemplateOutputStrategy(InputStream templateXlsx) throws IOException {
        super();
        template = temporaryDirectory.resolve("template.xlsx");
        try {
            Files.copy(templateXlsx, template, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            try {
                close();
            } catch (IOException closeFailure) {
                e.addSuppressed(closeFailure);
            }
            throw e;
        }
    }

    @Override
    protected void writePackage(OutputStream outputStream) throws IOException {
        writeTemplateOoxmlPackage(template, outputStream);
    }
}
