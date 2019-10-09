package exceptions.errors;

public class CustomError {

    private int statusCode;
    private String message;
    private String error;

    public CustomError(int statusCode, String message, String error) {
//        super();
        this.statusCode = statusCode;
        this.message = message;
        this.error = error;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }

    public String getError() {
        return error;
    }
}