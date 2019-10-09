package exceptions.errors;

public class ConflictError extends CustomError{

    public ConflictError(int statusCode, String message, String customError) {

        super(statusCode, message, customError);
    }

}
