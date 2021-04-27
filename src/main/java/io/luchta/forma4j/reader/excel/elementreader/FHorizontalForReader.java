package io.luchta.forma4j.reader.excel.elementreader;

import java.util.ArrayList;

import io.luchta.forma4j.databind.json.JsonNode;
import io.luchta.forma4j.databind.json.JsonNodes;
import io.luchta.forma4j.databind.json.JsonObject;
import io.luchta.forma4j.reader.excel.process.ProcessInfo;
import io.luchta.forma4j.reader.excel.process.ProcessInfoCollection;
import org.apache.poi.ss.usermodel.Row;

import io.luchta.forma4j.reader.config.element.FHorizontalFor;

public class FHorizontalForReader implements XlsxElementReader {
	
	private FHorizontalFor horizontalFor;
	
	public FHorizontalForReader(FHorizontalFor horizontalFor) {
		this.horizontalFor = horizontalFor;
	}

	@Override
	public ProcessInfo read(ProcessInfo xlsxReaderProcessInformation) {
		
		int i = 0;
		JsonNodes jsonNodes = new JsonNodes();
		ProcessInfo info = null;
		
		do {
			
			info = new ProcessInfo(
					xlsxReaderProcessInformation.workbook(),
					xlsxReaderProcessInformation.sheet(),
					this.horizontalFor,
					this.horizontalFor.getChildren(),
					new JsonObject(new JsonNodes(new ArrayList<JsonNode>())),
					xlsxReaderProcessInformation.currentRowNumber(),
					xlsxReaderProcessInformation.currentColNumber().add(i)
					);
			
			ProcessInfoCollection processInfoCollection = XlsxElementReaderHelper.read(info);
			JsonNode jsonNode = XlsxElementReaderHelper.jsonNode(processInfoCollection, new JsonNode());
			
			Row row = info.sheet().getRow(info.currentRowNumber().intValue());
			if (row == null || row.getLastCellNum() <= processInfoCollection.latestColNumber()) {
				break;
			}
			
			jsonNodes.add(jsonNode);
			i++;
			
		} while(true);
		
		JsonObject jsonObject = new JsonObject();
		if (jsonNodes.size() > 0) {
			jsonObject = new JsonObject(jsonNodes);
		}
		
		ProcessInfo result = new ProcessInfo(
				info.workbook(),
				info.sheet(),
				info.currentElement(),
				info.elements(),
				jsonObject,
				info.currentRowNumber(),
				info.currentColNumber()
				);
		
		return result;
	}
}
