import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.junit4.DisplayName;

import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.clients.UserApi;
import ru.models.Credentials;

import ru.models.User;
import java.util.UUID;


import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.emptyString;

@Epic("Stellar Burgers API")
@Feature("Авторизация и регистрация пользователей")
public class AuthApiTest extends BaseApiTest {

    private User testUser;
    private String accessToken;
    private String refreshToken;

    @Before
    public void setUp() {
        super.setUp();
        testUser = User.builder()
                .email("test-user-" + UUID.randomUUID() + "@yandex.ru")
                .password("StrongPassword123!")
                .name("Test User")
                .build();
    }

    @After
    public void tearDown() {
        deleteUser(accessToken);
    }

    @Test
    @DisplayName("Успешная регистрация пользователя")
    public void testSuccessfulUserRegistration() {
        Response response = UserApi.registerUser(testUser);

        response.then()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("accessToken", not(emptyString()))
                .body("refreshToken", not(emptyString()))
                .body("user.email", equalTo(testUser.getEmail()))
                .body("user.name", equalTo(testUser.getName()));

        accessToken = response.path("accessToken");
        refreshToken = response.path("refreshToken");
    }

    @Test
    @DisplayName("Успешный вход существующего пользователя")
    public void testSuccessfulLogin() {
        // 1. Регистрация с проверкой
        Response registerResponse = UserApi.registerUser(testUser);
        registerResponse.then()
                .statusCode(200)
                .body("success", equalTo(true));

        accessToken = registerResponse.path("accessToken");

        // 2. Логин с теми же данными
        UserApi.login(new Credentials(testUser.getEmail(), testUser.getPassword()))
                .then()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("accessToken", not(emptyString()))
                .body("refreshToken", not(emptyString()))
                .body("user.email", equalTo(testUser.getEmail()))
                .body("user.name", equalTo(testUser.getName()));
    }

    @Test
    @DisplayName("Неудачный вход с неверными учетными данными")
    public void testFailedLoginWithWrongCredentials() {
        UserApi.login(new Credentials("wrong@email.com", "wrongpassword"))
                .then()
                .statusCode(401)
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
    }
}


