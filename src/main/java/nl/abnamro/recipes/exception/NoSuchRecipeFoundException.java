package nl.abnamro.recipes.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class NoSuchRecipeFoundException extends ResponseStatusException {

    public NoSuchRecipeFoundException(String message) {
        super(HttpStatus.NO_CONTENT, message);
    }
}
