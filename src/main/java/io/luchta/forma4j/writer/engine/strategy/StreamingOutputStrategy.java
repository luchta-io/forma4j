package io.luchta.forma4j.writer.engine.strategy;

import io.luchta.forma4j.writer.engine.strategy.ooxml.AbstractOoxmlOutputStrategy;

import java.io.IOException;
import java.io.OutputStream;

/**
 * セル命令を一時ファイルへ退避し、OOXMLパッケージを逐次生成する出力戦略です。
 * 列幅の自動調整は行わず、明示された列幅だけを出力します。
 */
public class StreamingOutputStrategy extends AbstractOoxmlOutputStrategy {
    public StreamingOutputStrategy() {
        super();
    }

    @Override
    protected void writePackage(OutputStream outputStream) throws IOException {
        writeBlankOoxmlPackage(outputStream);
    }
}
