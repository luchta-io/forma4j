package io.luchta.forma4j.writer.stream;

/**
 * {@link SequenceSource} を同一の Writer 呼び出し内で再度開けるかを表します。
 */
public enum Replayability {
    /** 一度だけ開くことができます。 */
    ONE_SHOT,
    /** {@code open()} ごとに先頭から読み直すことができます。 */
    REPLAYABLE,
    /** 初回に一時ファイルへ退避し、その内容を再生します。 */
    SPOOLED
}
