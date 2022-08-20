package nl.abnamro.recipes.model;


import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class IngredientVO {
    @EqualsAndHashCode.Exclude
    private String name;

    @EqualsAndHashCode.Include
    private String getNameIgnoreCase() {
        return name != null ? name.toLowerCase() : null;
    }

}
