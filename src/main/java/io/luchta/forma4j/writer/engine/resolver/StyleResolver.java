package io.luchta.forma4j.writer.engine.resolver;

import io.luchta.forma4j.antlr.style.StyleLexer;
import io.luchta.forma4j.antlr.style.StyleParser;
import io.luchta.forma4j.writer.definition.schema.attribute.Style;
import io.luchta.forma4j.writer.engine.model.cell.style.BorderProperty;
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
                case BorderProperty.NAME:
                    list.add(border(styleContext.property_value().getText()));
                    break;
                default: break;
            }
        }

        return new XlsxCellStyle(list);
    }

    private XlsxCellStyleProperty border(String property) {
        switch (property.toUpperCase()) {
            case "THIN": return new BorderProperty(BorderStyle.THIN);
            case "MEDIUM": return new BorderProperty(BorderStyle.MEDIUM);
            case "DASHED": return new BorderProperty(BorderStyle.DASHED);
            case "DOTTED": return new BorderProperty(BorderStyle.DOTTED);
            case "THICK": return new BorderProperty(BorderStyle.THICK);
            case "DOUBLE": return new BorderProperty(BorderStyle.DOUBLE);
            case "HAIR": return new BorderProperty(BorderStyle.HAIR);
            case "MEDIUM_DASHED": return new BorderProperty(BorderStyle.MEDIUM_DASHED);
            case "DASH_DOT": return new BorderProperty(BorderStyle.DASH_DOT);
            case "MEDIUM_DASH_DOT": return new BorderProperty(BorderStyle.MEDIUM_DASH_DOT);
            case "DASH_DOT_DOT": return new BorderProperty(BorderStyle.DASH_DOT_DOT);
            case "MEDIUM_DASH_DOT_DOT": return new BorderProperty(BorderStyle.MEDIUM_DASH_DOT_DOT);
            case "SLANTED_DASH_DOT": return new BorderProperty(BorderStyle.SLANTED_DASH_DOT);
            default: return new BorderProperty(BorderStyle.NONE);
        }
    }
}
