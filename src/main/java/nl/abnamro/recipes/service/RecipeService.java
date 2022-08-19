package nl.abnamro.recipes.service;

import nl.abnamro.recipes.model.RecipeSearchCriteria;
import nl.abnamro.recipes.model.RecipeVO;

import java.util.List;

public interface RecipeService {
    public RecipeVO saveRecipeToRepository(RecipeVO recipeVO);

    public RecipeVO getRecipeFromRepository(Integer id);

    public List<RecipeVO> getAllRecipesFromRepository();

    public RecipeVO modifyExistingRecipeInRepository(RecipeVO recipeVO);

    public void deleteRecipe(Integer id);

    public List<RecipeVO> filterRecipes(RecipeSearchCriteria recipeSearchCriteria);
}
