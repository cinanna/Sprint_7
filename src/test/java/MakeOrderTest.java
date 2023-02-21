import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.example.OrderData;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import org.example.Color;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(Parameterized.class)
public class MakeOrderTest {
    private final String firstName;
    private final String lastName;
    private final String address;
    private final String metroStation;
    private final String phone;
    private final int rentTime;
    private final String deliveryDate;
    private final String comment;
    private final String[] color;

    static String[] black = {Color.BLACK};
    static String[] grey = {Color.GREY};
    static String[] twoColor = {Color.BLACK, Color.GREY};


    public MakeOrderTest(String firstName, String lastName, String address, String metroStation, String phone, int rentTime, String deliveryDate, String comment, String[] color) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.metroStation = metroStation;
        this.phone = phone;
        this.rentTime = rentTime;
        this.deliveryDate = deliveryDate;
        this.comment = comment;
        this.color = color;
    }

    @Parameterized.Parameters
    public static Object[][] courierData() {
        return new Object[][] {
                { "Джонотан", "Джостар", "Англия, поместье Джостаров", "3", "88005555555", 4, "01/03/2023", "Призражная кровь", black},
                { "Джозеф", "Джостар", "Америка", "2", "88005555555", 2, "01/03/2023", "Боевое стремление", grey},
                { "джотаро", "Куджо", "Япония", "1", "88005555555", 2, "01/03/2023", "Крестоносцы звездной пыли", twoColor},
                { "Джоске", "Хигашиката", "Япония, Морио", "1", "88005555555", 2, "01/03/2023", "Несокрушимый алмаз", null}
        };
    }

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
    }

    @Test
    @DisplayName("Успешное создание заказа")
    public void CreateOrderTest() {
        OrderData json = new OrderData(firstName, lastName, address, metroStation, phone, rentTime, deliveryDate, comment, color);
        Response response = given()
                .header("Content-Type", "application/json")
                .body(json)
                .post("/api/v1/orders");
        response.then().assertThat().body("track", notNullValue())
                .and()
                .statusCode(201);
    }
}
