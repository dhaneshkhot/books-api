package errors;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.springframework.http.HttpStatus;

public class CustomError {

    private HttpStatus statusCode;
    private String detailMessage;
    private String error;

    public CustomError(){}

    public CustomError(HttpStatus statusCode, String detailMessage, String error) {
        this.statusCode = statusCode;
        this.detailMessage = detailMessage;
        this.error = error;
    }

    public HttpStatus getStatusCode() {
        return statusCode;
    }

    public String getDetailMessage() {
        return detailMessage;
    }

    public String getError() {
        return error;
    }
}
