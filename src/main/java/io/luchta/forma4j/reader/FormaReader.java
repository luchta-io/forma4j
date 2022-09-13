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

/**
 * {@code FormaReader} は設定ファイルに従って EXCEL の読み込みを行うクラスです。
 *
 * <p>
 * 読み込まれた EXCEL は JSON 形式を扱う {@link JsonObject } クラスに変換されます。
 * </p>
 * <p>
 * 設定ファイルを記述せずに読み込みを行う場合は A1 セルから始まる一覧表であることを想定して EXCEL の読み込みを行います。1 行目をヘッダ行として扱います。ヘッダ行に設定されている値が JSON のキー名となります。
 * </p>
 *
 * @since 0.1.0
 */
public class FormaReader {
    private SyntaxErrors syntaxErrors = new SyntaxErrors();

    /**
     * EXCEL の読み込みを行うメソッドです。
     * <p>
     * 設定ファイルなしで読み込みを行います。A1 セルから始まる一覧表であることを想定しています。
     * </p>
     * <p>
     * 1 行目をヘッダ行、ヘッダ行に設定されている値を JSON のキー名として読み込みます。
     * </p>
     * @param excel 読み込みを行う EXCEL ファイル
     * @return 読み込んだ EXCEL ファイルを JSON 形式に変換したオブジェクト
     * @throws IOException
     */
    public JsonObject read(InputStream excel) throws IOException {
        ExcelReader excelReader = new ExcelReader();
        return excelReader.read(excel);
    }

    /**
     * EXCEL の読み込みを行うメソッドです。
     * <p>
     * 設定ファイルの内容に従って読み込みを行います。
     * </p>
     * @param config EXCEL の読み込み定義を記述した設定ファイル
     * @param excel 読み込みを行う EXCEL ファイル
     * @return 読み込んだ EXCEL ファイルを JSON 形式に変換したオブジェクト
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     */
    public JsonObject read(InputStream config, InputStream excel) throws ParserConfigurationException, IOException, SAXException {
        FormaReaderCompiler compiler = new FormaReaderCompiler();
        TagTree tree = compiler.compile(config, syntaxErrors);

        if (syntaxErrors.hasErrors()) {
            throw new IllegalArgumentException("設定ファイルの構文にエラーがあります");
        }

        ExcelReader excelReader = new ExcelReader();
        return excelReader.read(excel, tree);
    }

    /**
     * EXCEL の読み込み定義を記述した設定ファイルのエラーを取得するメソッドです。
     * <p>
     * {@link FormaReader#read(InputStream, InputStream)} を実行後に取得できるようになります。実行前に取得した場合、{@link SyntaxErrors} は空の状態です。
     * </p>
     * @return EXCEL の読み込み定義を記述した設定ファイルのエラー内容
     */
    public SyntaxErrors syntaxErrors() {
        return syntaxErrors;
    }
}
