package nl.abnamro.recipes.exception.model;

/**
 * Text message for the errors, Todo: move this to database
 */
public enum ErrorMessages {
    RECIPES_NOT_FOUND_MSG("No recipes found for the search criteria."),
    BAD_REQUEST_MSG("Bad Request, check request parameters."),
    RECIPE_NOT_FOUND_DELETION_MSG("Unable to delete, requested recipe not found."),
    RECIPE_NOT_FOUND_MODIFIY_MSG("unable to modify, requested recipe not found."),
    INTERNAL_SERVER_ERR_MSG("Technical error, please try after sometime."),
    RECIPE_ALREADY_EXIST_MSG("Recipe with same id already present.");

    ErrorMessages(String s) {
    }
}
