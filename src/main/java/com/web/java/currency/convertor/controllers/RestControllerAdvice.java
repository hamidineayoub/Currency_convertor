package com.web.java.currency.convertor.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
@RequestMapping(produces = "application/json")
@ResponseBody
public class RestControllerAdvice {

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Map<String, Object>> methodNotSupported(final HttpRequestMethodNotSupportedException e) {
        Map<String, Object> errorInfo = new LinkedHashMap<>();
        errorInfo.put("timestamp", new Date());
        errorInfo.put("httpCode", HttpStatus.METHOD_NOT_ALLOWED.value());
        errorInfo.put("httpStatus", HttpStatus.METHOD_NOT_ALLOWED.getReasonPhrase());
        errorInfo.put("errorMessage", e.getMessage());
        errorInfo.put("info", "exist only one end point POST /calculate");
        return new ResponseEntity(errorInfo, HttpStatus.METHOD_NOT_ALLOWED);
    }


}
