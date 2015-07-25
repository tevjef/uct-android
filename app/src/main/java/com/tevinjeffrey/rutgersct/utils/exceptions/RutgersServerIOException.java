package com.tevinjeffrey.rutgersct.utils.exceptions;

import java.io.IOException;

public class RutgersServerIOException extends IOException {
    public RutgersServerIOException() {
    }

    public RutgersServerIOException(String detailMessage) {
        super(detailMessage);
    }

    public RutgersServerIOException(String message, Throwable cause) {
        super(message, cause);
    }

    public RutgersServerIOException(Throwable cause) {
        super(cause);
    }
}
