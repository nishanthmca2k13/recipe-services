package nl.abnamro.recipes.util;

import lombok.extern.slf4j.Slf4j;
import nl.abnamro.recipes.entity.RecipeEntity;
import nl.abnamro.recipes.model.IngredientVO;
import nl.abnamro.recipes.model.RecipeVO;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class RecipeMapperUtil {
    //Method to map fields between Recipe and Recipe Entity
    public static RecipeEntity mapToRecipeEntity(RecipeVO recipeVO) {
        RecipeEntity rEntity = new RecipeEntity();
        //Map primitive fields
        rEntity.setId(recipeVO.getId());
        rEntity.setName(recipeVO.getName());
        rEntity.setType(recipeVO.getType());
        rEntity.setServingCapacity(recipeVO.getServingCapacity());

        //Save current date time into recipe instance, if not given
        if (recipeVO.getCreationDateTime() == null) {
            Optional<Date> currentDateTime = RecipeUtil.getCurrentDateTime();
            if (currentDateTime.isPresent())
                log.debug("Current DateTime to be set in recipe entity: " + currentDateTime.toString());
            else
                log.warn("Setting null to current date time field in recipe entity");
            rEntity.setCreationDateTime(currentDateTime.get());
        } else {
            log.debug("Retaining given creation date time value into recipe entity");
            rEntity.setCreationDateTime(recipeVO.getCreationDateTime());
        }

        //Convert ingredients list into String and set to recipe entity
        log.debug("Number of ingredients to convert to string: " + recipeVO.getIngredientsList().size());
        String ingredients = RecipeUtil.convertToJSONString(recipeVO.getIngredientsList());
        log.debug("Ingredients String: " + ingredients);
        rEntity.setIngredients(ingredients);

        rEntity.setInstructions(recipeVO.getInstructions());

        return rEntity;
    }

    //Method to map fields between Recipe and Recipe Entity
    public static RecipeVO mapToRecipeVO(RecipeEntity recipeEntity) {
        RecipeVO recipeVO = new RecipeVO();
        //Map primitive fields
        recipeVO.setId(recipeEntity.getId());
        recipeVO.setName(recipeEntity.getName());
        recipeVO.setType(recipeEntity.getType());
        recipeVO.setServingCapacity(recipeEntity.getServingCapacity());

        //Format creation date time to required format
        if (recipeEntity.getCreationDateTime() != null)
            recipeVO.setCDateTimeString(RecipeUtil.formatDateTime(recipeEntity.getCreationDateTime()));
        recipeVO.setCreationDateTime(recipeEntity.getCreationDateTime());

        //Convert ingredients string into list and set to recipe object
        log.debug("Ingredients String from DB: " + recipeEntity.getIngredients());
        List<IngredientVO> ingredientVOList = RecipeUtil.convertJSONStringToIngredientVOList(recipeEntity.getIngredients());
        log.debug("Ingredients List size after conversion: " + ingredientVOList.size());
        recipeVO.setIngredientsList(ingredientVOList);

        recipeVO.setInstructions(recipeEntity.getInstructions());

        return recipeVO;
    }
}
