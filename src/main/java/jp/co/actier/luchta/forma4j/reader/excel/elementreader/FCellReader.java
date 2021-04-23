package jp.co.actier.luchta.forma4j.reader.excel.elementreader;

import jp.co.actier.luchta.forma4j.reader.config.element.FCell;
import jp.co.actier.luchta.forma4j.reader.excel.process.ProcessInfo;
import jp.co.actier.luchta.forma4j.databind.json.JsonObject;

public class FCellReader implements XlsxElementReader {

	private FCell cell;
	
	public FCellReader(FCell cell) {
		this.cell = cell;
	}
	
	@Override
	public ProcessInfo read(ProcessInfo xlsxReaderProcessInformation) {
		
		JsonObject jsonObject = XlsxElementReaderHelper.jsonObject(
				xlsxReaderProcessInformation.sheet(), 
				this.cell.getRow().intValue(), 
				this.cell.getCol().intValue()
				);
		
		ProcessInfo result = new ProcessInfo(
				xlsxReaderProcessInformation.workbook(),
				xlsxReaderProcessInformation.sheet(),
				this.cell,
				this.cell.getChildren(),
				jsonObject,
				xlsxReaderProcessInformation.currentRowNumber(),
				xlsxReaderProcessInformation.currentColNumber()
				);
		
		return result;
	}
}
