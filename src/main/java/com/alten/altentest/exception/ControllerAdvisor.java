package com.alten.altentest.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

import static java.lang.System.currentTimeMillis;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.ResponseEntity.status;

@ControllerAdvice
@Slf4j
public class ControllerAdvisor {

    @ExceptionHandler(ConstraintsViolationException.class)
    public ResponseEntity<StandardError> constraintsViolation(ConstraintsViolationException e, HttpServletRequest request) {
        StandardError err = new StandardError(currentTimeMillis(), BAD_REQUEST.value(), "Some constraints are violated.", e.getMessage(), request.getRequestURI());
        log.info("ConstraintsViolationException: {}", e.getMessage());
        return status(BAD_REQUEST).body(err);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<StandardError> badRequest(ConstraintsViolationException e, HttpServletRequest request) {
        StandardError err = new StandardError(currentTimeMillis(), BAD_REQUEST.value(), "Invalid request, please review it.", e.getMessage(), request.getRequestURI());
        log.info("BadRequestException: {}", e.getMessage());
        return status(BAD_REQUEST).body(err);
    }

    @ExceptionHandler(ElementNotFoundException.class)
    public ResponseEntity<StandardError> elementNotFound(ConstraintsViolationException e, HttpServletRequest request) {
        StandardError err = new StandardError(currentTimeMillis(), NOT_FOUND.value(), "Could not find resource for the request identifier.", e.getMessage(), request.getRequestURI());
        log.info("ElementNotFoundException: {}", e.getMessage());
        return status(NOT_FOUND).body(err);
    }
}
