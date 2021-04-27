package io.luchta.forma4j.reader.excel.elementreader;

import io.luchta.forma4j.reader.config.element.Element;
import io.luchta.forma4j.reader.config.element.FBody;
import io.luchta.forma4j.reader.config.element.FCell;
import io.luchta.forma4j.reader.config.element.FCol;
import io.luchta.forma4j.reader.config.element.FHeader;
import io.luchta.forma4j.reader.config.element.FHorizontalFor;
import io.luchta.forma4j.reader.config.element.FRoot;
import io.luchta.forma4j.reader.config.element.FRow;
import io.luchta.forma4j.reader.config.element.FSheet;
import io.luchta.forma4j.reader.config.element.FVerticalFor;

public class XlsxElementReaderFactory {

    public static XlsxElementReader newXlsxElementReader(Element element) {
    	
        if (element instanceof FRoot) {
            return new FRootReader((FRoot) element);
        }
        if (element instanceof FSheet) {
            return new FSheetReader((FSheet) element);
        }
        if (element instanceof FHeader){
            return new FHeaderReader((FHeader) element);
        }
        if (element instanceof FBody) {
            return new FBodyReader((FBody) element);
        }
        if (element instanceof FRow) {
            return new FRowReader((FRow) element);
        }
        if (element instanceof FCol) {
            return new FColReader((FCol) element);
        }
        if (element instanceof FCell) {
            return new FCellReader((FCell) element);
        }
        if (element instanceof FVerticalFor) {
            return new FVerticalForReader((FVerticalFor) element);
        }
        if (element instanceof FHorizontalFor) {
            return new FHorizontalForReader((FHorizontalFor) element);
        }
        return null;
    }
}
