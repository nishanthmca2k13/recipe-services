package nl.abnamro.recipes.repository;

import nl.abnamro.recipes.entity.RecipeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeRepository extends JpaRepository<RecipeEntity, Integer> {

    @Query(nativeQuery = true, value = "SELECT * FROM recipes r WHERE (:type is null or r.TYPE = :type)" +
            " AND  (:servingCapacity is null or r.SERVING_CAPACITY = :servingCapacity)" +
            " AND (:instructions is null or LOWER(r.INSTRUCTIONS) LIKE LOWER(concat('%', :instructions, '%')))")
    public List<RecipeEntity> findRecipeByTypeAndServCapAndInstruc(@Param("type") String type,
                                                                   @Param("servingCapacity") Integer servingCapacity,
                                                                   @Param("instructions") String instructions);

}
