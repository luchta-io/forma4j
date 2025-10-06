package io.luchta.forma4j.writer.engine.resolver.style;

import io.luchta.forma4j.antlr.style.StyleBaseVisitor;
import io.luchta.forma4j.antlr.style.StyleParser;
import io.luchta.forma4j.writer.engine.model.cell.value.XlsxCellValue;
import io.luchta.forma4j.writer.engine.resolver.VariableResolver;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class StyleEvaluationVisitor extends StyleBaseVisitor<Object> {
    private final Styles styles = new Styles();
    private final VariableResolver variableResolver;

    public StyleEvaluationVisitor(VariableResolver variableResolver) {
        this.variableResolver = variableResolver;
    }

    @Override
    public Styles visitStyles(StyleParser.StylesContext ctx) {
        visitChildren(ctx);
        return styles;
    }

    @Override
    public Void visitStyle(StyleParser.StyleContext ctx) {
        visitChildren(ctx);
        return null;
    }

    @Override
    public Void visitProperty(StyleParser.PropertyContext ctx) {
        String propertyName = ctx.property_name().getText();
        String propertyValue = ctx.property_value().getText();
        styles.add(new Style(propertyName, propertyValue));
        return null;
    }

    @Override
    public Void visitExpression(StyleParser.ExpressionContext ctx) {
        visitChildren(ctx);
        return null;
    }

    @Override
    public Void visitIf_expression(StyleParser.If_expressionContext ctx) {
        Boolean condition = visitCondition(ctx.condition());
        if (condition) {
            visitStyles(ctx.styles(0));
        } else {
            visitStyles(ctx.styles(1));
        }
        return null;
    }

    @Override
    public Boolean visitCondition(StyleParser.ConditionContext ctx) {
        return visitBoolean_expr(ctx.boolean_expr());
    }

    @Override
    public Boolean visitBoolean_expr(StyleParser.Boolean_exprContext ctx) {
        Boolean right = visitBoolean_term(ctx.boolean_term());
        if (ctx.OR() == null) return right;
        Boolean left = visitBoolean_expr(ctx.boolean_expr());
        return left || right;
    }

    @Override
    public Boolean visitBoolean_term(StyleParser.Boolean_termContext ctx) {
        Boolean right = visitBoolean_factor(ctx.boolean_factor());
        if (ctx.AND() == null) return right;
        Boolean left = visitBoolean_term(ctx.boolean_term());
        return left && right;
    }

    @Override
    public Boolean visitBoolean_factor(StyleParser.Boolean_factorContext ctx) {
        if (ctx.comparison() == null) return visitBoolean_expr(ctx.boolean_expr());
        return visitComparison(ctx.comparison());
    }

    @Override
    public Boolean visitComparison(StyleParser.ComparisonContext ctx) {
        Object left = visitOperand(ctx.operand(0));
        Object right = visitOperand(ctx.operand(1));

        String operator = ctx.comparison_operator().getText();
        if (left instanceof BigDecimal && right instanceof BigDecimal) return numberOperation((BigDecimal) left, (BigDecimal) right, operator);

        switch (operator.toUpperCase()) {
            case "EQ": return left.equals(right);
            case "NE": return !left.equals(right);
        }
        return null;
    }

    @Override
    public Object visitOperand(StyleParser.OperandContext ctx) {
        if (ctx.IDENTIFIER() != null) {
            XlsxCellValue<?> value = variableResolver.get(ctx.IDENTIFIER().getText());
            if (value == null) return null;
            return value.toValue();
        }

        if (ctx.STRING() != null) {
            String str = ctx.STRING().getText();
            return str.substring(1, str.length() - 1);
        }

        if (ctx.NUMBER() != null) {
            return new BigDecimal(ctx.NUMBER().getText());
        }
        return null;
    }

    private Boolean numberOperation(BigDecimal left, BigDecimal right, String operator) {
        switch (operator.toUpperCase()) {
            case "EQ": return left.compareTo(right) == 0;
            case "NE": return left.compareTo(right) != 0;
            case "GE": return left.compareTo(right) >= 0;
            case "LE": return left.compareTo(right) <= 0;
            case "GT":  return left.compareTo(right) > 0;
            case "LT":  return left.compareTo(right) < 0;
        }
        return null;
    }

    public static class Styles implements Iterable<Style> {
        private final List<Style> list;

        public Styles() {
            list = new ArrayList<>();
        }

        public Styles(List<Style> list) {
            this.list = list;
        }

        public void add(Style value) {
            list.add(value);
        }

        @Override
        public Iterator<Style> iterator() {
            return list.iterator();
        }
    }

    public static class Style {
        private final String propertyName;
        private final String propertyValue;

        public Style(String propertyName, String propertyValue) {
            this.propertyName = propertyName;
            this.propertyValue = propertyValue;
        }

        public String getPropertyName() {
            return propertyName;
        }

        public String getPropertyValue() {
            return propertyValue;
        }
    }
}
