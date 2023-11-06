package com.traveloka_project.traveloka.exceptionHandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.traveloka_project.traveloka.exception.NotFoundException;

@ControllerAdvice
public class GlobalHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(NotFoundException.class)
    ProblemDetail handleNotFoundException(NotFoundException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND,
                e.getMessage());
        problemDetail.setTitle("Not Found");
        problemDetail.setProperty("Timestamp", System.currentTimeMillis());
        return problemDetail;
    }

    @ExceptionHandler(Exception.class)
    ProblemDetail handleException(Exception exc) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                exc.getLocalizedMessage());
        problemDetail.setProperty("Timestamp", System.currentTimeMillis());
        return problemDetail;
    }
}
