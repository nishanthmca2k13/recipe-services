package nl.abnamro.recipes.util;

import lombok.Builder;
import lombok.Data;
import nl.abnamro.recipes.entity.IngredientEntity;
import nl.abnamro.recipes.entity.RecipeEntity;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.isAllBlank;

@Data
@Builder
public class RecipeSpecification implements Specification<RecipeEntity> {

    private String type;
    private Integer servCapacity;
    private String instructions;
    private List<IngredientEntity> ingredientsInclude;
    private List<IngredientEntity> ingredientsExclude;

    @Override
    public Predicate toPredicate(Root<RecipeEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

        Predicate typePred = ofNullable(type)
                .map(b -> equals(cb, root.get("type"), type))
                .orElse(null);
        Predicate servCapacityPred = ofNullable(servCapacity)
                .map(h -> equals(cb, root.get("servingCapacity"), servCapacity))
                .orElse(null);
        Predicate instructionsPred = ofNullable(instructions)
                .map(h -> like(cb, root.get("instructions"), instructions))
                .orElse(null);

        List<Predicate> ingredientsPredicates = ingredientPredicates(root, cb);


        if (nonNull(ingredientsPredicates)) {
            query.distinct(true);
        }

        List<Predicate> predicates = new ArrayList<>();

        ofNullable(typePred).ifPresent(predicates::add);
        ofNullable(servCapacityPred).ifPresent(predicates::add);
        ofNullable(instructionsPred).ifPresent(predicates::add);
        ofNullable(ingredientsPredicates).ifPresent(predicates::addAll);

        return cb.and(predicates.toArray(new Predicate[predicates.size()]));
    }

    private List<Predicate> ingredientPredicates(Root<RecipeEntity> root, CriteriaBuilder cb) {
        List<Predicate> ingredientPredicates = new ArrayList<>();
        if (nonNull(ingredientsInclude)) {
            for (IngredientEntity ingredient : ingredientsInclude) {
                ofNullable(ingrInclusionPred(root, cb, ingredient.getName())).ifPresent(ingredientPredicates::add);
            }
        }

        if (nonNull(ingredientsExclude)) {
            for (IngredientEntity ingredient : ingredientsExclude) {
                ofNullable(ingrExclusionPred(root, cb, ingredient.getName())).ifPresent(ingredientPredicates::add);
            }
        }
        return ingredientPredicates;
    }

    private Predicate ingrInclusionPred(Root<RecipeEntity> root, CriteriaBuilder cb, String ingredient) {
        if (isAllBlank(ingredient)) {
            return null;
        }

        Join<RecipeEntity, IngredientEntity> titleJoin = root.join("ingredients", JoinType.INNER);

        return cb.and(like(cb, titleJoin.get("name"), ingredient));
    }

    private Predicate ingrExclusionPred(Root<RecipeEntity> root, CriteriaBuilder cb, String ingredient) {
        if (isAllBlank(ingredient)) {
            return null;
        }

        Join<RecipeEntity, IngredientEntity> titleJoin = root.join("ingredients", JoinType.INNER);

        return cb.and(notLike(cb, titleJoin.get("name"), ingredient)).not();
    }

    private Predicate equals(CriteriaBuilder cb, Path<Object> field, Object value) {
        return cb.equal(field, value);
    }

    private Predicate like(CriteriaBuilder cb, Path<String> field, String searchVal) {
        return cb.like(cb.lower(field), "%" + searchVal.toLowerCase() + "%");
    }

    private Predicate notLike(CriteriaBuilder cb, Path<String> field, String searchVal) {
        return cb.notLike(cb.lower(field), "%" + searchVal.toLowerCase() + "%");
    }

}
