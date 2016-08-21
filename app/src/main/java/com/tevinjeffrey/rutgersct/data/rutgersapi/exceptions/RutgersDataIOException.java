package com.tevinjeffrey.rutgersct.data.rutgersapi.exceptions;

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
