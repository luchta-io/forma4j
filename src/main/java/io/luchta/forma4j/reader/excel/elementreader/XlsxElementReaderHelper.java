package io.luchta.forma4j.reader.excel.elementreader;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import io.luchta.forma4j.databind.json.JsonNode;
import io.luchta.forma4j.databind.json.JsonNodes;
import io.luchta.forma4j.databind.json.JsonObject;
import io.luchta.forma4j.reader.excel.process.ProcessInfo;
import io.luchta.forma4j.reader.excel.process.ProcessInfoCollection;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import io.luchta.forma4j.reader.config.element.Element;

public class XlsxElementReaderHelper {
	
	public static ProcessInfoCollection read(ProcessInfo xlsxReaderProcessInformation) {
		
		ProcessInfoCollection processInfoCollection = new ProcessInfoCollection();
		for (Element element : xlsxReaderProcessInformation.elements()) {
			
			XlsxElementReader reader = XlsxElementReaderFactory.newXlsxElementReader(element);
			if (reader != null) {
				processInfoCollection.add(reader.read(xlsxReaderProcessInformation));
			}
		}
		
		return processInfoCollection;
	}
	
	public static JsonNode jsonNode(ProcessInfoCollection processInfoCollection, JsonNode jsonNode) {
		
		for (ProcessInfo processInfo : processInfoCollection) {
			if (processInfo.jsonObject().isEmpty()) {
				jsonNode.putVar(processInfo.currentElement().getName().toString(), new JsonObject());
			} else if (processInfo.jsonObject().isJsonNodes()) {
				jsonNode.putVar(processInfo.currentElement().getName().toString(), new JsonObject((JsonNodes) processInfo.jsonObject().getValue()));
			} else if (processInfo.jsonObject().isJsonNode()) {
				jsonNode.putVar(processInfo.currentElement().getName().toString(), new JsonObject((JsonNode) processInfo.jsonObject().getValue()));
			} else if (processInfo.jsonObject().isValue()) {
				jsonNode.putVar(processInfo.currentElement().getName().toString(), new JsonObject(processInfo.jsonObject().getValue().toString()));
			}
		}
		return jsonNode;
	}
	
	public static JsonObject jsonObject(Sheet sheet, int rowIndex, int columnIndex) {
		
		Row row = sheet.getRow(rowIndex);
		if (row == null) {
			return new JsonObject();
		}
		
		Cell cell = row.getCell(columnIndex);
		if (cell == null) {
			return new JsonObject("");
		}
		
		if (cell.getCellType() == CellType.NUMERIC) {
			if (DateUtil.isCellDateFormatted(cell)) {
				Date date = cell.getDateCellValue();
				Instant instant = date.toInstant();
				return new JsonObject(LocalDateTime.ofInstant(instant, ZoneId.systemDefault()));
			}
			return new JsonObject(cell.getNumericCellValue());
		}
		
		return new JsonObject(cell.toString());
	}
}
