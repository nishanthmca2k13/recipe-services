package nl.abnamro.recipes.controller;

import lombok.extern.slf4j.Slf4j;
import nl.abnamro.recipes.exception.NoSuchRecipeFoundException;
import nl.abnamro.recipes.model.RecipeVO;
import nl.abnamro.recipes.service.RecipeService;
import nl.abnamro.recipes.util.RecipeUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
public class RecipeController {

    private RecipeService recipeService;

    @PostMapping("/recipe")
    public ResponseEntity<RecipeVO> createRecipe(
            @RequestBody RecipeVO recipe) {
        log.info("Processing the request for /api/recipe to create new recipe");
        if (RecipeUtil.checkRecipeValidity(recipe) == false) {
            log.error("Provided recipe instance is not valid, throwing Bad Request Exception");
            // throw new BadRequestException(ErrorMessages.BAD_REQUEST_MSG);
        } else if (recipeService.getRecipeFromRepository(recipe.getId()) != null) {
            log.error("Provided recipe is having duplicate Id, thowing Resource Conflict Exception");
            // throw new ResourceConflictException(ErrorMessages.RESOURCE_CONFLICT_MSG);
        } else {
            log.debug("Calling service.saveRecipeToRepository to save recipe into DB");
            RecipeVO savedRecipeVO = recipeService.saveRecipeToRepository(recipe);
            if (savedRecipeVO == null) {
                log.error("Service failed to save new recipe into DB");
                // throw new RecipeNotCreatedException(ErrorMessages.INTERNAL_SERVER_ERR_MSG);
            }

            log.info("Service successfully saved new recipe into DB with recipeId: " + savedRecipeVO.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(savedRecipeVO);
        }
        return null;
    }

    @GetMapping("/recipes")
    public ResponseEntity<List<RecipeVO>> filterRecipes() {
        log.info("Processing the request for /api/recipes to get all recipes from DB");
        log.debug("Calling service.getAllRecipes to retrieve all recipes from DB");
        List<RecipeVO> recipeList = recipeService.getAllRecipesFromRepository();
        if (recipeList.size() == 0) {
            log.error("No recipes found in DB, throwing RecipeNotFound Exception");
            // throw new NoSuchRecipeFoundException(ErrorMessages.RECIPES_NOT_FOUND_MSG);
        }

        log.info("Number of recipes retrieved from DB: " + recipeList.size());
        return ResponseEntity.status(HttpStatus.OK).body(recipeList);
    }

    @PutMapping("/recipe")
    public ResponseEntity<RecipeVO> modifyRecipe(@RequestBody RecipeVO recipeVO) {
        log.info("modify existing recipe: {}", recipeVO);
        RecipeVO modifiedRecipe = recipeService.modifyExistingRecipeInRepository(recipeVO);
        log.info("Service successfully modified existing recipe");
        return ResponseEntity.status(HttpStatus.OK).body(modifiedRecipe);
    }

    @DeleteMapping("/recipe/{id}")
    public ResponseEntity<String> deleteRecipe(@PathVariable Integer id) {
        log.info("delete recipe with id: {}", id);
        recipeService.deleteRecipe(id);
        log.info("recipe {} successfully deleted", id);
        return ResponseEntity.status(HttpStatus.OK).body("Requested recipe deleted successfully");
    }

}
