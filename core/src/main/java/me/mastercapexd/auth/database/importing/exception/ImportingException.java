package me.mastercapexd.auth.database.importing.exception;

public class ImportingException extends RuntimeException {

    public ImportingException() {
    }

    public ImportingException(String message) {
        super(message);
    }

    public ImportingException(String message, Throwable cause) {
        super(message, cause);
    }

    public ImportingException(Throwable cause) {
        super(cause);
    }

}
