package exception;

public class DataAccessException extends Exception {
    private int statusCode;

    public DataAccessException(int statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }

    public DataAccessException(String message){
        super(message);
    }

    public int getStatusCode() {
        return statusCode;
    }
}
