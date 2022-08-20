package nl.abnamro.recipes.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecipeFilterCriteria {
    private String type;
    private Integer servingCapacity;
    private IngredientSearchVO ingredientSearchVO;
    private String instructions;
}
