package io.luchta.forma4j.writer.engine.resolver.style;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.CommonToken;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

public class StyleErrorListener extends BaseErrorListener {
    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
        super.syntaxError(recognizer, offendingSymbol, line, charPositionInLine, msg, e);
        CommonToken token = (CommonToken) offendingSymbol;
        throw new IllegalArgumentException("style の構文に誤りがあります : 位置 " + charPositionInLine + ", " + token.getInputStream().toString());
    }
}
