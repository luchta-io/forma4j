package io.luchta.forma4j.writer.definition;

import io.luchta.forma4j.writer.definition.schema.element.Root;

import javax.xml.bind.JAXB;
import java.io.InputStream;

public class XmlDocumentReader {

    public XmlDocument read(InputStream in) {
        // TODO おかしな要素・属性が定義されていた場合に分かりやすいログを出す
        // TODO refs. https://qiita.com/KevinFQ/items/928f6ed6d369bdeb630a#jaxb%E3%81%AE%E3%82%A2%E3%83%B3%E3%83%9E%E3%83%BC%E3%82%B7%E3%83%A3%E3%83%ABunmarshal%E3%82%92%E5%88%A9%E7%94%A8%E3%81%99%E3%82%8B%E6%96%B9%E6%B3%95
        Root root = JAXB.unmarshal(in, Root.class);
        return new XmlDocument(root);
    }
}
