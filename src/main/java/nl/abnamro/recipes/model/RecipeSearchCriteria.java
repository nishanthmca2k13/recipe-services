package nl.abnamro.recipes.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecipeSearchCriteria {
    private String type;
    private Integer servingCapacity;
    private IngredientSearchVO ingredientSearchVO;
}
