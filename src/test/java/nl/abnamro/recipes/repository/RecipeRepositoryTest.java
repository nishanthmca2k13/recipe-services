package nl.abnamro.recipes.repository;

import nl.abnamro.recipes.entity.RecipeEntity;
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
    void findRecipeByTypeAndServingCapacityAndInstructions() {
        List<RecipeEntity> result = recipeRepository
                .findRecipeByTypeAndServCapAndInstruc("VEG", 3, "oven");
        assertEquals(2, result.size());
    }

    @Test
    void findRecipeByTypeAndServingCapacityAndInstructionsEmptyResult() {
        List<RecipeEntity> result = recipeRepository
                .findRecipeByTypeAndServCapAndInstruc("NON-VEG", 4, "oven");
        assertEquals(0, result.size());
    }
}