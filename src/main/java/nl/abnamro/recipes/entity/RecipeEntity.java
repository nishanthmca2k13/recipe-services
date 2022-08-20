package nl.abnamro.recipes.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
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
	
	@Column(name = "CREATION_DATE")
	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationDateTime;
	
	@Column(name = "SERVING_CAPACITY")
	private Integer servingCapacity;
	
	@Column(name = "INGREDIENTS", nullable=true)
	private String ingredients;
	
	@Column(name = "INSTRUCTIONS", nullable=true)
	private String instructions;
}
