package com.dev9.presentation.support;

/**
 * Created by Piotr Wyrwinski on 4/6/15.
 */
public class CallError {

    private Throwable throwable = null;
    private String url;
    private String developerMessage;

    public CallError(Throwable throwable, String url, String developerMessage) {
        this.throwable = throwable;
        this.url = url;
        this.developerMessage = developerMessage;
    }

    public CallError(String url, String developerMessage) {
        this.url = url;
        this.developerMessage = developerMessage;
    }

    public CallError(String developerMessage) {
        this.url = url;
        this.developerMessage = developerMessage;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public String getUrl() {
        return url;
    }

    public String getDeveloperMessage() {
        return developerMessage;
    }
}
