package io.luchta.forma4j.writer.stream;

import java.io.IOException;
import java.io.InputStream;

/**
 * Writer が所有して閉じる InputStream を生成します。
 */
@FunctionalInterface
public interface InputStreamSupplier {
    InputStream open() throws IOException;
}
