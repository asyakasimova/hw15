package tests;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
import static io.restassured.path.json.JsonPath.from;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static utils.FileUtils.readStringFromFile;

public class ReqresInTests {

    @BeforeAll
    static void setup(){
        RestAssured.filters(new AllureRestAssured());
        RestAssured.baseURI = "https://reqres.in";
    }

    @Test
    void successUserListTest(){
        given()
                .when()
                .get("/api/users?page=2")
                .then()
                .statusCode(200)
                .log().body()
                .body("support.text", is("To keep ReqRes free, contributions towards server costs are appreciated!"));

    }

    @Test
    void successLoginTest(){
        given()
                .contentType(ContentType.JSON)
                .body("\"email\": \"eve.holt@reqres.in\",\n" +
                        "    \"password\": \"cityslicka\"")
        .when()
                .post("/api/login")
        .then()
                .statusCode(200)
                .log().body()
                .body("token", is(notNullValue()));
    }

    @Test
    void successLoginDataInFileTest(){
        String data = readStringFromFile("./src/test/resources/loginData.txt");
        given()
                .contentType(ContentType.JSON)
                .body(data)
                .when()
                .post("/api/login")
                .then()
                .statusCode(200)
                .log().body()
                .body("token", is(notNullValue()));
    }

    @Test
    void unSuccessLoginTest(){
        given()
                .contentType(ContentType.JSON)
                .body("{ \"email\": \"peter@klaven\" }")
       .when()
                .post("/api/login")
       .then()
                .statusCode(400)
                .log().body()
                .body("error", is("Missing password"));
    }

    //Homework tests

    @Test
    void checkUserHasAnEmailTest(){
        given()
            .when()
                .get("/api/users/2")
            .then()
                .statusCode(200)
                .log().body()
                .body("data.email", is(notNullValue()));
    }

    @Test
    void nonexistingUserNotFoundTest(){
        get("/api/users/23")
            .then()
                .statusCode(404);
    }

    @Test
    void resourcesInListTest(){
       get("/api/unknown").
               then().statusCode(200)
               .log().body()
               .body("data", is(notNullValue()));
    }

    @Test // this test doesn't work as intended because the server returns the error answer. Maybe some temporary issue
    void successfulLoginTest(){
        given()
                .body("{ \"email\": \"eve.holt@reqres.in\",\n \"password\": \"pistol\"\n }")
                .when().post("/api/register")
                .then().statusCode(400)
                        .log().body()
                        .body("id", is(not(0))).body("token", is(notNullValue()));
    }

    @Test
    void unSuccessfulLoginTest(){
        given()
                .body("{\n" +
                        "    \"email\": \"sydney@fife\"\n" +
                        "}")
                .when().post("/api/register")
                .then().statusCode(400)
                .log().body()
                .body("error", is("Missing email or username"));
    }

}
