package com.tevinjeffrey.rutgersct.utils.exceptions;

import java.io.IOException;

public class RutgersDataIOException extends IOException {
    public RutgersDataIOException() {
    }

    public RutgersDataIOException(String detailMessage) {
        super(detailMessage);
    }

    public RutgersDataIOException(String message, Throwable cause) {
        super(message, cause);
    }

    public RutgersDataIOException(Throwable cause) {
        super(cause);
    }
}
