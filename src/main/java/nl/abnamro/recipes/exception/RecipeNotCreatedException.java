package nl.abnamro.recipes.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

//CLass to represent resource creation failure exception
public class RecipeNotCreatedException extends ResponseStatusException{

	public RecipeNotCreatedException(String message) {
		super(HttpStatus.INTERNAL_SERVER_ERROR,message);
	}

}
