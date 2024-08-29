package io.luchta.forma4j.reader.compile.parse.tagbuilder;

import io.luchta.forma4j.context.syntax.SyntaxError;
import io.luchta.forma4j.context.syntax.SyntaxErrors;

import java.util.List;

/**
 * タグクラス生成中のシンタックスエラーメッセージ追加クラス
 * @since v1.8.0
 */
public class AddBuilderSyntaxErrors {
    /**
     * エラーメッセージ追加
     * @param errorMessages
     * @param syntaxErrors
     */
    public static void run(List<String> errorMessages, SyntaxErrors syntaxErrors) {
        for (String message : errorMessages) {
            SyntaxError syntaxError = new SyntaxError(message, new UnsupportedOperationException());
            syntaxErrors.add(syntaxError);
        }
    }
}
