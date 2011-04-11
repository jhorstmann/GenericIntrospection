package net.jhorstmann.gein.introspection;

public class ReflectException extends RuntimeException {

    public ReflectException(Throwable cause) {
        super(cause);
    }

    public ReflectException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReflectException(String message) {
        super(message);
    }

}
