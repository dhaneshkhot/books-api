package exceptions.errors;

public class ConflictError{
    private String error;


    public ConflictError(String detailError) {
        int startIndex = detailError.indexOf("ON ")+3;
        int endIndex = detailError.indexOf(" VALUES");
        String schema = detailError.substring(startIndex, endIndex);
        String fieldName = schema.substring(schema.indexOf("(")+1,schema.indexOf(")"));

        this.error = "'"+fieldName+"'"+" cannot be duplicate!";
    }

    public String getError() {
        return error;
    }
}
