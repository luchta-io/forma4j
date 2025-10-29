package io.luchta.forma4j.writer.engine.resolver;

import io.luchta.forma4j.antlr.style.StyleLexer;
import io.luchta.forma4j.antlr.style.StyleParser;
import io.luchta.forma4j.writer.definition.schema.attribute.Style;
import io.luchta.forma4j.writer.engine.model.cell.style.XlsxCellStyle;
import io.luchta.forma4j.writer.engine.model.cell.style.XlsxCellStyleProperty;
import io.luchta.forma4j.writer.engine.model.column.property.NotSupportColumnProperty;
import io.luchta.forma4j.writer.engine.model.column.property.XlsxColumnProperties;
import io.luchta.forma4j.writer.engine.model.column.property.XlsxColumnProperty;
import io.luchta.forma4j.writer.engine.resolver.style.StyleErrorListener;
import io.luchta.forma4j.writer.engine.resolver.style.StyleEvaluationVisitor;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.ArrayList;
import java.util.List;

public class StyleResolver {
    public XlsxCellStyle get(Style style, VariableResolver variableResolver) {
        if (style.isEmpty()) return new XlsxCellStyle();

        StyleEvaluationVisitor.Styles styles = evaluate(style, variableResolver);

        List<XlsxCellStyleProperty> properties = new ArrayList<>();
        for (StyleEvaluationVisitor.Style s : styles) {
            properties.add(XlsxCellStyleProperty.of(s.getPropertyName(), s.getPropertyValue()));
        }
        return new XlsxCellStyle(properties);
    }

    public XlsxColumnProperties getColumnProperties(Style style, VariableResolver variableResolver) {
        if (style.isEmpty()) return new XlsxColumnProperties();

        StyleEvaluationVisitor.Styles styles = evaluate(style, variableResolver);

        List<XlsxColumnProperty> properties = new ArrayList<>();
        for (StyleEvaluationVisitor.Style s : styles) {
            XlsxColumnProperty property = XlsxColumnProperty.of(s.getPropertyName(), s.getPropertyValue());
            if (!(property instanceof NotSupportColumnProperty)) {
                properties.add(property);
            }
        }
        return new XlsxColumnProperties(properties);
    }

    private StyleEvaluationVisitor.Styles evaluate(Style style, VariableResolver variableResolver) {
        StyleLexer lexer = new StyleLexer(CharStreams.fromString(style.toString()));
        CommonTokenStream stream = new CommonTokenStream(lexer);

        StyleErrorListener errorListener = new StyleErrorListener();
        lexer.addErrorListener(errorListener);

        StyleParser parser = new StyleParser(stream);
        parser.addErrorListener(errorListener);

        ParseTree tree = parser.styles();

        StyleEvaluationVisitor visitor = new StyleEvaluationVisitor(variableResolver);
        return (StyleEvaluationVisitor.Styles) visitor.visit(tree);
    }
}
