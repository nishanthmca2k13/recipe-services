package nl.abnamro.recipes.util;

import lombok.extern.slf4j.Slf4j;
import nl.abnamro.recipes.model.RecipeVO;
import org.springframework.stereotype.Component;

/**
 * Utility for Recipe validation & transformation
 */
@Slf4j
@Component
public class RecipeUtil {

    /**
     * validate the mandatory fields
     *
     * @param recipeVO
     * @return
     */
    public static Boolean checkRecipeValidity(RecipeVO recipeVO) {
        if (recipeVO == null) {
            log.error("recipe is null");
            return false;
        }
        if (recipeVO.getId() == null || recipeVO.getName() == null ||
                recipeVO.getType() == null || recipeVO.getServingCapacity() == null) {
            log.error("mandatory fields not filled");
            return false;
        }
        return true;
    }

}
