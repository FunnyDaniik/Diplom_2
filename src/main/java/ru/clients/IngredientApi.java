package ru.clients;

import io.restassured.response.Response;
import lombok.experimental.UtilityClass;

import java.util.List;

import static io.restassured.RestAssured.given;

@UtilityClass
public class IngredientApi {
    private static final String INGREDIENTS_ENDPOINT = "/api/ingredients";

    public static Response getIngredients() {
        return given()
                .when()
                .get(INGREDIENTS_ENDPOINT);
    }

    public static List<String> getValidIngredientIds() {
        return getIngredients()
                .jsonPath()
                .getList("data._id");
    }
}
