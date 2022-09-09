package io.luchta.forma4j.writer.engine.model.cell.style;

import io.luchta.forma4j.antlr.style.StyleLexer;
import io.luchta.forma4j.antlr.style.StyleParser;
import io.luchta.forma4j.writer.definition.schema.attribute.Style;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.apache.poi.ss.usermodel.BorderStyle;

import java.util.ArrayList;
import java.util.List;

public class XlsxCellStylesBuilder {
    public XlsxCellStyles build(Style style) {
        if (style.isEmpty()) {
            return new XlsxCellStyles();
        }

        StyleLexer styleLexer = new StyleLexer(CharStreams.fromString(style.toString()));
        CommonTokenStream commonTokenStream = new CommonTokenStream(styleLexer);
        StyleParser styleParser = new StyleParser(commonTokenStream);
        StyleParser.StylesContext stylesContext = styleParser.styles();

        if (stylesContext.exception != null) {
            return new XlsxCellStyles();
        }

        List<XlsxCellStyle> list = new ArrayList<>();

        for (StyleParser.StyleContext styleContext : stylesContext.style()) {
            switch (styleContext.property().getText().toUpperCase()) {
                case XlsxBorderStyle.NAME:
                    list.add(border(styleContext.property_value().getText()));
                    break;
                default: break;
            }
        }

        XlsxCellStyles styles = new XlsxCellStyles(list);
        return styles;
    }

    private XlsxCellStyle border(String property) {
        switch (property.toUpperCase()) {
            case "THIN": return new XlsxBorderStyle(BorderStyle.THIN);
            case "MEDIUM": return new XlsxBorderStyle(BorderStyle.MEDIUM);
            case "DASHED": return new XlsxBorderStyle(BorderStyle.DASHED);
            case "DOTTED": return new XlsxBorderStyle(BorderStyle.DOTTED);
            case "THICK": return new XlsxBorderStyle(BorderStyle.THICK);
            case "DOUBLE": return new XlsxBorderStyle(BorderStyle.DOUBLE);
            case "HAIR": return new XlsxBorderStyle(BorderStyle.HAIR);
            case "MEDIUM_DASHED": return new XlsxBorderStyle(BorderStyle.MEDIUM_DASHED);
            case "DASH_DOT": return new XlsxBorderStyle(BorderStyle.DASH_DOT);
            case "MEDIUM_DASH_DOT": return new XlsxBorderStyle(BorderStyle.MEDIUM_DASH_DOT);
            case "DASH_DOT_DOT": return new XlsxBorderStyle(BorderStyle.DASH_DOT_DOT);
            case "MEDIUM_DASH_DOT_DOT": return new XlsxBorderStyle(BorderStyle.MEDIUM_DASH_DOT_DOT);
            case "SLANTED_DASH_DOT": return new XlsxBorderStyle(BorderStyle.SLANTED_DASH_DOT);
            default: return new XlsxBorderStyle(BorderStyle.NONE);
        }
    }
}
