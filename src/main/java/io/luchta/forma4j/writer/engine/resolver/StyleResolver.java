package io.luchta.forma4j.writer.engine.resolver;

import io.luchta.forma4j.antlr.style.StyleLexer;
import io.luchta.forma4j.antlr.style.StyleParser;
import io.luchta.forma4j.writer.definition.schema.attribute.Style;
import io.luchta.forma4j.writer.engine.model.cell.style.XlsxCellStyle;
import io.luchta.forma4j.writer.engine.model.cell.style.XlsxCellStyleProperty;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

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
            String propertyName = getPropertyName(styleContext);
            String propertyValue = getPropertyValue(styleContext);
            XlsxCellStyleProperty property = XlsxCellStyleProperty.of(propertyName, propertyValue);
            list.add(property);
        }

        return new XlsxCellStyle(list);
    }

    private static String getPropertyName(StyleParser.StyleContext styleContext) {
        return styleContext.property().getText().toUpperCase();
    }

    private static String getPropertyValue(StyleParser.StyleContext styleContext) {
        return styleContext.property_value().getText();
    }

}
