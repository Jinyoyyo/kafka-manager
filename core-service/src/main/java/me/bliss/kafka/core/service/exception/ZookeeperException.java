package me.bliss.kafka.core.service.exception;

/**
 *
 *
 * @author lanjue
 * @version $Id: me.bliss.kafka.core.service.exception, v 0.1 7/8/15
 *          Exp $
 */
public class ZookeeperException extends Exception{

    public ZookeeperException() {
        super();
    }

    public ZookeeperException(String message) {
        super(message);
    }

    public ZookeeperException(String message, Throwable cause) {
        super(message, cause);
    }

    public ZookeeperException(Throwable cause) {
        super(cause);
    }

    protected ZookeeperException(String message, Throwable cause, boolean enableSuppression,
                                 boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
