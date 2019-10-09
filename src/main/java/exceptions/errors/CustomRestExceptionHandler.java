package exceptions.errors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@ControllerAdvice
public class CustomRestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({ DataIntegrityViolationException.class })
    public ResponseEntity<Object> handleDataIntegrityViolationException(final DataIntegrityViolationException ex, ConflictError conflictError) {
        logger.info(ex.getClass().getName());

        final CustomError error = new CustomError(conflictError.getStatusCode(), conflictError.getMessage(), conflictError.getError());

        return new ResponseEntity<Object>(error, new HttpHeaders(), HttpStatus.CONFLICT);
    }
}
