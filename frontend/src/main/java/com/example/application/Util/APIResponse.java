package com.example.application.Util;

public class APIResponse<T> {
    private T data;
    private String errorMassage;

    public APIResponse(T data , String errorMassage){
        this.data = data;
        this.errorMassage = errorMassage;
    }

    public T getData() {
        return data;
    }

    public String getErrorMassage() {
        return errorMassage;
    }
}
