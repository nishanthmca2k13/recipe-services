package nl.abnamro.recipes.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IngredientSearchVO {
    private List<IngredientVO> ingredientVOList;
    private Inclusion inclusion;
}
