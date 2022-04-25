package io.luchta.forma4j.context.syntax;

public class SyntaxError {

    String message;
    Exception exception;

    public SyntaxError(String message) {
        this.message = message;
    }

    public SyntaxError(String message, Exception exception) {
        this.message = message;
        this.exception = exception;
    }

    public String getMessage() {
        return message;
    }

    public Exception getException() {
        return exception;
    }
}
