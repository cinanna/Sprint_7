import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.example.Courier;
import org.example.CourierId;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.example.Login;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.post;
import static org.hamcrest.Matchers.equalTo;

public class CreateCourierTest {
    boolean result = true;
    Courier json = new Courier("Bucciarati", "123456", "Bruno");
    Login jsonLogin = new Login("Bucciarati", "123456");
    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
    }

    @Test
    @DisplayName("Создание курьера")
    public void createCourierTest() {
        Response response = given()
                .header("Content-Type", "application/json")
                .body(json)
                .post("/api/v1/courier");
        response.then().assertThat().body("ok", equalTo(result))
                .and()
                .statusCode(201);;
    }
    @Test
    @DisplayName("Логин курьера должен быть уникальным")
    public void cantCreateCourierTest() {
        given()
                .header("Content-Type", "application/json")
                .body(json)
                .post("/api/v1/courier");
        Response response = given()
                .header("Content-Type", "application/json")
                .body(json)
                .post("/api/v1/courier");
        response.then().assertThat().body("message", equalTo("Этот логин уже используется. Попробуйте другой."))
                .and()
                .statusCode(409);
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
