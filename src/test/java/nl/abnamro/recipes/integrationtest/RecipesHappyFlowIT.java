package nl.abnamro.recipes.integrationtest;

import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RecipesHappyFlowIT {
    @LocalServerPort
    Integer port;

    @Test
    public void filterRecipeseExcludeIT() {
        String request = "{" +
                " \"type\": \"VEG\", " +
                " \"servingCapacity\": 3, " +
                " \"instructions\": \"oven\", " +
                " \"ingredientSearchVO\": {" +
                " \"inclusion\": \"EXCLUDE\", " +
                "       \"ingredientVOList\": [ " +
                "           {  \"name\": \"cabbage\" }," +
                "           {  \"name\": \"tomato\" }" +
                "         ]" +
                "   }" +
                "}";
        RestAssured
                .given()
                .port(port)
                .body(request)
                .contentType("application/json")
                .when()
                .log().all(true)
                .post("/recipes/filter")
                .then()
                .log().all(true)
                .statusCode(200)
                .body("[0].id", Matchers.equalTo(101));
    }

    @Test
    public void createRecipeIT() {
        String request = "{" +
                " \"id\": 108, " +
                " \"name\": \"DISH1\", " +
                " \"type\": \"VEG\", " +
                " \"servingCapacity\": 3, " +
                " \"instructions\": \"oven\", " +
                " \"ingredientsList\": [ " +
                "     {  \"name\": \"tomato\" }," +
                "     {  \"name\": \"potato\" }" +
                "]" +
                "}";
        RestAssured
                .given()
                .port(port)
                .body(request)
                .contentType("application/json")
                .when()
                .log().all(true)
                .post("/recipe")
                .then()
                .log().all(true)
                .statusCode(201)
                .body("id", Matchers.equalTo(108))
                .body("type", Matchers.equalTo("VEG"));
    }

    @Test
    public void createRecipeMissingIdIT() {
        String request = "{" +
                " \"name\": \"DISH1\", " +
                " \"type\": \"VEG\", " +
                " \"servingCapacity\": 3, " +
                " \"instructions\": \"oven\", " +
                " \"ingredientsList\": [ " +
                "     {  \"name\": \"tomato\" }," +
                "     {  \"name\": \"potato\" }" +
                "]" +
                "}";
        RestAssured
                .given()
                .port(port)
                .body(request)
                .contentType("application/json")
                .when()
                .log().all(true)
                .post("/recipe")
                .then()
                .log().all(true)
                .statusCode(400);
    }

    @Test
    public void filterRecipesIT() {
        String request = "{" +
                " \"type\": \"VEG\", " +
                " \"servingCapacity\": 3, " +
                " \"instructions\": \"oven\", " +
                " \"ingredientSearchVO\": {" +
                " \"inclusion\": \"INCLUDE\", " +
                "       \"ingredientVOList\": [ " +
                "           {  \"name\": \"tomato\" }," +
                "           {  \"name\": \"potato\" }" +
                "         ]" +
                "   }" +
                "}";
        RestAssured
                .given()
                .port(port)
                .body(request)
                .contentType("application/json")
                .when()
                .log().all(true)
                .post("/recipes/filter")
                .then()
                .log().all(true)
                .statusCode(200)
                .body("[0].id", Matchers.equalTo(103));
    }

    @Test
    public void filterRecipesNoResultsIT() {
        String request = "{" +
                " \"type\": \"VEG\", " +
                " \"servingCapacity\": 23, " +
                " \"instructions\": \"oven\", " +
                " \"ingredientSearchVO\": {" +
                " \"inclusion\": \"INCLUDE\", " +
                "       \"ingredientVOList\": [ " +
                "           {  \"name\": \"cabbage\" }," +
                "           {  \"name\": \"potato\" }" +
                "         ]" +
                "   }" +
                "}";
        RestAssured
                .given()
                .port(port)
                .body(request)
                .contentType("application/json")
                .when()
                .log().all(true)
                .post("/recipes/filter")
                .then()
                .log().all(true)
                .statusCode(204);
    }

    @Test
    public void modifyRecipeIT() {
        String request = "{" +
                " \"id\": 101, " +
                " \"name\": \"DISH8\", " +
                " \"type\": \"VEG\", " +
                " \"servingCapacity\": 3, " +
                " \"instructions\": \"oven\", " +
                " \"ingredientsList\": [ " +
                "     {  \"name\": \"potato\" }" +
                "]" +
                "}";
        RestAssured
                .given()
                .port(port)
                .body(request)
                .contentType("application/json")
                .when()
                .log().all(true)
                .put("/recipe")
                .then()
                .log().all(true)
                .statusCode(200)
                .body("id", Matchers.equalTo(101))
                .body("name", Matchers.equalTo("DISH8"));
    }

    @Test
    public void getRecipeIT() {
        RestAssured
                .given()
                .port(port)
                .contentType("application/json")
                .when()
                .log().all(true)
                .get("/recipe/101")
                .then()
                .log().all(true)
                .statusCode(200)
                .body("id", Matchers.equalTo(101))
                .body("name", Matchers.equalTo("DISH8"));
    }

    @Test
    public void getAllRecipesIT() {
        RestAssured
                .given()
                .port(port)
                .contentType("application/json")
                .when()
                .log().all(true)
                .get("/recipes")
                .then()
                .log().all(true)
                .statusCode(200)
                .body("[0].id", Matchers.equalTo(101))
                .body("[3].id", Matchers.equalTo(104));
    }

    @Test
    public void deleteRecipeIT() {
        RestAssured
                .given()
                .port(port)
                .contentType("application/json")
                .when()
                .log().all(true)
                .delete("/recipe/104")
                .then()
                .log().all(true)
                .statusCode(200);
    }

}
