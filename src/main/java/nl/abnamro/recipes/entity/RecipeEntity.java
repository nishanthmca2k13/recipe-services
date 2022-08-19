package nl.abnamro.recipes.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "Recipes")
public class RecipeEntity {
	@Id
	private Integer id;
	
	@Column(name = "NAME")
	private String name;
	
	@Column(name = "TYPE")
	private String type;
	
	@Column(name = "CDATETIME", nullable=true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationDateTime;
	
	@Column(name = "CAPACITY")
	private Integer servingCapacity;
	
	@Column(name = "INGREDIENTS", nullable=true)
	private String ingredients;
	
	@Column(name = "INSTRUCTIONS", nullable=true)
	private String instructions;
}
