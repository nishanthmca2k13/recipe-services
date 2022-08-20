package nl.abnamro.recipes.exception;

import lombok.extern.slf4j.Slf4j;
import nl.abnamro.recipes.exception.model.ErrorResponse;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
@Slf4j
public class RecipesRestControllerAdvice extends ResponseEntityExceptionHandler {

    /**
     * error handler for NoRecipes found
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(NoRecipesFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> handleRecipesNotFoundException(NoRecipesFoundException ex) {
        return buildErrorResponse(ex, ex.getMessage(), ex.getStatus());
    }

    /**
     * handler for data access error
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(InvalidDataAccessResourceUsageException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Object> handleDataAccessException(InvalidDataAccessResourceUsageException ex) {
        return buildErrorResponse(ex, ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }


    /**
     * handler for input error
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleBadRequestException(BadRequestException ex) {
        return buildErrorResponse(ex, ex.getMessage(), ex.getStatus());
    }

    /**
     * hanlder for saving new recipe in DB
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(RecipeNotCreatedException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Object> handleResourceNotCreatedException(RecipeNotCreatedException ex) {
        return buildErrorResponse(ex, ex.getMessage(), ex.getStatus());
    }


    /**
     * handler for global exception
     *
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Object> handleAllUncaughtException(Exception e) {
        return buildErrorResponse(e, e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Common method to build error response and return ErrorResponse Instance
    private ResponseEntity<Object> buildErrorResponse(Exception e, String message, HttpStatus status) {
        ErrorResponse errorResponse = new ErrorResponse(status.value(), message, LocalDateTime.now());
        log.error("error response: {} ", e);
        return ResponseEntity.status(status).body(errorResponse);
    }
}
