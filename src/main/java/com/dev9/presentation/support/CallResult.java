package com.dev9.presentation.support;

import com.google.common.base.Preconditions;

/**
 * Wraps result from underlying network call. Optionally allows services to parse exception and allow managers to decide
 */
public class CallResult<T> {

    private final T payload;

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    private int statusCode;

    private CallError error;


    CallResult(T t) {
        Preconditions.checkArgument(t != null, "Result value must not be null");
        payload = t;
    }

    CallResult(CallError error) {
        Preconditions.checkArgument(error != null, "Error must not be null");
        this.error = error;
        payload = null;
    }


    public static <T> CallResult<T> ok(T value) {
        return new CallResult<>(value);
    }

    public static <T> CallResult<T> error(CallError error) {
        Preconditions.checkArgument(error != null, "Error must not be null");
        return new CallResult<>(error);
    }

    public boolean isError() {
        return error != null;
    }


    public T get() {
        return payload;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public CallError getError() {
        return error;
    }
}
