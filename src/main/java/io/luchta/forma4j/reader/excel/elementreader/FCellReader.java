package io.luchta.forma4j.reader.excel.elementreader;

import io.luchta.forma4j.databind.json.JsonObject;
import io.luchta.forma4j.reader.excel.process.ProcessInfo;
import io.luchta.forma4j.reader.config.element.FCell;

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
