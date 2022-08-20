package nl.abnamro.recipes.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import nl.abnamro.recipes.entity.RecipeEntity;
import nl.abnamro.recipes.model.IngredientVO;
import nl.abnamro.recipes.model.RecipeVO;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class RecipeMapperUtil {

    private static String PATTERN = "dd-MM-yyyy HH:mm:ss";

    /**
     * Map VO to entity
     *
     * @param recipeVO
     * @return recipeEntity
     */
    public static RecipeEntity mapToRecipeEntity(RecipeVO recipeVO) {
        RecipeEntity recipeEntity = new RecipeEntity();
        recipeEntity.setId(recipeVO.getId());
        recipeEntity.setName(recipeVO.getName());
        recipeEntity.setType(recipeVO.getType());
        recipeEntity.setServingCapacity(recipeVO.getServingCapacity());
        recipeEntity.setIngredients(convertToJSONString(recipeVO.getIngredientsList()));
        recipeEntity.setInstructions(recipeVO.getInstructions());
        return recipeEntity;
    }

    /**
     * map Entity to VO
     *
     * @param recipeEntity
     * @return
     */
    public static RecipeVO mapToRecipeVO(RecipeEntity recipeEntity) {
        RecipeVO recipeVO = new RecipeVO();
        recipeVO.setId(recipeEntity.getId());
        recipeVO.setName(recipeEntity.getName());
        recipeVO.setType(recipeEntity.getType());
        recipeVO.setServingCapacity(recipeEntity.getServingCapacity());
        recipeVO.setIngredientsList(convertJSONStringToIngredientVOList(recipeEntity.getIngredients()));
        recipeVO.setInstructions(recipeEntity.getInstructions());
        recipeVO.setCreationDate(formatDateTime(recipeEntity.getCreationDateTime()));
        return recipeVO;
    }

    /**
     * transform Ingredients List to Json string
     *
     * @param ingredientVOList
     * @return
     */
    public static String convertToJSONString(List<IngredientVO> ingredientVOList) {
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = null;
        try {
            jsonString = mapper.writeValueAsString(ingredientVOList);
        } catch (Exception e) {
            log.error("error while converting List to JSON String");
            log.error(ExceptionUtils.getStackTrace(e));
        }
        return jsonString;
    }

    /**
     * transform string to Ingredients List
     *
     * @param jsonString
     * @return
     */
    public static List<IngredientVO> convertJSONStringToIngredientVOList(String jsonString) {
        ObjectMapper mapper = new ObjectMapper();
        List<IngredientVO> ingredientVOList = null;
        try {
            ingredientVOList = Arrays.asList(mapper.readValue(jsonString, IngredientVO[].class));
        } catch (Exception e) {
            log.error("error while converting JSON String to Ingredients List");
            log.error(ExceptionUtils.getStackTrace(e));
        }
        return ingredientVOList;
    }


    /**
     * date forma
     *
     * @param date
     * @return
     */
    public static String formatDateTime(Date date) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(PATTERN);
            String formattedDateTimeString = formatter.format(date);
            log.debug("Formatted DateTime: " + formattedDateTimeString);
            return formattedDateTimeString;
        } catch (Exception e) {
            log.error("Exception caught while formatting and parsing date time in " + PATTERN);
            ExceptionUtils.getStackTrace(e);
            return null;
        }
    }
}
