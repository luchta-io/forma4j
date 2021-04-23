package jp.co.actier.luchta.forma4j.reader.excel.elementreader;

import jp.co.actier.luchta.forma4j.reader.config.element.FBody;
import jp.co.actier.luchta.forma4j.reader.excel.process.CurrentRowNumber;
import jp.co.actier.luchta.forma4j.reader.excel.process.ProcessInfo;
import jp.co.actier.luchta.forma4j.reader.excel.process.ProcessInfoCollection;
import jp.co.actier.luchta.forma4j.databind.json.JsonNode;
import jp.co.actier.luchta.forma4j.databind.json.JsonObject;

public class FBodyReader implements XlsxElementReader {

	private FBody body;
	
	public FBodyReader(FBody body) {
		this.body = body;
	}
	
	@Override
	public ProcessInfo read(ProcessInfo xlsxReaderProcessInformation) {
		
		ProcessInfo info = new ProcessInfo(
				xlsxReaderProcessInformation.workbook(),
				xlsxReaderProcessInformation.sheet(),
				this.body,
				this.body.getChildren(),
				new JsonObject(new JsonNode()),
				new CurrentRowNumber(this.body.getStartRow().intValue()),
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
