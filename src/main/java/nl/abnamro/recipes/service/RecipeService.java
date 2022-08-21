package nl.abnamro.recipes.service;

import nl.abnamro.recipes.model.RecipeFilterCriteria;
import nl.abnamro.recipes.model.RecipeVO;

import java.util.List;

public interface RecipeService {
    RecipeVO saveRecipe(RecipeVO recipeVO);

    List<RecipeVO> filterRecipes(RecipeFilterCriteria recipeFilterCriteria);

    void deleteRecipe(Integer id);

    RecipeVO modifyExistingRecipe(RecipeVO recipeVO);

    List<RecipeVO> getAllRecipes();

    RecipeVO getRecipe(Integer id);
}
