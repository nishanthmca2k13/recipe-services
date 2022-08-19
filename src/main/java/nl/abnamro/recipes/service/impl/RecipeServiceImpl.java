package nl.abnamro.recipes.service.impl;

import lombok.extern.slf4j.Slf4j;
import nl.abnamro.recipes.entity.RecipeEntity;
import nl.abnamro.recipes.exception.BadRequestException;
import nl.abnamro.recipes.exception.NoSuchRecipeFoundException;
import nl.abnamro.recipes.exception.RecipeExistsException;
import nl.abnamro.recipes.exception.RecipeNotCreatedException;
import nl.abnamro.recipes.exception.model.ErrorMessages;
import nl.abnamro.recipes.model.RecipeSearchCriteria;
import nl.abnamro.recipes.model.RecipeVO;
import nl.abnamro.recipes.repository.RecipeRepository;
import nl.abnamro.recipes.service.RecipeService;
import nl.abnamro.recipes.util.RecipeMapperUtil;
import nl.abnamro.recipes.util.RecipeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class RecipeServiceImpl implements RecipeService {

    @Autowired
    private RecipeRepository recipeRepository;

    /**
     * save given new recipe in database
     */
    @Override
    public RecipeVO saveRecipeToRepository(RecipeVO recipeVO) {
        if (!RecipeUtil.checkRecipeValidity(recipeVO)) {
            log.error("Provided recipe instance is not valid: {}", recipeVO);
            throw new BadRequestException(ErrorMessages.BAD_REQUEST_MSG.name());
        }
        if (isRecipeFound(recipeVO)) {
            log.error("Provided recipe is not found in database: {}", recipeVO);
            throw new RecipeExistsException(ErrorMessages.RECIPE_ALREADY_EXIST_MSG.name());
        }

        RecipeEntity recipeEntity = RecipeMapperUtil.mapToRecipeEntity(recipeVO);
        RecipeVO savedRecipe = RecipeMapperUtil.mapToRecipeVO(recipeRepository.save(recipeEntity));

        if (savedRecipe == null) {
            log.error("Service failed to modify recipe in DB");
            throw new RecipeNotCreatedException(ErrorMessages.INTERNAL_SERVER_ERR_MSG.name());
        }
        return savedRecipe;

    }

    //Method to query and retrieve requested recipe based on it's id
    public RecipeVO getRecipeFromRepository(Integer id) {
        Optional<RecipeEntity> optRecipe = recipeRepository.findById(id);
        if (optRecipe.isPresent())
            return RecipeMapperUtil.mapToRecipeVO(optRecipe.get());
        else
            return null;
    }

    //Method to retrieve all recipes
    public List<RecipeVO> getAllRecipesFromRepository() {
        List<RecipeEntity> retrievedRecipes = recipeRepository.findAll();
        log.debug("Number of retrieved recipes from DB: " + retrievedRecipes.size());
        //Map all retrieved recipes entity to recipe instances
        List<RecipeVO> recipesList = new ArrayList<>(retrievedRecipes.size());
        retrievedRecipes.forEach(recipeEntity -> recipesList.add(RecipeMapperUtil.mapToRecipeVO(recipeEntity)));
        log.debug("Number of recipe entities mapped and stored to recipesList: " + recipesList.size());
        //Return mapped recipes
        return recipesList;
    }

    /**
     * modify an existing recipe
     */
    @Override
    public RecipeVO modifyExistingRecipeInRepository(RecipeVO recipeVO) {
        if (!RecipeUtil.checkRecipeValidity(recipeVO)) {
            log.error("Provided recipe instance is not valid: {}", recipeVO);
            throw new BadRequestException(ErrorMessages.BAD_REQUEST_MSG.name());
        }
        if (!isRecipeFound(recipeVO)) {
            log.error("Provided recipe is not found in database: {}", recipeVO);
            throw new NoSuchRecipeFoundException(ErrorMessages.RECIPE_NOT_FOUND_MODIFIY_MSG.name());
        }
        RecipeVO modifiedRecipe = saveRecipeToRepository(recipeVO);

        if (modifiedRecipe == null) {
            log.error("Service failed to modify recipe in DB");
            throw new RecipeNotCreatedException(ErrorMessages.INTERNAL_SERVER_ERR_MSG.name());
        }
        return modifiedRecipe;
    }

    /**
     * deleting Recipe from database
     */
    @Override
    public void deleteRecipe(Integer id) {
        try {
            recipeRepository.deleteById(id);
        } catch (EmptyResultDataAccessException ex) {
            log.error("Error while deleting recipe: {}", ex);
            throw new NoSuchRecipeFoundException(ErrorMessages.RECIPE_NOT_FOUND_DELETION_MSG.name());
        }

    }

    @Override
    public List<RecipeVO> filterRecipes(RecipeSearchCriteria recipeSearchCriteria) {

        recipeRepository.findRecipesByTypeA
    }

    private boolean isRecipeFound(RecipeVO recipeVO) {
        return recipeRepository.findById(recipeVO.getId()) == null;
    }
}
