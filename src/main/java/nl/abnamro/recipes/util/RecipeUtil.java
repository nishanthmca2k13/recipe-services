package nl.abnamro.recipes.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import nl.abnamro.recipes.model.IngredientVO;
import nl.abnamro.recipes.model.RecipeVO;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Util Class to contain common utility methods
 */
@Slf4j
@Component
public class RecipeUtil {

    private static String PATTERN = "dd-MM-yyyy HH:mm:ss";

    //Method to Get and return current date along with time in required format
    public static Optional<Date> getCurrentDateTime() {
        try {
            Date currentDateTime = Calendar.getInstance().getTime();
            log.debug("Current Date Time Value: " + currentDateTime.toString());
            return Optional.of(currentDateTime);
        } catch (Exception e) {
            log.error("Exception caught while getting current datetime");
            ExceptionUtils.getStackTrace(e);
            return Optional.empty();
        }
    }

    //Method to validate various fields present in given Recipe Entity
    public static Boolean checkRecipeValidity(RecipeVO recipeVO) {
        //Check for nullness
        if (recipeVO == null) {
            log.error("Given recipe instance is null");
            return false;
        } else if (recipeVO.getId() == null || recipeVO.getName() == null ||
                recipeVO.getType() == null || recipeVO.getServingCapacity() == null) {
            log.error("One of non-null field is null in recipe instance");
            return false;
        } else
            return true;
    }

    //Convert given ingredients list to JsonString and return
    public static String convertToJSONString(List<IngredientVO> ingredientVOList) {
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = null;
        try {
            jsonString = mapper.writeValueAsString(ingredientVOList);
        } catch (Exception e) {
            log.error("Exception caught while converting List to JSON String");
            log.error(ExceptionUtils.getStackTrace(e));
        }
        return jsonString;
    }

    //Convert given JSON String to List of Ingredients
    public static List<IngredientVO> convertJSONStringToIngredientVOList(String jsonString) {
        ObjectMapper mapper = new ObjectMapper();
        List<IngredientVO> ingredientVOList = null;
        //Convert JSON array to Array objects
        //Ingredient[] ingredients = mapper.readValue(jsonString, Ingredient[].class);
        try {
            //Convert JSON array to List of objects
            ingredientVOList = Arrays.asList(mapper.readValue(jsonString, IngredientVO[].class));
        } catch (Exception e) {
            log.error("Exception caught while converting JSON String to Ingredients List");
            log.error(ExceptionUtils.getStackTrace(e));
        }
        return ingredientVOList;
    }

    //Format given Date contents to
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
