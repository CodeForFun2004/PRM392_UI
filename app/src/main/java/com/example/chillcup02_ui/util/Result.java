package com.example.chillcup02_ui.util;

public class Result<T> {
    private T data;
    private String error;
    private boolean isSuccess;

    private Result(T data, String error, boolean isSuccess) {
        this.data = data;
        this.error = error;
        this.isSuccess = isSuccess;
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(data, null, true);
    }

    public static <T> Result<T> error(String error) {
        return new Result<>(null, error, false);
    }

    public T getData() {
        return data;
    }

    public String getError() {
        return error;
    }

    public boolean isSuccess() {
        return isSuccess;
    }
}
