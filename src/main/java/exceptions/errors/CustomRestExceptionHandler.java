package exceptions.errors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@ControllerAdvice
public class CustomRestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({ DataIntegrityViolationException.class })
    public ResponseEntity<Object> handleDataIntegrityViolationException(CustomError conflictError) {

        final CustomError error = new CustomError(conflictError.getStatusCode(), conflictError.getMessage(), conflictError.getError());

        return new ResponseEntity<Object>(error, new HttpHeaders(), HttpStatus.CONFLICT);
    }


    @ExceptionHandler({ HttpClientErrorException.BadRequest.class })
    public ResponseEntity<Object> handleBadRequest(CustomError badRequestError) {

        final CustomError error = new CustomError(badRequestError.getStatusCode(), badRequestError.getMessage(), badRequestError.getError());

        return new ResponseEntity<Object>(error, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }
}
