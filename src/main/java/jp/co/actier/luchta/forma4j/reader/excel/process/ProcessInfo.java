package jp.co.actier.luchta.forma4j.reader.excel.process;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import jp.co.actier.luchta.forma4j.reader.config.element.Element;
import jp.co.actier.luchta.forma4j.reader.config.element.Elements;
import jp.co.actier.luchta.forma4j.databind.json.JsonObject;

public class ProcessInfo {
	
	private Workbook workbook;
	private Sheet sheet;
	private Element currentElement;
	private Elements elements;
	private JsonObject jsonObject;
	private CurrentRowNumber currentRowNumber;
	private CurrentColNumber currentColNumber;
	
	public ProcessInfo(Workbook workbook, Sheet sheet, Element currentElement, Elements elements, JsonObject jsonObject,
			CurrentRowNumber currentRowNumber, CurrentColNumber currentColNumber) {
		this.workbook = workbook;
		this.sheet = sheet;
		this.currentElement = currentElement;
		this.elements = elements;
		this.jsonObject = jsonObject;
		this.currentRowNumber = currentRowNumber;
		this.currentColNumber = currentColNumber;
	}
	
	public Workbook workbook() {
		return this.workbook;
	}
	
	public Sheet sheet() {
		return this.sheet;
	}
	
	public Element currentElement() {
		return this.currentElement;
	}
	
	public Elements elements() {
		return this.elements;
	}
	
	public JsonObject jsonObject() {
		return this.jsonObject;
	}
	
	public CurrentRowNumber currentRowNumber() {
		return this.currentRowNumber;
	}
	
	public CurrentColNumber currentColNumber() {
		return this.currentColNumber;
	}
}
