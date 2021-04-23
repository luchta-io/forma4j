package jp.co.actier.luchta.forma4j.reader.excel.elementreader;

import jp.co.actier.luchta.forma4j.reader.config.element.FHeader;
import jp.co.actier.luchta.forma4j.reader.excel.process.ProcessInfo;
import jp.co.actier.luchta.forma4j.reader.excel.process.ProcessInfoCollection;
import jp.co.actier.luchta.forma4j.databind.json.JsonNode;
import jp.co.actier.luchta.forma4j.databind.json.JsonObject;

public class FHeaderReader implements XlsxElementReader {

	private FHeader header;
	
	public FHeaderReader(FHeader header) {
		this.header = header;
	}
	
	@Override
	public ProcessInfo read(ProcessInfo xlsxReaderProcessInformation) {
		
		ProcessInfo info = new ProcessInfo(
				xlsxReaderProcessInformation.workbook(),
				xlsxReaderProcessInformation.sheet(),
				this.header,
				this.header.getChildren(),
				null,
				xlsxReaderProcessInformation.currentRowNumber(),
				xlsxReaderProcessInformation.currentColNumber()
				);
		
		ProcessInfoCollection processInfoCollection = XlsxElementReaderHelper.read(info);
		JsonNode jsonNode = XlsxElementReaderHelper.jsonNode(processInfoCollection, new JsonNode());
		
		JsonObject jsonObject = new JsonObject();
		if (jsonNode.size() > 0) {
			jsonObject = new JsonObject(jsonNode);
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
