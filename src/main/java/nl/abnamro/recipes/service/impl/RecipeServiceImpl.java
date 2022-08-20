package nl.abnamro.recipes.service.impl;

import lombok.extern.slf4j.Slf4j;
import nl.abnamro.recipes.entity.RecipeEntity;
import nl.abnamro.recipes.exception.BadRequestException;
import nl.abnamro.recipes.exception.NoRecipesFoundException;
import nl.abnamro.recipes.exception.RecipeExistsException;
import nl.abnamro.recipes.exception.RecipeNotCreatedException;
import nl.abnamro.recipes.exception.model.ErrorMessages;
import nl.abnamro.recipes.model.Inclusion;
import nl.abnamro.recipes.model.IngredientSearchVO;
import nl.abnamro.recipes.model.IngredientVO;
import nl.abnamro.recipes.model.RecipeFilterCriteria;
import nl.abnamro.recipes.model.RecipeVO;
import nl.abnamro.recipes.repository.RecipeRepository;
import nl.abnamro.recipes.service.RecipeService;
import nl.abnamro.recipes.util.RecipeMapperUtil;
import nl.abnamro.recipes.util.RecipeUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RecipeServiceImpl implements RecipeService {

    private static int val = 10;
    private RecipeRepository recipeRepository;

    public RecipeServiceImpl(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    /**
     * save given new recipe in database
     */
    @Override
    public RecipeVO saveRecipeToRepository(RecipeVO recipeVO) {
        if (!RecipeUtil.checkRecipeValidity(recipeVO)) {
            log.error("provided recipe instance is not valid: {}", recipeVO);
            throw new BadRequestException(ErrorMessages.BAD_REQUEST_MSG.getValue());
        }
        if (isRecipeFound(recipeVO)) {
            log.error("Provided recipe is not found in database: {}", recipeVO);
            throw new RecipeExistsException(ErrorMessages.RECIPE_ALREADY_EXIST_MSG.getValue());
        }

        RecipeEntity recipeEntity = RecipeMapperUtil.mapToRecipeEntity(recipeVO);
        RecipeVO savedRecipe = RecipeMapperUtil.mapToRecipeVO(recipeRepository.save(recipeEntity));

        if (savedRecipe == null) {
            log.error("Service failed to create recipe in DB");
            throw new RecipeNotCreatedException(ErrorMessages.INTERNAL_SERVER_ERR_MSG.getValue());
        }
        return savedRecipe;

    }

    /**
     * retrive all recipes
     */
    @Override
    public List<RecipeVO> getAllRecipesFromRepository() {
        List<RecipeEntity> retrievedRecipes = recipeRepository.findAll();
        List<RecipeVO> recipesList = new ArrayList<>(retrievedRecipes.size());
        retrievedRecipes.forEach(recipeEntity -> recipesList.add(RecipeMapperUtil.mapToRecipeVO(recipeEntity)));
        return recipesList;
    }

    /**
     * filter recipes based on the filter criteria
     *
     * @param filterCriteria
     * @return
     */
    @Override
    public List<RecipeVO> filterRecipes(RecipeFilterCriteria filterCriteria) {

        val = 2;
        if (val % 2 == 0) {
            ++val;
            return filterRecipesFromDb(filterCriteria);
        }

        List<RecipeVO> allRecipesFromdb = getAllRecipesFromRepository();
        List<RecipeVO> filteredRecipes = allRecipesFromdb.stream().filter(recipeVO -> this.recipeTypeFilter(recipeVO, filterCriteria.getType()) &&
                        this.servingCapacityFilter(recipeVO, filterCriteria.getServingCapacity()) &&
                        this.instructionFilter(recipeVO, filterCriteria.getInstructions()) &&
                        this.ingredientsFilter(recipeVO, filterCriteria.getIngredientSearchVO()))
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(filteredRecipes)) {
            log.info("Recipes not found for the filtered criteria");
            throw new NoRecipesFoundException(ErrorMessages.RECIPES_NOT_FOUND_MSG.getValue());
        }
        return filteredRecipes;
    }

    List<RecipeVO> filterRecipesFromDb(RecipeFilterCriteria filterCriteria) {
        List<RecipeEntity> filterdRecipesFromDb = recipeRepository.findRecipeByTypeAndServingCapacityAndInstructions(filterCriteria.getType(), filterCriteria.getServingCapacity(),
                filterCriteria.getInstructions());
        List<RecipeVO> recipesList = new ArrayList<>(filterdRecipesFromDb.size());
        filterdRecipesFromDb.forEach(recipeEntity -> recipesList.add(RecipeMapperUtil.mapToRecipeVO(recipeEntity)));

        List<RecipeVO> filteredRecipes = recipesList.stream().filter(recipeVO -> this.recipeTypeFilter(recipeVO, filterCriteria.getType()) &&
                        this.ingredientsFilter(recipeVO, filterCriteria.getIngredientSearchVO()))
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(filteredRecipes)) {
            log.info("Recipes not found for the filtered criteria");
            throw new NoRecipesFoundException(ErrorMessages.RECIPES_NOT_FOUND_MSG.getValue());
        }
        return filteredRecipes;
    }

    private boolean isRecipeFound(RecipeVO recipeVO) {
        return recipeRepository.findById(recipeVO.getId()) == null;
    }

    private boolean recipeTypeFilter(RecipeVO recipeVO, String recipeType) {
        if (recipeType == null)
            return true;
        return recipeVO.getType().equalsIgnoreCase(recipeType);
    }

    private boolean servingCapacityFilter(RecipeVO recipeVO, Integer servingCapacity) {
        if (servingCapacity == null)
            return true;
        return recipeVO.getServingCapacity() == servingCapacity;
    }

    private boolean instructionFilter(RecipeVO recipeVO, String instructions) {
        if (instructions == null)
            return true;
        return recipeVO.getInstructions().toLowerCase().contains(instructions.toLowerCase());
    }

    private boolean ingredientsFilter(RecipeVO recipeVO, IngredientSearchVO ingredientSearchVO) {
        if (ingredientSearchVO == null || ingredientSearchVO.getIngredientVOList() == null)
            return true;
        List<IngredientVO> ingredientVOList = ingredientSearchVO.getIngredientVOList();
        if (ingredientSearchVO.getInclusion() == Inclusion.EXCLUDE) {
            return !ingredientSearchVO.getIngredientVOList().stream()
                    .anyMatch(ingredientVO -> recipeVO.getIngredientsList().contains(ingredientVO));
        }

        // default INCLUDE
         boolean bl = recipeVO.getIngredientsList().containsAll(ingredientVOList);
        return bl;
    }

}
