package io.luchta.forma4j.writer.processor;

import io.luchta.forma4j.writer.processor.poi.WorkbookBuilder;
import io.luchta.forma4j.writer.engine.model.book.XlsxBook;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.IOException;
import java.io.OutputStream;

public class XlsxWriteProcessor {
    XlsxBook model;

    public XlsxWriteProcessor(XlsxBook model) {
        this.model = model;
    }

    public void process(OutputStream out) {
        WorkbookBuilder builder = new WorkbookBuilder(model);
        Workbook workbook = builder.build();
        try {
            workbook.write(out);
        } catch (IOException e) {
            // TODO
            e.printStackTrace();
        }
    }
}
