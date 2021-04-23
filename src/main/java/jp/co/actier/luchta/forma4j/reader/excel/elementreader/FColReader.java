package jp.co.actier.luchta.forma4j.reader.excel.elementreader;

import jp.co.actier.luchta.forma4j.reader.config.element.FCol;
import jp.co.actier.luchta.forma4j.reader.excel.process.ProcessInfo;
import jp.co.actier.luchta.forma4j.databind.json.JsonObject;

public class FColReader implements XlsxElementReader {

	private FCol col;
	
	public FColReader(FCol col) {
		this.col = col;
	}
	
	@Override
	public ProcessInfo read(ProcessInfo xlsxReaderProcessInformation) {
		
		JsonObject jsonObject = XlsxElementReaderHelper.jsonObject(
				xlsxReaderProcessInformation.sheet(), 
				xlsxReaderProcessInformation.currentRowNumber().intValue(), 
				xlsxReaderProcessInformation.currentColNumber().add(this.col.getNum().intValue()).intValue()
				);
		
		ProcessInfo result = new ProcessInfo(
				xlsxReaderProcessInformation.workbook(),
				xlsxReaderProcessInformation.sheet(),
				this.col,
				this.col.getChildren(),
				jsonObject,
				xlsxReaderProcessInformation.currentRowNumber(),
				xlsxReaderProcessInformation.currentColNumber().add(this.col.getNum().intValue())
				);
		
		return result;
	}
}
