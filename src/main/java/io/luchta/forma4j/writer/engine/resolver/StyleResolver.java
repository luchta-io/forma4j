package io.luchta.forma4j.writer.engine.resolver;

import io.luchta.forma4j.antlr.style.StyleLexer;
import io.luchta.forma4j.antlr.style.StyleParser;
import io.luchta.forma4j.writer.definition.schema.attribute.Style;
import io.luchta.forma4j.writer.engine.model.cell.style.NotSupportProperty;
import io.luchta.forma4j.writer.engine.model.cell.style.XlsxCellStyle;
import io.luchta.forma4j.writer.engine.model.cell.style.XlsxCellStyleProperty;
import io.luchta.forma4j.writer.engine.model.column.property.NotSupportColumnProperty;
import io.luchta.forma4j.writer.engine.model.column.property.XlsxColumnProperties;
import io.luchta.forma4j.writer.engine.model.column.property.XlsxColumnStyle;
import io.luchta.forma4j.writer.engine.model.column.property.XlsxColumnProperty;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import java.util.ArrayList;
import java.util.List;

public class StyleResolver {
    public XlsxCellStyle get(Style style) {
        if (style.isEmpty()) {
            return new XlsxCellStyle();
        }

        StyleParser.StylesContext stylesContext = makeStylesContext(style);
        if (stylesContext.exception != null) {
            return new XlsxCellStyle();
        }

        List<XlsxCellStyleProperty> list = new ArrayList<>();
        for (StyleParser.StyleContext styleContext : stylesContext.style()) {
            String propertyName = getPropertyName(styleContext);
            String propertyValue = getPropertyValue(styleContext);
            XlsxCellStyleProperty property = XlsxCellStyleProperty.of(propertyName, propertyValue);
            if (!(property instanceof NotSupportProperty)) {
                list.add(property);
            }
        }

        return new XlsxCellStyle(list);
    }

    public XlsxColumnProperties getColumnProperties(Style style) {
        if (style.isEmpty()) {
            return new XlsxColumnProperties();
        }

        StyleParser.StylesContext stylesContext = makeStylesContext(style);
        if (stylesContext.exception != null) {
            return new XlsxColumnProperties();
        }

        List<XlsxColumnProperty> list = new ArrayList<>();
        for (StyleParser.StyleContext styleContext : stylesContext.style()) {
            String propertyName = getPropertyName(styleContext);
            String propertyValue = getPropertyValue(styleContext);
            XlsxColumnProperty property = XlsxColumnProperty.of(propertyName, propertyValue);
            if (!(property instanceof NotSupportColumnProperty)) {
                list.add(property);
            }
        }

        return new XlsxColumnProperties(list);
    }

    private static StyleParser.StylesContext makeStylesContext(Style style) {
        StyleLexer styleLexer = new StyleLexer(CharStreams.fromString(style.toString()));
        CommonTokenStream commonTokenStream = new CommonTokenStream(styleLexer);
        StyleParser styleParser = new StyleParser(commonTokenStream);
        return styleParser.styles();
    }

    private String getPropertyName(StyleParser.StyleContext styleContext) {
        return styleContext.property().getText().strip().toUpperCase();
    }

    private String getPropertyValue(StyleParser.StyleContext styleContext) {
        return styleContext.property_value().getText().strip();
    }

}
