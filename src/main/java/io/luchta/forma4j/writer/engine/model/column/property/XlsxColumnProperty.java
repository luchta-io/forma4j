package io.luchta.forma4j.writer.engine.model.column.property;

/**
 * {@code XlsxColumnStyleProperty} はカラムスタイルの基底クラスです
 */
public interface XlsxColumnProperty {
    static XlsxColumnProperty of(String name, String value) {
        switch(name) {
            case WidthProperty.NAME:
                return new WidthProperty(value);
            default:
                return new NotSupportColumnProperty(name, value);
        }
    }
}
