package edu.flightregistration.rest.config;

import edu.flightregistration.core.exceptions.RegistrationException;
import edu.flightregistration.rest.dto.ErrorDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestControllerAdvice
@Order
@Slf4j
public class RestConfig extends ResponseEntityExceptionHandler {

    @ExceptionHandler(RegistrationException.class)
    @ResponseStatus(BAD_REQUEST)
    public ResponseEntity<ErrorDto> handleRegistrationExceptions(Exception ex, HttpServletRequest request) {
        return ResponseEntity.status(BAD_REQUEST).body(createHttpErrorInfo(BAD_REQUEST, ex));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(BAD_REQUEST)
    public ResponseEntity<ErrorDto> handleIllegalArgumentExceptions(Exception ex, HttpServletRequest request) {
        return ResponseEntity.status(BAD_REQUEST).body(createHttpErrorInfo(BAD_REQUEST, ex));
    }

    public ErrorDto createHttpErrorInfo(HttpStatus httpStatus, Throwable ex) {
        final String message = ex.getMessage();

        log.debug("Returning HTTP status: {}, message: {}", httpStatus, message);
        return new ErrorDto(String.valueOf(httpStatus.value()), httpStatus.getReasonPhrase(), message);
    }


}
