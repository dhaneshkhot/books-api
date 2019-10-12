package errors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


public class CustomRestErrorResponseHandler extends ResponseEntityExceptionHandler {

    public static ResponseEntity<Object> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        String detailError = e.getMostSpecificCause().getMessage();
        String customError = getCustomErrorMessages(detailError);
        final CustomError ce = new CustomError(HttpStatus.CONFLICT, detailError, customError);
        return new ResponseEntity<Object>(ce, new HttpHeaders(), HttpStatus.CONFLICT);
    }

    public static ResponseEntity<Object> handleBadRequest(HttpClientErrorException.BadRequest badRequestError) {
        String error = "Bad request";
        final CustomError ce = new CustomError(badRequestError.getStatusCode(), badRequestError.getMessage(), error);

        return new ResponseEntity<Object>(ce, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    public static ResponseEntity<Object> handleNotFoundException(Long id) {
        String message = "'" + id + "' not found!";
        final CustomError ce = new CustomError(HttpStatus.NOT_FOUND, message, message);
        return new ResponseEntity<Object>(ce, new HttpHeaders(), HttpStatus.NOT_FOUND);
    }

    public static ResponseEntity<Object> handleInternalServerError(Exception e) {
        String message = "Something bad happened";
        final CustomError ce = new CustomError(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), message);
        return new ResponseEntity<Object>(ce, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private static String getCustomErrorMessages(String detailError) {
        int startIndex = detailError.indexOf("ON ")+3;
        int endIndex = detailError.indexOf(" VALUES");
        String schema = detailError.substring(startIndex, endIndex);

        String schemaName = schema.substring(0, schema.indexOf(".")-1);
        String tableName = schema.substring(schema.indexOf(".")+1, schema.indexOf("(")-1);
        String fieldName = schema.substring(schema.indexOf("(")+1,schema.indexOf(")"));

        return "'" + fieldName + "'"+" cannot be duplicate!";
    }
}
