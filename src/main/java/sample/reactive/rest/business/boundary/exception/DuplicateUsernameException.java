package sample.reactive.rest.business.boundary.exception;

public class DuplicateUsernameException extends Exception {

    public DuplicateUsernameException(String message) {
        super(message);
    }

}
