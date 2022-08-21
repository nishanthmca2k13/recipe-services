package nl.abnamro.recipes.exception.model;

/**
 * Text message for the errors, Todo: move this to database
 */
public enum ErrorMessages {
    RECIPES_NOT_FOUND_FILTER_MSG("No recipes found for the search criteria."),
    RECIPES_NOT_FOUND_MSG("No recipes found."),
    BAD_REQUEST_MSG("Bad Request, check request parameters."),
    INTERNAL_SERVER_ERR_MSG("Technical error, please try after sometime."),
    RECIPE_ALREADY_EXIST_MSG("Recipe with same id already present."),
    DEFAULT_MSG("unknown error."),
    NO_SUCH_RECIPE_FOUND_MSG("No recipes found for the requested id.");

    private String value;

    ErrorMessages(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}
