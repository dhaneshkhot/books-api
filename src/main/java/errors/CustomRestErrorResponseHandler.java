package errors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


public class CustomRestErrorResponseHandler extends ResponseEntityExceptionHandler {

    /**
     * Gets DataIntegrityViolationException and returns ResponseEntity object with CustomError
     * @param e
     * @return
     */
    public static ResponseEntity<?> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        String detailError = e.getMostSpecificCause().getMessage();
        String customError = getCustomErrorMessages(detailError);
        final CustomError ce = new CustomError(HttpStatus.CONFLICT, detailError, customError);
        return new ResponseEntity<Object>(ce, new HttpHeaders(), HttpStatus.CONFLICT);
    }

    /**
     * Gets HttpClientErrorException.BadRequest and returns ResponseEntity object with CustomError
     * @param badRequestError
     * @return
     */
    public static ResponseEntity<?> handleBadRequest(HttpClientErrorException.BadRequest badRequestError) {
        String error = "Bad request";
        final CustomError ce = new CustomError(badRequestError.getStatusCode(), badRequestError.getMessage(), error);

        return new ResponseEntity<Object>(ce, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    /**
     * Gets ID that is not found and returns ResponseEntity object with 404 status code
     * @param id
     * @return
     */
    public static ResponseEntity<?> handleNotFoundException(Long id) {
        String message = "'" + id + "' not found!";
        final CustomError ce = new CustomError(HttpStatus.NOT_FOUND, message, message);
        return new ResponseEntity<Object>(ce, new HttpHeaders(), HttpStatus.NOT_FOUND);
    }

    /**
     * Gets Exception and returns ResponseEntity object with CustomError
     * @param e
     * @return
     */
    public static ResponseEntity<?> handleInternalServerError(Exception e) {
        String message = "Something bad happened";
        final CustomError ce = new CustomError(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), message);
        return new ResponseEntity<Object>(ce, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Gets detailError on DataIntegrityViolationException and returns custom error message
     * @param detailError
     * @return
     */
    private static String getCustomErrorMessages(String detailError) {
        String customError = "Unique key violation";
        if(detailError.contains("AUTHOR"))
            customError = "'AUTHOR'"+" cannot be duplicate!";
        else if(detailError.contains("TITLE"))
            customError = "'TITLE'"+" cannot be duplicate!";

        return customError;
    }
}
