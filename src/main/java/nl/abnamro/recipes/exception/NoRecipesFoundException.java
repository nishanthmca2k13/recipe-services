package nl.abnamro.recipes.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class NoRecipesFoundException extends ResponseStatusException {

	public NoRecipesFoundException(String message) {
		super(HttpStatus.NO_CONTENT, message);
	}
}
