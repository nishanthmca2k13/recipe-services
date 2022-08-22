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
import java.util.stream.Collectors;

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
            ingredientPredicates.addAll(ingredientsInclude
                    .stream()
                    .map(ingredient -> ingrInclusionPred(root, cb, ingredient.getName())).collect(Collectors.toList()));
        }

        if (nonNull(ingredientsExclude)) {
            ingredientPredicates.addAll(ingredientsExclude
                    .stream()
                    .map(ingredient -> ingrExclusionPred(root, cb, ingredient.getName())).collect(Collectors.toList()));

        }
        return ingredientPredicates;
    }

    private Predicate ingrInclusionPred(Root<RecipeEntity> root, CriteriaBuilder cb, String ingredient) {
        if (isAllBlank(ingredient)) {
            return null;
        }
        Join<RecipeEntity, IngredientEntity> ingredientsJoin = root.join("ingredients", JoinType.INNER);
        return cb.and(in(cb, ingredientsJoin.get("name"), ingredient));
    }

    private Predicate ingrExclusionPred(Root<RecipeEntity> root, CriteriaBuilder cb, String ingredient) {
        if (isAllBlank(ingredient)) {
            return null;
        }
        Join<RecipeEntity, IngredientEntity> ingredientsJoin = root.join("ingredients", JoinType.INNER);
        return cb.or(in(cb, ingredientsJoin.get("name"), ingredient)).not();
    }

    private Predicate in(CriteriaBuilder cb, Path<Object> field, String value) {
        return cb.in(field).value(value);
    }

    private Predicate equals(CriteriaBuilder cb, Path<Object> field, Object value) {
        return cb.equal(field, value);
    }

    private Predicate like(CriteriaBuilder cb, Path<String> field, String searchVal) {
        return cb.like(cb.lower(field), "%" + searchVal.toLowerCase() + "%");
    }

}
