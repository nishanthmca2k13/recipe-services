package nl.abnamro.recipes.controller;

import lombok.extern.slf4j.Slf4j;
import nl.abnamro.recipes.model.RecipeFilterCriteria;
import nl.abnamro.recipes.model.RecipeVO;
import nl.abnamro.recipes.service.RecipeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
public class RecipeController {

    private RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    /**
     * saves new recipe in the database
     *
     * @param recipeVO
     * @return
     */
    @PostMapping("/recipe")
    public ResponseEntity<RecipeVO> createRecipe(
            @RequestBody RecipeVO recipeVO) {
        log.info("create new recipe: {}", recipeVO);
        RecipeVO savedRecipeVO = recipeService.saveRecipeToRepository(recipeVO);
        log.info("successfully saved new recipe: {}", savedRecipeVO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedRecipeVO);
    }

    /**
     * retrives the list of recipes based on the filter criteria
     *
     * @param recipeFilterCriteria
     * @return
     */
    @PostMapping("/recipes/filter")
    public ResponseEntity<List<RecipeVO>> filterRecipes(@RequestBody RecipeFilterCriteria recipeFilterCriteria) {
        log.info("filter the recipes with the criteria: {}", recipeFilterCriteria);
        List<RecipeVO> recipeList = recipeService.filterRecipes(recipeFilterCriteria);
        log.info("filtered recipes: {} ", recipeList);
        return ResponseEntity.status(HttpStatus.OK).body(recipeList);
    }

}
