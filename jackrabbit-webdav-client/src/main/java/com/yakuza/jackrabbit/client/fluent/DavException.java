package com.yakuza.jackrabbit.client.fluent;

public class DavException extends Exception {

    public DavException() {
    }

    public DavException(String message) {
        super(message);
    }

    public DavException(String message, Throwable cause) {
        super(message, cause);
    }

    public DavException(Throwable cause) {
        super(cause);
    }

    public DavException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
