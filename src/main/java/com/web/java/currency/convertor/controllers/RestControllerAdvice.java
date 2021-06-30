package com.web.java.currency.convertor.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
@RequestMapping(produces = "application/json")
@ResponseBody
public class RestControllerAdvice {

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<Map<String, Object>> unhandledPath(final NoHandlerFoundException e) {
        Map<String, Object> errorInfo = new LinkedHashMap<>();
        errorInfo.put("timestamp", new Date());
        errorInfo.put("httpCode", HttpStatus.NOT_FOUND.value());
        errorInfo.put("httpStatus", HttpStatus.NOT_FOUND.getReasonPhrase());
        errorInfo.put("errorMessage", e.getMessage());
        errorInfo.put("info","exist only one end point GET /languages/list");
        return new ResponseEntity(errorInfo, HttpStatus.NOT_FOUND);
    }

}
