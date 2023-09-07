package io.luchta.forma4j.writer.engine.resolver;

import io.luchta.forma4j.antlr.style.StyleLexer;
import io.luchta.forma4j.antlr.style.StyleParser;
import io.luchta.forma4j.writer.definition.schema.attribute.Style;
import io.luchta.forma4j.writer.engine.model.cell.style.XlsxBorderStyleProperty;
import io.luchta.forma4j.writer.engine.model.cell.style.XlsxCellStyle;
import io.luchta.forma4j.writer.engine.model.cell.style.XlsxCellStyleProperty;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.apache.poi.ss.usermodel.BorderStyle;

import java.util.ArrayList;
import java.util.List;

public class StyleResolver {
    public XlsxCellStyle get(Style style) {
        if (style.isEmpty()) {
            return new XlsxCellStyle();
        }

        StyleLexer styleLexer = new StyleLexer(CharStreams.fromString(style.toString()));
        CommonTokenStream commonTokenStream = new CommonTokenStream(styleLexer);
        StyleParser styleParser = new StyleParser(commonTokenStream);
        StyleParser.StylesContext stylesContext = styleParser.styles();

        if (stylesContext.exception != null) {
            return new XlsxCellStyle();
        }

        List<XlsxCellStyleProperty> list = new ArrayList<>();

        for (StyleParser.StyleContext styleContext : stylesContext.style()) {
            switch (styleContext.property().getText().toUpperCase()) {
                case XlsxBorderStyleProperty.NAME:
                    list.add(border(styleContext.property_value().getText()));
                    break;
                default: break;
            }
        }

        return new XlsxCellStyle(list);
    }

    private XlsxCellStyleProperty border(String property) {
        switch (property.toUpperCase()) {
            case "THIN": return new XlsxBorderStyleProperty(BorderStyle.THIN);
            case "MEDIUM": return new XlsxBorderStyleProperty(BorderStyle.MEDIUM);
            case "DASHED": return new XlsxBorderStyleProperty(BorderStyle.DASHED);
            case "DOTTED": return new XlsxBorderStyleProperty(BorderStyle.DOTTED);
            case "THICK": return new XlsxBorderStyleProperty(BorderStyle.THICK);
            case "DOUBLE": return new XlsxBorderStyleProperty(BorderStyle.DOUBLE);
            case "HAIR": return new XlsxBorderStyleProperty(BorderStyle.HAIR);
            case "MEDIUM_DASHED": return new XlsxBorderStyleProperty(BorderStyle.MEDIUM_DASHED);
            case "DASH_DOT": return new XlsxBorderStyleProperty(BorderStyle.DASH_DOT);
            case "MEDIUM_DASH_DOT": return new XlsxBorderStyleProperty(BorderStyle.MEDIUM_DASH_DOT);
            case "DASH_DOT_DOT": return new XlsxBorderStyleProperty(BorderStyle.DASH_DOT_DOT);
            case "MEDIUM_DASH_DOT_DOT": return new XlsxBorderStyleProperty(BorderStyle.MEDIUM_DASH_DOT_DOT);
            case "SLANTED_DASH_DOT": return new XlsxBorderStyleProperty(BorderStyle.SLANTED_DASH_DOT);
            default: return new XlsxBorderStyleProperty(BorderStyle.NONE);
        }
    }
}
