package jp.co.actier.luchta.forma4j.reader.excel.elementreader;

import org.apache.poi.ss.usermodel.Sheet;

import jp.co.actier.luchta.forma4j.reader.config.element.FSheet;
import jp.co.actier.luchta.forma4j.reader.excel.process.ProcessInfo;
import jp.co.actier.luchta.forma4j.reader.excel.process.ProcessInfoCollection;
import jp.co.actier.luchta.forma4j.databind.json.JsonNode;
import jp.co.actier.luchta.forma4j.databind.json.JsonObject;

public class FSheetReader implements XlsxElementReader {

	private FSheet sheet;
	
	public FSheetReader(FSheet sheet) {
		this.sheet = sheet;
	}

	@Override
	public ProcessInfo read(ProcessInfo xlsxReaderProcessInformation) {
		
		Sheet worksheet = xlsxReaderProcessInformation.workbook().getSheetAt(this.sheet.getSheetIndex().intValue());
		
		ProcessInfo info = new ProcessInfo(
				xlsxReaderProcessInformation.workbook(),
				worksheet,
				this.sheet,
				this.sheet.getChildren(),
				null,
				xlsxReaderProcessInformation.currentRowNumber(),
				xlsxReaderProcessInformation.currentColNumber()
				);
		
		ProcessInfoCollection processInfoCollection = XlsxElementReaderHelper.read(info);
		
		JsonNode jsonNode = new JsonNode();
		jsonNode.putVar("index", new JsonObject(this.sheet.getSheetIndex().intValue()));
		jsonNode = XlsxElementReaderHelper.jsonNode(processInfoCollection, jsonNode);
		
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
