package tests;

import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.CoreMatchers.is;

public class SelenoidTests {

    @Test
    public void successStatusTest() {
        given()
                .when()
                .get("https://selenoid.autotests.cloud/status")
                .then()
                .statusCode(200);
    }

    @Test
    public void successWithoutGivenWhenStatusTest() {
        get("https://selenoid.autotests.cloud/status")
                .then()
                .statusCode(200);
    }

    @Test
    public void successStatusWithAuthTest() {
        given()
                .when()
                .get("https://user1:1234@selenoid.autotests.cloud:4444/WD/HUB/status")
                .then()
                .statusCode(200);
    }

    @Test
    public void successStatusWithBasicAuthTest() {
        given()
                .auth().basic("user1", "1234")
                .when()
                .get("https://selenoid.autotests.cloud:4444/WD/HUB/status")
                .then()
                .statusCode(200);
    }
    @Test
    public void successStatusResponseTest() {
        Response response = given()
                .auth().basic("user1", "1234")
                .when()
                .get("https://selenoid.autotests.cloud:4444/WD/HUB/status")
                .then()
                .statusCode(200)
                .extract().response();
        System.out.println(response.asString());
    }

    @Test
    public void successStatusResponseWithLogTest() {
        given()
                .auth().basic("user1", "1234")
                .when()
                .get("https://selenoid.autotests.cloud:4444/WD/HUB/status")
                .then()
                .log().body()
                .statusCode(200)
                .extract().response();

    }

    @Test
    public void successStatusReadyTest() {
        given()
                .auth().basic("user1", "1234")
                .when()
                .get("https://selenoid.autotests.cloud:4444/WD/HUB/status")
                .then()
                .log().body()
                .statusCode(200)
                .body("value.ready", is(true));
    }
}
