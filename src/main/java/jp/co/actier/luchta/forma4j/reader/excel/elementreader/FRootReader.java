package jp.co.actier.luchta.forma4j.reader.excel.elementreader;

import jp.co.actier.luchta.forma4j.reader.config.element.FRoot;
import jp.co.actier.luchta.forma4j.reader.excel.process.ProcessInfo;
import jp.co.actier.luchta.forma4j.reader.excel.process.ProcessInfoCollection;
import jp.co.actier.luchta.forma4j.databind.json.JsonNode;
import jp.co.actier.luchta.forma4j.databind.json.JsonObject;

public class FRootReader implements XlsxElementReader {
	
	private FRoot root;
	
	public FRootReader(FRoot root) {
		this.root = root;
	}

	@Override
	public ProcessInfo read(ProcessInfo xlsxReaderProcessInformation) {
		
		ProcessInfo info = new ProcessInfo(
				xlsxReaderProcessInformation.workbook(),
				xlsxReaderProcessInformation.sheet(),
				this.root,
				this.root.getChildren(),
				null,
				xlsxReaderProcessInformation.currentRowNumber(),
				xlsxReaderProcessInformation.currentColNumber()
				);
		
		ProcessInfoCollection processInfoCollection = XlsxElementReaderHelper.read(info);
		JsonNode jsonNode = XlsxElementReaderHelper.jsonNode(processInfoCollection, new JsonNode());
		
		JsonNode resultNode = new JsonNode();
		resultNode.putVar(this.root.getName().toString(), new JsonObject(jsonNode));
		
		ProcessInfo result = new ProcessInfo(
				info.workbook(),
				info.sheet(),
				info.currentElement(),
				info.elements(),
				new JsonObject(resultNode),
				info.currentRowNumber(),
				info.currentColNumber()
				);
		
		return result;
	}
}
