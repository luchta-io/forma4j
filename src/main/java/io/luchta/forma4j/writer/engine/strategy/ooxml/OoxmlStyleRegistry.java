package io.luchta.forma4j.writer.engine.strategy.ooxml;

import io.luchta.forma4j.writer.engine.model.cell.style.XlsxCellStyle;
import io.luchta.forma4j.writer.processor.poi.CellStyleBuilder;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

final class OoxmlStyleRegistry implements Closeable {
    private final XSSFWorkbook sourceWorkbook = new XSSFWorkbook();
    private final Map<XlsxCellStyle, Integer> tokens = new HashMap<>();
    private final Map<Integer, XSSFCellStyle> sourceStyles = new LinkedHashMap<>();
    private final Map<Integer, Integer> sourceStyleIds = new HashMap<>();
    private int nextToken;

    int register(XlsxCellStyle style) {
        XlsxCellStyle effectiveStyle = style == null ? new XlsxCellStyle() : style;
        Integer existing = tokens.get(effectiveStyle);
        if (existing != null) {
            return existing;
        }
        CellStyle built = CellStyleBuilder.of(effectiveStyle, sourceWorkbook).build();
        int token = nextToken++;
        tokens.put(effectiveStyle, token);
        sourceStyles.put(token, (XSSFCellStyle) built);
        sourceStyleIds.put(
                token,
                sourceWorkbook.getStylesSource().getNumCellStyles() - 1
        );
        return token;
    }

    boolean hasCustomStyles() {
        return !sourceStyles.isEmpty();
    }

    PreparedStyles prepareBlank() {
        Map<Integer, Integer> mapping = new HashMap<>();
        for (Map.Entry<Integer, XSSFCellStyle> entry : sourceStyles.entrySet()) {
            mapping.put(entry.getKey(), sourceStyleIds.get(entry.getKey()));
        }
        return new PreparedStyles(sourceWorkbook.getStylesSource(), mapping);
    }

    PreparedStyles prepareTemplate(InputStream existingStyles) throws IOException {
        StylesTable target = existingStyles == null
                ? new StylesTable()
                : new StylesTable(existingStyles);
        Map<Integer, Integer> mapping = new HashMap<>();
        for (Map.Entry<Integer, XSSFCellStyle> entry : sourceStyles.entrySet()) {
            XSSFCellStyle targetStyle = target.createCellStyle();
            targetStyle.cloneStyleFrom(entry.getValue());
            mapping.put(entry.getKey(), target.getNumCellStyles() - 1);
        }
        return new PreparedStyles(target, mapping);
    }

    @Override
    public void close() throws IOException {
        sourceWorkbook.close();
    }

    static final class PreparedStyles {
        private final StylesTable styles;
        private final Map<Integer, Integer> styleIds;

        private PreparedStyles(StylesTable styles, Map<Integer, Integer> styleIds) {
            this.styles = styles;
            this.styleIds = styleIds;
        }

        int styleId(int token) {
            Integer id = styleIds.get(token);
            if (id == null) {
                throw new IllegalStateException("未登録のスタイルトークンです: " + token);
            }
            return id;
        }

        void write(OutputStream output) throws IOException {
            styles.writeTo(output);
        }
    }
}
