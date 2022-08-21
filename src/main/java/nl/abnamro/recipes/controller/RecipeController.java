package nl.abnamro.recipes.controller;

import lombok.extern.slf4j.Slf4j;
import nl.abnamro.recipes.model.RecipeFilterCriteria;
import nl.abnamro.recipes.model.RecipeVO;
import nl.abnamro.recipes.service.RecipeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
    public ResponseEntity<RecipeVO> createRecipe(@RequestBody RecipeVO recipeVO) {
        log.info("create new recipe: {}", recipeVO);
        RecipeVO savedRecipeVO = recipeService.saveRecipe(recipeVO);
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

    /**
     * retrieve Recipe with by ID
     *
     * @param id
     * @return
     */
    @GetMapping("/recipe/{id}")
    public ResponseEntity<RecipeVO> getRecipe(@PathVariable Integer id) {
        log.info("retrieve recipe for ID: {}", id);
        RecipeVO recipeVO = recipeService.getRecipe(id);
        log.info("successfully retrieved recipe: {}", recipeVO);
        return ResponseEntity.status(HttpStatus.OK).body(recipeVO);
    }

    /**
     * retrieve all recipes
     *
     * @return
     */
    @GetMapping("/recipes")
    public ResponseEntity<List<RecipeVO>> getAllRecipes() {
        log.info("retrieve all the recipes");
        List<RecipeVO> allRecipes = recipeService.getAllRecipes();
        log.info("number of recipes retrieved: {}", allRecipes.size());
        return ResponseEntity.status(HttpStatus.OK).body(allRecipes);
    }

    /**
     * modify existing recipe
     *
     * @param recipeVO
     * @return
     */
    @PutMapping("/recipe")
    public ResponseEntity<RecipeVO> modifyRecipe(@RequestBody RecipeVO recipeVO) {
        log.info("modify existing recipe: {}", recipeVO);
        RecipeVO modifiedRecipe = recipeService.modifyExistingRecipe(recipeVO);
        log.info("successfully modified recipe: {}", modifiedRecipe);
        return ResponseEntity.status(HttpStatus.OK).body(modifiedRecipe);
    }

    /**
     * delete existing recipe
     *
     * @param id
     * @return
     */
    @DeleteMapping("/recipe/{id}")
    public ResponseEntity<String> deleteRecipe(@PathVariable Integer id) {
        log.info("delete existing recipe for ID: {}", id);
        recipeService.deleteRecipe(id);
        log.info("successfully deleted recipe with ID: {}", id);
        return ResponseEntity.status(HttpStatus.OK).body("recipe deleted from db");
    }

}
