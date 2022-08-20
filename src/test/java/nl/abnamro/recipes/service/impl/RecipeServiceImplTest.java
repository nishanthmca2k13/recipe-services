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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.swing.text.html.parser.Entity;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
        RecipeVO result = recipeService.saveRecipeToRepository(recipeVO);
        assertNotNull(result);
        assertEquals(101, result.getId());
    }

    @Test
    void saveRecipeToRepositoryBadRequest() {
        RecipeVO recipeVO = createRecipeVO();
        recipeVO.setServingCapacity(null);
        BadRequestException badRequestException = Assertions.assertThrows(BadRequestException.class, () -> {
            RecipeVO result = recipeService.saveRecipeToRepository(recipeVO);
        });
        assertEquals("400 BAD_REQUEST \"Bad Request, check request parameters.\"", badRequestException.getMessage());
    }

    @Test
    void filterRecipesWithResults() {
        RecipeFilterCriteria filterCriteria = createFilterCriteria();
        List<RecipeEntity> recipeEntities = recipeEntityList();
        when(recipeRepository.findRecipeByTypeAndServingCapacityAndInstructions(filterCriteria.getType(),
                filterCriteria.getServingCapacity(), filterCriteria.getInstructions())).thenReturn(recipeEntities);
        List<RecipeVO> recipeVOS = recipeService.filterRecipes(filterCriteria);
        assertEquals(2, recipeVOS.size());
    }

    @Test
    void filterRecipesWithEmptyContent() {
        RecipeFilterCriteria filterCriteria = createFilterCriteria();
        filterCriteria.getIngredientSearchVO().getIngredientVOList().get(0).setName("Cabbage");
        List<RecipeEntity> recipeEntities = recipeEntityList();
        when(recipeRepository.findRecipeByTypeAndServingCapacityAndInstructions(filterCriteria.getType(),
                filterCriteria.getServingCapacity(), filterCriteria.getInstructions())).thenReturn(recipeEntities);
        NoRecipesFoundException noRecipesFoundException = Assertions.assertThrows(NoRecipesFoundException.class, () -> {
            List<RecipeVO> recipeVOS = recipeService.filterRecipes(filterCriteria);
        });
        assertEquals("204 NO_CONTENT \"No recipes found for the search criteria.\"", noRecipesFoundException.getMessage());
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