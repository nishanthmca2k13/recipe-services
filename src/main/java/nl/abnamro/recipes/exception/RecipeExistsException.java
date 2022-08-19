package nl.abnamro.recipes.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class RecipeExistsException extends ResponseStatusException{
	public RecipeExistsException(String message) {
		super(HttpStatus.CONFLICT,message);
	}
}
