package nl.abnamro.recipes.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@Embeddable
public class IngredientEntity implements Serializable {
    @Column(name = "name")
    private String name;
}
