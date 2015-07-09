package me.bliss.kafka.core.service.exception;

/**
 *
 *
 * @author lanjue
 * @version $Id: me.bliss.kafka.core.service.exception, v 0.1 7/9/15
 *          Exp $
 */
public class JsonParseException extends Exception{

    public JsonParseException() {
        super();
    }

    public JsonParseException(String message) {
        super(message);
    }

    public JsonParseException(String message, Throwable cause) {
        super(message, cause);
    }

    public JsonParseException(Throwable cause) {
        super(cause);
    }

    protected JsonParseException(String message, Throwable cause, boolean enableSuppression,
                                 boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
