package nl.abnamro.recipes.repository;

import nl.abnamro.recipes.entity.IngredientEntity;
import nl.abnamro.recipes.entity.RecipeEntity;
import nl.abnamro.recipes.util.RecipeSpecification;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class RecipeRepositoryTest {

    @Autowired
    private RecipeRepository recipeRepository;

    @Test
    void findAllRecipesWithMatchingRecords() {
        List<RecipeEntity> result = recipeRepository
                .findAll(RecipeSpecification.builder().type("VEG").servCapacity(3).instructions("oven").build());
        assertEquals(2, result.size());
    }

    @Test
    void findAllRecipesWithMatchingRecordsIncludeIngredients() {
        IngredientEntity ingredient1 = new IngredientEntity();
        ingredient1.setName("tomato");
        IngredientEntity ingredient2 = new IngredientEntity();
        ingredient2.setName("potato");
        List<RecipeEntity> result = recipeRepository
                .findAll(RecipeSpecification.builder()
                        .type("VEG")
                        .servCapacity(3)
                        .instructions("oven")
                        .ingredientsInclude(List.of(ingredient1, ingredient2))
                        .build());
        assertEquals(1, result.size());
    }

    @Test
    void findAllRecipesWithMatchingRecordsExcludeIngredients() {
        IngredientEntity ingreTomato = new IngredientEntity();
        ingreTomato.setName("tomato");
        IngredientEntity ingregPotato = new IngredientEntity();
        ingregPotato.setName("potato");
        List<RecipeEntity> result = recipeRepository
                .findAll(RecipeSpecification.builder()
                        .type("VEG")
                        .servCapacity(3)
                        .instructions("oven")
                        .ingredientsExclude(List.of(ingregPotato))
                        .build());
        assertEquals(1, result.size());
    }

    @Test
    void findAllRecipeEmptyRecords() {
        List<RecipeEntity> result = recipeRepository
                .findAll(RecipeSpecification.builder().type("NON-VEG").servCapacity(4).instructions("oven").build());
        assertEquals(0, result.size());
    }
}