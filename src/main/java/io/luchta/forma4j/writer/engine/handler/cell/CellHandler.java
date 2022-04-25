package io.luchta.forma4j.writer.engine.handler.cell;

import java.util.ArrayList;
import java.util.List;

import io.luchta.forma4j.writer.definition.schema.element.Cell;
import io.luchta.forma4j.writer.engine.buffer.BuildBuffer;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import io.luchta.forma4j.antlr.style.StyleLexer;
import io.luchta.forma4j.antlr.style.StyleParser;
import io.luchta.forma4j.antlr.style.StyleParser.StyleContext;
import io.luchta.forma4j.antlr.style.StyleParser.StylesContext;
import io.luchta.forma4j.writer.definition.schema.attribute.Style;
import io.luchta.forma4j.writer.engine.model.cell.XlsxCell;
import io.luchta.forma4j.writer.engine.model.cell.address.XlsxCellAddress;
import io.luchta.forma4j.writer.engine.model.cell.style.XlsxBorderStyle;
import io.luchta.forma4j.writer.engine.model.cell.style.XlsxBorderType;
import io.luchta.forma4j.writer.engine.model.cell.style.XlsxFontColorStyle;
import io.luchta.forma4j.writer.engine.model.cell.style.XlsxFontColorType;
import io.luchta.forma4j.writer.engine.model.cell.style.XlsxCellStyle;
import io.luchta.forma4j.writer.engine.model.cell.style.XlsxCellStyles;
import io.luchta.forma4j.writer.engine.model.cell.value.Text;
import io.luchta.forma4j.writer.engine.model.cell.value.XlsxCellValue;

public class CellHandler {
    BuildBuffer buffer;

    public CellHandler(BuildBuffer buffer) {
        this.buffer = buffer;
    }

    public void handle(Cell cell) {
        XlsxCellAddress address = buffer.addressStack().peek();
        XlsxCell xlsxCell = new XlsxCell(address, value(cell), style(cell));
        buffer.accumulator().put(address, xlsxCell);
        buffer.addressStack().push(address.columnNumberIncrement());
    }

    private XlsxCellValue value(Cell cell) {
        return cell.text().isVariable() ?
                buffer.variableResolver().get(cell.text().stripMarker()) :
                new Text(cell.text().toString());
    }
    
    private XlsxCellStyles style(Cell cell) {
    	Style style = cell.style();
    	
    	List<XlsxCellStyle> list = new ArrayList<>();
    	if (style.isEmpty()) {
    		return new XlsxCellStyles(list);
    	}
    	
    	StyleLexer styleLexer = new StyleLexer(CharStreams.fromString(style.toString()));
    	CommonTokenStream commonTokenStream = new CommonTokenStream(styleLexer);
    	StyleParser styleParser = new StyleParser(commonTokenStream);
    	StylesContext stylesContext = styleParser.styles();
    	
    	if (stylesContext.exception != null) {
    		return new XlsxCellStyles(list);
    	}
    	
    	for (StyleContext styleContext : stylesContext.style()) {
    		if ("font-color".equals(styleContext.property().getText())) {
    			if ("black".equals(styleContext.property_value().getText())) {
    				list.add(new XlsxFontColorStyle(XlsxFontColorType.BLACK));
    			} else if ("white".equals(styleContext.property_value().getText())) {
    				list.add(new XlsxFontColorStyle(XlsxFontColorType.WHITE));
    			} else if ("red".equals(styleContext.property_value().getText())) {
    				list.add(new XlsxFontColorStyle(XlsxFontColorType.RED));
    			} else if ("blue".equals(styleContext.property_value().getText())) {
    				list.add(new XlsxFontColorStyle(XlsxFontColorType.BLUE));
    			} else if ("green".equals(styleContext.property_value().getText())) {
    				list.add(new XlsxFontColorStyle(XlsxFontColorType.GREEN));
    			} else if ("yellow".equals(styleContext.property_value().getText())) {
    				list.add(new XlsxFontColorStyle(XlsxFontColorType.YELLOW));
    			}
    		} else if("border".equals(styleContext.property().getText())) {
    			if ("none".equals(styleContext.property_value().getText())) {
    				list.add(new XlsxBorderStyle(XlsxBorderType.NONE));
    			} else if ("solid".equals(styleContext.property_value().getText())) {
    				list.add(new XlsxBorderStyle(XlsxBorderType.SOLID));
    			}
    		}
    	}
    	
    	return new XlsxCellStyles(list);
    }
}
