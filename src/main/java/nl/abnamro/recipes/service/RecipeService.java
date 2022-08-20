package nl.abnamro.recipes.service;

import nl.abnamro.recipes.model.RecipeFilterCriteria;
import nl.abnamro.recipes.model.RecipeVO;

import java.util.List;

public interface RecipeService {
    public RecipeVO saveRecipeToRepository(RecipeVO recipeVO);

    public List<RecipeVO> getAllRecipesFromRepository();

    public List<RecipeVO> filterRecipes(RecipeFilterCriteria recipeFilterCriteria);
}
