package nl.abnamro.recipes.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class RecipeVO {
	private Integer id;
	private String name;
	private String type;
	private Integer servingCapacity;
	private List<IngredientVO> ingredientsList = new ArrayList<>();
	private Date creationDateTime;
	private String instructions;
	private String cDateTimeString;
}
