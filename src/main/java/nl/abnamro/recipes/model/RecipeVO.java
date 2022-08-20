package nl.abnamro.recipes.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecipeVO {
    private Integer id;
    private String name;
    private String type;
    private Integer servingCapacity;
    private List<IngredientVO> ingredientsList = new ArrayList<>();
    private String instructions;
    @JsonIgnore
    private String creationDate;
}
