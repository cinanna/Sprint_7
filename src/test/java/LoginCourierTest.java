import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.example.Courier;
import org.example.CourierId;
import org.example.Login;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.post;
import static org.hamcrest.Matchers.notNullValue;

public class LoginCourierTest {
    Courier json = new Courier("Bruno", "123456", "Bucciarati");
    Login jsonLogin = new Login("Bruno", "123456");
    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";

        given()
                .header("Content-Type", "application/json")
                .body(json)
                .post("/api/v1/courier");
    }

    @Test
    @DisplayName("Успешная авторизация курьера")
    public void loginCourierTest() {
        Response response = given()
                .header("Content-Type", "application/json")
                .body(jsonLogin)
                .post("/api/v1/courier/login");
        response.then().assertThat().body("id", notNullValue())
                .and()
                .statusCode(200);


    }
    @After
    public void setDown() {
        CourierId id = given()
                .header("Content-Type","application/json")
                .body(jsonLogin)
                .post("/api/v1/courier/login")
                .body().as(CourierId.class);
        given()
                .body(id)
                .delete("http://qa-scooter.praktikum-services.ru/api/v1/courier/" + id.getId());

    }
}
