package nl.abnamro.recipes.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.abnamro.recipes.model.Inclusion;
import nl.abnamro.recipes.model.IngredientSearchVO;
import nl.abnamro.recipes.model.IngredientVO;
import nl.abnamro.recipes.model.RecipeFilterCriteria;
import nl.abnamro.recipes.model.RecipeVO;
import nl.abnamro.recipes.service.RecipeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
class RecipeControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private RecipeController recipeController;

    @Mock
    private RecipeService recipeService;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(recipeController).setControllerAdvice().build();
    }

    @Test
    void createRecipeSuccess() throws Exception {
        RecipeVO recipeVO = createRecipeVO();
        ObjectMapper mapper = new ObjectMapper();
        String jsonRequest;
        jsonRequest = mapper.writeValueAsString(recipeVO);

        when(recipeService.saveRecipeToRepository(recipeVO)).thenReturn(recipeVO);
        mockMvc.perform(MockMvcRequestBuilders.post("/recipe")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated());
    }


    @Test
    void filterRecipes() throws Exception {
        RecipeFilterCriteria recipeFilterCriteria = createFilterCriteria();
        ObjectMapper mapper = new ObjectMapper();
        String jsonRequest;
        jsonRequest = mapper.writeValueAsString(recipeFilterCriteria);
        when(recipeService.filterRecipes(recipeFilterCriteria)).thenReturn(List.of(createRecipeVO()));
        mockMvc.perform(MockMvcRequestBuilders.post("/recipes/filter")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk());
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
}