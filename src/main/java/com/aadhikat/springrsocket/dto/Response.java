package com.aadhikat.springrsocket.dto;

import com.aadhikat.springrsocket.dto.error.ErrorEvent;

import java.util.Objects;

public class Response<T> {

    ErrorEvent errorResponse;
    T successResponse;

    public Response(ErrorEvent errorResponse) {
        this.errorResponse = errorResponse;
    }

    public Response(T successResponse) {
        this.successResponse = successResponse;
    }

    public Response() {
    }

    public ErrorEvent getErrorResponse() {
        return errorResponse;
    }

    public T getSuccessResponse() {
        return successResponse;
    }

    public boolean hasError() {
       return Objects.nonNull(this.errorResponse);
    }

    public static <T> Response<T> with (T t){
        return new Response<T>(t);
    }

    public static <T> Response<T> with(ErrorEvent errorEvent) {
        return new Response<T>(errorEvent);
    }
}
