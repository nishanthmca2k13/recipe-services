package nl.abnamro.recipes.service.impl;

import nl.abnamro.recipes.entity.RecipeEntity;
import nl.abnamro.recipes.exception.BadRequestException;
import nl.abnamro.recipes.exception.NoRecipesFoundException;
import nl.abnamro.recipes.model.Inclusion;
import nl.abnamro.recipes.model.IngredientSearchVO;
import nl.abnamro.recipes.model.IngredientVO;
import nl.abnamro.recipes.model.RecipeFilterCriteria;
import nl.abnamro.recipes.model.RecipeVO;
import nl.abnamro.recipes.repository.RecipeRepository;
import nl.abnamro.recipes.util.RecipeMapperUtil;
import nl.abnamro.recipes.util.RecipeSpecification;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class RecipeServiceImplTest {

    @Mock
    private RecipeRepository recipeRepository;

    @InjectMocks
    private RecipeServiceImpl recipeService;

    @Test
    void saveRecipeToRepositorySuccess() {
        RecipeVO recipeVO = createRecipeVO();
        RecipeEntity recipeEntity = createRecipeEntity(createRecipeVO());
        when(recipeRepository.save(recipeEntity)).thenReturn(recipeEntity);
        RecipeVO result = recipeService.saveRecipe(recipeVO);
        assertNotNull(result);
        assertEquals(101, result.getId());
    }

    @Test
    void saveRecipeToRepositoryBadRequest() {
        RecipeVO recipeVO = createRecipeVO();
        recipeVO.setServingCapacity(null);
        BadRequestException badRequestException = Assertions.assertThrows(BadRequestException.class, () -> {
            RecipeVO result = recipeService.saveRecipe(recipeVO);
        });
        assertEquals("400 BAD_REQUEST \"BAD_REQUEST_MSG\"", badRequestException.getMessage());
    }

    @Test
    void filterRecipesWithResults() {
        RecipeFilterCriteria filterCriteria = createFilterCriteria();
        List<RecipeEntity> recipeEntities = recipeEntityList();
        when(recipeRepository.findAll(any(RecipeSpecification.class))).thenReturn(recipeEntities);
        List<RecipeVO> recipeVOS = recipeService.filterRecipes(filterCriteria);
        assertEquals(3, recipeVOS.size());
    }

    @Test
    void filterRecipesWithEmptyContent() {
        RecipeFilterCriteria filterCriteria = createFilterCriteria();
        filterCriteria.getIngredientSearchVO().getIngredientVOList().get(0).setName("Cabbage");
        List<RecipeEntity> recipeEntities = new ArrayList<>();
        when(recipeRepository.findAll(any(RecipeSpecification.class))).thenReturn(recipeEntities);
        NoRecipesFoundException noRecipesFoundException = Assertions.assertThrows(NoRecipesFoundException.class,
                () -> {
            List<RecipeVO> recipeVOS = recipeService.filterRecipes(filterCriteria);
        });
        assertEquals("204 NO_CONTENT \"RECIPES_NOT_FOUND_FILTER_MSG\"",
                noRecipesFoundException.getMessage());
    }

    @Test
    void getAllRecipes() {
        List<RecipeEntity> recipeEntities = recipeEntityList();
        when(recipeRepository.findAll()).thenReturn(recipeEntities);
        List<RecipeVO> allRecipes = recipeService.getAllRecipes();
        assertEquals(3, allRecipes.size());
    }

    @Test
    void getRecipe() {
        Integer request = 101;
        RecipeEntity recipeEntity = createRecipeEntity(createRecipeVO());
        when(recipeRepository.findById(request)).thenReturn(java.util.Optional.of(recipeEntity));
        RecipeVO recipe = recipeService.getRecipe(101);
        assertEquals(101, recipe.getId());
    }

    @Test
    void modifyExistingRecipe() {
        RecipeVO request = createRecipeVO();
        request.setName("dish3");
        RecipeEntity existingRecipeEntity = createRecipeEntity(createRecipeVO());
        RecipeEntity newRecipeEntity = createRecipeEntity(request);
        when(recipeRepository.findById(request.getId())).thenReturn(java.util.Optional.of(existingRecipeEntity));
        when(recipeRepository.save(newRecipeEntity)).thenReturn(newRecipeEntity);
        RecipeVO recipe = recipeService.modifyExistingRecipe(request);
        assertEquals(101, recipe.getId());
        assertEquals("dish3", recipe.getName());
    }

    @Test
    void deleteRecipe() {
        Integer request = 101;
        RecipeEntity recipeEntity = createRecipeEntity(createRecipeVO());
        when(recipeRepository.findById(request)).thenReturn(java.util.Optional.of(recipeEntity));
        recipeService.deleteRecipe(request);
        verify(recipeRepository, times(1)).deleteById(101);
    }

    private RecipeVO createRecipeVO() {
        return RecipeVO
                .builder()
                .id(101)
                .name("dish1")
                .instructions("oven")
                .type("VEG")
                .ingredientsList(createIngredientsList())
                .servingCapacity(2)
                .build();
    }

    private List<IngredientVO> createIngredientsList() {
        return List.of(new IngredientVO("tomato"), new IngredientVO("potato"));
    }

    private RecipeEntity createRecipeEntity(RecipeVO recipeVO) {
        return RecipeMapperUtil.mapToRecipeEntity(recipeVO);
    }

    private RecipeFilterCriteria createFilterCriteria() {
        RecipeFilterCriteria recipeFilterCriteria = new RecipeFilterCriteria();
        recipeFilterCriteria.setInstructions("oven");
        recipeFilterCriteria.setType("VEG");
        recipeFilterCriteria.setServingCapacity(2);
        recipeFilterCriteria.setIngredientSearchVO(createIngredientSearchVO());
        return recipeFilterCriteria;
    }

    private IngredientSearchVO createIngredientSearchVO() {
        IngredientSearchVO ingredientSearchVO = new IngredientSearchVO();
        ingredientSearchVO.setInclusion(Inclusion.INCLUDE);
        ingredientSearchVO.setIngredientVOList(createIngredientsList());
        return ingredientSearchVO;
    }

    private List<RecipeEntity> recipeEntityList() {
        RecipeVO recipeVO1 = createRecipeVO();

        RecipeVO recipeVO2 = createRecipeVO();
        recipeVO2.setId(102);
        recipeVO2.setInstructions("normal");
        recipeVO2.setServingCapacity(5);
        recipeVO2.setType("NON-VEG");
        recipeVO2.getIngredientsList().get(0).setName("chicken");

        RecipeVO recipeVO3 = createRecipeVO();
        recipeVO3.setId(103);
        recipeVO3.setServingCapacity(2);
        recipeVO3.setType("VEG");
        recipeVO3.getIngredientsList().get(0).setName("tomato");

        return List.of(createRecipeEntity(recipeVO1), createRecipeEntity(recipeVO2), createRecipeEntity(recipeVO3));
    }

}