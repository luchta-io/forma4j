package io.luchta.forma4j.writer.processor;

import io.luchta.forma4j.writer.engine.model.book.XlsxBook;
import io.luchta.forma4j.writer.processor.poi.WorkbookBuilder;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * {@code XlsxWriteProcessor} は EXCEL の書き込みを行うクラスです。
 *
 * <p>
 * {@link XlsxBook} の内容に従って EXCEL に書き込みを行います。
 * </p>
 *
 * @since 0.1.0
 */
public class XlsxWriteProcessor {
    /**
     * Excelへ書き込む内容
     */
    XlsxBook model;

    /**
     * コンストラクタ
     * @param model Excelへ書き込む内容
     */
    public XlsxWriteProcessor(XlsxBook model) {
        this.model = model;
    }

    /**
     * Excel 書き込み処理
     * @param out 書き込み先のファイル
     */
    public void process(OutputStream out) {
        WorkbookBuilder builder = new WorkbookBuilder(model);
        try (Workbook workbook = builder.build()) {
            workbook.write(out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Excel で作成されたテンプレートを利用した書き込み処理
     * @param out 書き込み先のファイル
     * @param in テンプレートファイル
     */
    public void process(OutputStream out, InputStream in) {
        WorkbookBuilder builder = new WorkbookBuilder(model);
        try (Workbook workbook = builder.build(in)) {
            workbook.write(out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
