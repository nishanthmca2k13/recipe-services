package nl.abnamro.recipes.repository;

import nl.abnamro.recipes.entity.RecipeEntity;
import nl.abnamro.recipes.model.Inclusion;
import nl.abnamro.recipes.model.IngredientSearchVO;
import nl.abnamro.recipes.model.RecipeSearchCriteria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public interface RecipeRepository extends JpaRepository<RecipeEntity,Integer> {
	@PersistenceContext
	private EntityManager em;
	//Method to retrieve all recipes from repository matching with given DateTime value
	List<RecipeEntity> findRecipesByCreationDateTime(Date dateTime);
	
	//Method to retrieve all recipes from repository matching with given recipe type
	List<RecipeEntity> findRecipesByType(String type);
	
	//Method to retrieve all recipes from repository matching with given serving capacity
	List<RecipeEntity> findRecipesByServingCapacity(Integer capacity);

	default List<RecipeEntity> find(RecipeSearchCriteria recipeSearchCriteria) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<RecipeEntity> cq = cb.createQuery(RecipeEntity.class);
		Root<RecipeEntity> recipeEntityRoot = cq.from(RecipeEntity.class);
		List<Predicate> predicates = new ArrayList<Predicate>();
		if (recipeSearchCriteria.getType() != null) {
			predicates.add(cb.equal(recipeEntityRoot.get("type"), "%" + recipeSearchCriteria.getType() + "%"));
		}
		// checking if parameter code is provided, if yes, adding new predicate
		if (recipeSearchCriteria.getServingCapacity() != null) {
			predicates.add(cb.equal(recipeEntityRoot.get("servingCapacity"), recipeSearchCriteria.getServingCapacity()));
		}

		IngredientSearchVO ingredientSearchVO = recipeSearchCriteria.getIngredientSearchVO();
		if (ingredientSearchVO != null && ingredientSearchVO.getIngredientVOList() != null) {
			if(ingredientSearchVO.getInclusion() == Inclusion.INCLUDE){
				predicates.add(cb.equal(recipeEntityRoot.get("servingCapacity"), ingredientSearchVO.getIngredientVOList()));
			}
			if(ingredientSearchVO.getInclusion() == Inclusion.EXCLUDE){

			}
		}

		cq.where(predicates.toArray(new Predicate[] {}));
		TypedQuery<RecipeEntity> query = em.createQuery(cq);
		List<RecipeEntity> recipeEntities = query.getResultList();
		return recipeEntities;
	}
	
}
