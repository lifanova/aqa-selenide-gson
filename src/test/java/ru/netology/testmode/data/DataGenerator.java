package ru.netology.testmode.data;

import com.github.javafaker.Faker;
import com.google.gson.Gson;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static io.restassured.RestAssured.given;

public class DataGenerator {
    private static final String URL = "/api/system/users";

    private static Faker faker;

    private DataGenerator() {

    }

    private static void sendRequest(RegistrationDto user) {
        RequestSpecification requestSpec = new RequestSpecBuilder()
                .setBaseUri("http://localhost")
                .setPort(9999)
                .setAccept(ContentType.JSON)
                .setContentType(ContentType.JSON)
                .log(LogDetail.ALL)
                .build();

        Gson gson = new Gson();
        String json = gson.toJson(user);

        given()
                .spec(requestSpec)
                .body(json)
                .when()
                .post(URL)
                .then()
                .statusCode(200);
    }

    public static String getRandomLogin() {
        faker = new Faker(new Locale("en"));

        return faker.name().firstName();
    }

    public static String getRandomPassword() {
        faker = new Faker(new Locale("en"));

        return faker.internet().password();
    }


    public static class Registration {
        private Registration() {
        }

        public static RegistrationDto getUser(String status) {
            RegistrationDto user = new RegistrationDto();
            user.login = getRandomLogin();
            user.password = getRandomPassword();
            user.status = status;

            return user;
        }

        public static RegistrationDto getRegisteredUser(String status) {
            RegistrationDto registeredUser = getUser(status);
            sendRequest(registeredUser);

            return registeredUser;
        }

    }


    public static class RegistrationDto {
        private String login;
        private String password;
        private String status;

        public String getLogin() {
            return login;
        }

        public void setLogin(String login) {
            this.login = login;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
