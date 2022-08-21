package nl.abnamro.recipes.service.impl;

import lombok.extern.slf4j.Slf4j;
import nl.abnamro.recipes.entity.IngredientEntity;
import nl.abnamro.recipes.entity.RecipeEntity;
import nl.abnamro.recipes.exception.BadRequestException;
import nl.abnamro.recipes.exception.NoRecipesFoundException;
import nl.abnamro.recipes.exception.NoSuchRecipeFoundException;
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
import nl.abnamro.recipes.util.RecipeSpecification;
import nl.abnamro.recipes.util.RecipeUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RecipeServiceImpl implements RecipeService {

    private RecipeRepository recipeRepository;

    public RecipeServiceImpl(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    /**
     * save given new recipe in database
     *
     * @param recipeVO
     * @return
     */
    @Override
    public RecipeVO saveRecipe(RecipeVO recipeVO) {
        if (!RecipeUtil.checkRecipeValidity(recipeVO)) {
            log.error("provided recipe instance is not valid");
            throw new BadRequestException(ErrorMessages.BAD_REQUEST_MSG.name());
        }
        if (isRecipeFound(recipeVO.getId())) {
            log.error("Provided recipe is not found in database");
            throw new RecipeExistsException(ErrorMessages.RECIPE_ALREADY_EXIST_MSG.name());
        }

        RecipeEntity recipeEntity = RecipeMapperUtil.mapToRecipeEntity(recipeVO);
        RecipeVO savedRecipe = RecipeMapperUtil.mapToRecipeVO(recipeRepository.save(recipeEntity));

        if (savedRecipe == null) {
            log.error("Service failed to create recipe in DB");
            throw new RecipeNotCreatedException(ErrorMessages.INTERNAL_SERVER_ERR_MSG.name());
        }
        return savedRecipe;

    }

    /**
     * retrive all recipes
     *
     * @return
     */
    @Override
    public List<RecipeVO> getAllRecipes() {
        List<RecipeEntity> retrievedRecipes = recipeRepository.findAll();
        List<RecipeVO> recipesList = new ArrayList<>(retrievedRecipes.size());
        retrievedRecipes.forEach(recipeEntity -> recipesList.add(RecipeMapperUtil.mapToRecipeVO(recipeEntity)));
        if (CollectionUtils.isEmpty(recipesList)) {
            log.info("Recipes not found in database");
            throw new NoRecipesFoundException(ErrorMessages.RECIPES_NOT_FOUND_MSG.name());
        }
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
        List<RecipeEntity> filteredRecipesFromDb = recipeRepository.findAll(RecipeSpecification.builder()
                .type(filterCriteria.getType())
                .servCapacity(filterCriteria.getServingCapacity())
                .instructions(filterCriteria.getInstructions())
                .ingredientsInclude(getIngredientsList(filterCriteria.getIngredientSearchVO(), Inclusion.INCLUDE))
                //.ingredientsExclude(getIngredientsList(filterCriteria.getIngredientSearchVO(), Inclusion.EXCLUDE))
                .build());
        List<RecipeVO> recipesList = new ArrayList<>(filteredRecipesFromDb.size());
        filteredRecipesFromDb.forEach(recipeEntity -> recipesList.add(RecipeMapperUtil.mapToRecipeVO(recipeEntity)));
        List<RecipeVO> filteredRecipes = recipesList.stream().filter(recipeVO ->
                        this.ingredExclusionFilter(recipeVO, filterCriteria.getIngredientSearchVO()))
                .collect(Collectors.toList());

        if (CollectionUtils.isEmpty(filteredRecipes)) {
            log.info("Recipes not found for the filtered criteria");
            throw new NoRecipesFoundException(ErrorMessages.RECIPES_NOT_FOUND_FILTER_MSG.name());
        }
        return filteredRecipes;
    }

    /**
     * @param ingredientSearchVO
     * @param inclusion
     * @return
     */
    private List<IngredientEntity> getIngredientsList(IngredientSearchVO ingredientSearchVO, Inclusion inclusion) {

        if (ingredientSearchVO == null)
            return null;
        if (ingredientSearchVO.getInclusion() == inclusion)
            return RecipeMapperUtil.mapToIngredientEntityList(ingredientSearchVO.getIngredientVOList());
        return null;
    }

    /**
     * retrieve all recipes
     *
     * @param id
     * @return
     */
    @Override
    public RecipeVO getRecipe(Integer id) {
        Optional<RecipeEntity> recipeEntityOptional = recipeRepository.findById(id);
        if (!recipeEntityOptional.isPresent())
            throw new NoSuchRecipeFoundException(ErrorMessages.RECIPES_NOT_FOUND_MSG.name());
        return RecipeMapperUtil.mapToRecipeVO(recipeEntityOptional.get());
    }


    /**
     * modify an existing recipe
     *
     * @param recipeVO
     * @return
     */
    @Override
    public RecipeVO modifyExistingRecipe(RecipeVO recipeVO) {
        if (!RecipeUtil.checkRecipeValidity(recipeVO)) {
            log.error("unable to update, provided recipe instance is not valid");
            throw new BadRequestException(ErrorMessages.BAD_REQUEST_MSG.name());
        }
        if (!isRecipeFound(recipeVO.getId())) {
            log.error("unable to update, provided recipe is not found");
            throw new NoSuchRecipeFoundException(ErrorMessages.NO_SUCH_RECIPE_FOUND_MSG.name());
        }

        RecipeEntity recipeEntity = RecipeMapperUtil.mapToRecipeEntity(recipeVO);
        RecipeVO modifiedRecipe = RecipeMapperUtil.mapToRecipeVO(recipeRepository.save(recipeEntity));

        if (modifiedRecipe == null) {
            log.error("Service failed to modify recipe");
            throw new RecipeNotCreatedException(ErrorMessages.INTERNAL_SERVER_ERR_MSG.name());
        }
        return modifiedRecipe;
    }

    /**
     * delete requested recipe based on its id
     *
     * @param id
     */
    @Override
    public void deleteRecipe(Integer id) {
        if (!isRecipeFound(id)) {
            log.error("unable to delete, provided recipe is not found");
            throw new NoSuchRecipeFoundException(ErrorMessages.NO_SUCH_RECIPE_FOUND_MSG.name());
        }
        recipeRepository.deleteById(id);
    }

    private boolean ingredExclusionFilter(RecipeVO recipeVO, IngredientSearchVO ingredientSearchVO) {
        if (ingredientSearchVO == null || ingredientSearchVO.getIngredientVOList() == null)
            return true;
        List<IngredientVO> ingredientVOList = ingredientSearchVO.getIngredientVOList();
        if (ingredientSearchVO.getInclusion() == Inclusion.EXCLUDE) {
            return !ingredientSearchVO.getIngredientVOList().stream()
                    .anyMatch(ingredientVO -> recipeVO.getIngredientsList().contains(ingredientVO));
        }

        return true;
    }

    private boolean isRecipeFound(Integer id) {
        return recipeRepository.findById(id).isPresent();
    }

}
