package io.luchta.forma4j.writer.engine.resolver;

import io.luchta.forma4j.antlr.style.StyleBaseVisitor;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StyleResolver {
    private final Map<String, ParsedStyle> parsedStyleCache = new HashMap<>();
    private final Map<String, ResolvedStyle> resolvedStyleCache = new HashMap<>();

    public XlsxCellStyle get(Style style, VariableResolver variableResolver) {
        return resolve(style, variableResolver).cellStyle();
    }

    public XlsxColumnProperties getColumnProperties(Style style, VariableResolver variableResolver) {
        return resolve(style, variableResolver).columnProperties();
    }

    public ResolvedStyle resolve(Style style, VariableResolver variableResolver) {
        if (style.isEmpty()) {
            return ResolvedStyle.empty();
        }

        String styleText = style.toString();
        ParsedStyle parsedStyle = parsedStyleCache.computeIfAbsent(styleText, this::parse);
        if (parsedStyle.isStatic()) {
            return resolvedStyleCache.computeIfAbsent(styleText, key -> evaluate(parsedStyle.tree(), variableResolver));
        }
        return evaluate(parsedStyle.tree(), variableResolver);
    }

    private ParsedStyle parse(String styleText) {
        StyleLexer lexer = new StyleLexer(CharStreams.fromString(styleText));
        CommonTokenStream stream = new CommonTokenStream(lexer);

        StyleErrorListener errorListener = new StyleErrorListener();
        lexer.addErrorListener(errorListener);

        StyleParser parser = new StyleParser(stream);
        parser.addErrorListener(errorListener);

        ParseTree tree = parser.styles();
        return new ParsedStyle(tree, !containsVariableReference(tree));
    }

    private ResolvedStyle evaluate(ParseTree tree, VariableResolver variableResolver) {
        StyleEvaluationVisitor visitor = new StyleEvaluationVisitor(variableResolver);
        StyleEvaluationVisitor.Styles styles = (StyleEvaluationVisitor.Styles) visitor.visit(tree);

        List<XlsxCellStyleProperty> cellProperties = new ArrayList<>();
        List<XlsxColumnProperty> columnProperties = new ArrayList<>();
        for (StyleEvaluationVisitor.Style style : styles) {
            cellProperties.add(XlsxCellStyleProperty.of(style.getPropertyName(), style.getPropertyValue()));

            XlsxColumnProperty columnProperty = XlsxColumnProperty.of(style.getPropertyName(), style.getPropertyValue());
            if (!(columnProperty instanceof NotSupportColumnProperty)) {
                columnProperties.add(columnProperty);
            }
        }
        return new ResolvedStyle(cellProperties, columnProperties);
    }

    private boolean containsVariableReference(ParseTree tree) {
        return Boolean.TRUE.equals(new StyleBaseVisitor<Boolean>() {
            @Override
            protected Boolean defaultResult() {
                return false;
            }

            @Override
            protected Boolean aggregateResult(Boolean aggregate, Boolean nextResult) {
                return aggregate || nextResult;
            }

            @Override
            public Boolean visitOperand(StyleParser.OperandContext ctx) {
                if (ctx.IDENTIFIER() != null) {
                    return true;
                }
                return super.visitOperand(ctx);
            }
        }.visit(tree));
    }

    private static class ParsedStyle {
        private final ParseTree tree;
        private final boolean isStatic;

        private ParsedStyle(ParseTree tree, boolean isStatic) {
            this.tree = tree;
            this.isStatic = isStatic;
        }

        private ParseTree tree() {
            return tree;
        }

        private boolean isStatic() {
            return isStatic;
        }
    }

    public static class ResolvedStyle {
        private static final ResolvedStyle EMPTY = new ResolvedStyle(new ArrayList<>(), new ArrayList<>());

        private final List<XlsxCellStyleProperty> cellProperties;
        private final List<XlsxColumnProperty> columnProperties;

        private ResolvedStyle(List<XlsxCellStyleProperty> cellProperties, List<XlsxColumnProperty> columnProperties) {
            this.cellProperties = cellProperties;
            this.columnProperties = columnProperties;
        }

        public static ResolvedStyle empty() {
            return EMPTY;
        }

        public XlsxCellStyle cellStyle() {
            return new XlsxCellStyle(new ArrayList<>(cellProperties));
        }

        public XlsxColumnProperties columnProperties() {
            return new XlsxColumnProperties(new ArrayList<>(columnProperties));
        }
    }
}
