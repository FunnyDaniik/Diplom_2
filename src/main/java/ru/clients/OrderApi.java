package ru.clients;

import io.restassured.response.Response;
import lombok.experimental.UtilityClass;
import ru.models.OrderRequest;

import java.util.List;

import static io.restassured.RestAssured.given;

@UtilityClass
public class OrderApi {
    private static final String ORDERS_ENDPOINT = "/api/orders";

    public static Response createOrder(String accessToken, List<String> ingredients) {
        return given()
                .header("Authorization", accessToken)
                .contentType("application/json")
                .body(new OrderRequest(ingredients))
                .when()
                .post(ORDERS_ENDPOINT);
    }

    public static Response createOrderWithoutAuth(List<String> ingredients) {
        return given()
                .contentType("application/json")
                .body(new OrderRequest(ingredients))
                .when()
                .post(ORDERS_ENDPOINT);
    }

    public static Response getUserOrders(String accessToken) {
        return given()
                .header("Authorization", accessToken)
                .when()
                .get(ORDERS_ENDPOINT);
    }
}
