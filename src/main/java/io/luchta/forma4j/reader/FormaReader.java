package io.luchta.forma4j.reader;

import io.luchta.forma4j.context.syntax.SyntaxErrors;
import io.luchta.forma4j.context.databind.json.JsonObject;
import io.luchta.forma4j.reader.compile.FormaReaderCompiler;
import io.luchta.forma4j.reader.excel.ExcelReader;
import io.luchta.forma4j.reader.model.tag.TagTree;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;

public class FormaReader {

    SyntaxErrors syntaxErrors = new SyntaxErrors();

    public JsonObject read(InputStream excel) throws IOException {
        ExcelReader excelReader = new ExcelReader();
        return excelReader.read(excel);
    }

    public JsonObject read(InputStream config, InputStream excel) throws ParserConfigurationException, IOException, SAXException {

        FormaReaderCompiler compiler = new FormaReaderCompiler();
        TagTree tree = compiler.compile(config, syntaxErrors);

        if (syntaxErrors.hasErrors()) {
            throw new IllegalArgumentException("設定ファイルの構文にエラーがあります");
        }

        ExcelReader excelReader = new ExcelReader();
        return excelReader.read(excel, tree);
    }

    public SyntaxErrors syntaxErrors() {
        return syntaxErrors;
    }
}
