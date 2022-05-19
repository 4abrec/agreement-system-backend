package project.diploma.agreement.handler.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import project.diploma.agreement.exception.NotFoundException;

import java.util.Date;

@Data
@AllArgsConstructor
public class ExceptionHandlerStructure {

    private int status;
    private String error;
    private String message;
    private Date date;
    private String URL;

    public static ExceptionHandlerStructure createException(NotFoundException exception, String path) {
        return new ExceptionHandlerStructure(exception.getHttpStatus().value(), exception.getHttpStatus().name(),
                exception.getMessage(), new Date(System.currentTimeMillis()), path);
    }
}
