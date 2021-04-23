package jp.co.actier.luchta.forma4j.reader.excel.elementreader;

import java.util.ArrayList;

import jp.co.actier.luchta.forma4j.reader.config.element.FVerticalFor;
import jp.co.actier.luchta.forma4j.reader.excel.process.ProcessInfo;
import jp.co.actier.luchta.forma4j.reader.excel.process.ProcessInfoCollection;
import jp.co.actier.luchta.forma4j.databind.json.JsonNode;
import jp.co.actier.luchta.forma4j.databind.json.JsonNodes;
import jp.co.actier.luchta.forma4j.databind.json.JsonObject;

public class FVerticalForReader implements XlsxElementReader {

	private FVerticalFor verticalFor;
	
	public FVerticalForReader(FVerticalFor verticalFor) {
		this.verticalFor = verticalFor;
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
					this.verticalFor,
					this.verticalFor.getChildren(),
					new JsonObject(new JsonNodes(new ArrayList<JsonNode>())),
					xlsxReaderProcessInformation.currentRowNumber().add(i),
					xlsxReaderProcessInformation.currentColNumber()
					);
			
			ProcessInfoCollection processInfoCollection = XlsxElementReaderHelper.read(info);
			JsonNode jsonNode = XlsxElementReaderHelper.jsonNode(processInfoCollection, new JsonNode());
			
			if (info.sheet().getLastRowNum() < processInfoCollection.latestRowNumber()) {
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
