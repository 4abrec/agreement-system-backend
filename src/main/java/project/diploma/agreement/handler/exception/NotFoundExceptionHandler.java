package project.diploma.agreement.handler.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import project.diploma.agreement.exception.NotFoundException;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class NotFoundExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ExceptionHandlerStructure> handleException(NotFoundException exception,
                                                                     HttpServletRequest webRequest) {
        return ResponseEntity
                .status(exception.getHttpStatus())
                .body(ExceptionHandlerStructure.createException(exception, webRequest.getRequestURI()));
    }
}
