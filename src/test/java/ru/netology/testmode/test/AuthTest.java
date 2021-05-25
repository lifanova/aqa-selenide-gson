package ru.netology.testmode.test;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.netology.testmode.data.DataGenerator;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.*;
import static ru.netology.testmode.data.DataGenerator.Registration.getRegisteredUser;
import static ru.netology.testmode.data.DataGenerator.Registration.getUser;
import static ru.netology.testmode.data.DataGenerator.getRandomLogin;
import static ru.netology.testmode.data.DataGenerator.getRandomPassword;

class AuthTest {

    @Test
    //@DisplayName("Should successfully login with active registered user")
    void shouldSuccessfulLoginIfRegisteredActiveUser() {
        open("http://localhost:9999");

        DataGenerator.RegistrationDto registeredUser = getRegisteredUser("active");
        $("[data-test-id=login] input").setValue(registeredUser.getLogin());
        $("[data-test-id=password] input").setValue(registeredUser.getPassword());

        $("[data-test-id=action-login]").click();

        System.out.println("[shouldSuccessfulLoginIfRegisteredActiveUser]: " + registeredUser.getLogin() + ", " + registeredUser.getPassword());

        //find(exactText("Личный кабинет"))
        $("h2.heading").shouldBe(visible);
    }

    @Test
    //@DisplayName("Should get error message if login with not registered user")
    void shouldGetErrorIfNotRegisteredUser() {
        open("http://localhost:9999");

        // Не отправляем post-запрос на регистрацию
        DataGenerator.RegistrationDto notRegisteredUser = getUser("active");
        $("[data-test-id=login] input").setValue(notRegisteredUser.getLogin());
        $("[data-test-id=password] input").setValue(notRegisteredUser.getPassword());
        $("[data-test-id=action-login]").click();

        $("[data-test-id=error-notification]").shouldBe(visible);
    }

    @Test
    //@DisplayName("Should get error message if login with blocked registered user")
    void shouldGetErrorIfBlockedUser() {
        open("http://localhost:9999");

        DataGenerator.RegistrationDto blockedUser = getRegisteredUser("blocked");
        $("[data-test-id=login] input").setValue(blockedUser.getLogin());
        $("[data-test-id=password] input").setValue(blockedUser.getPassword());

        $("[data-test-id=action-login]").click();

        $("[data-test-id=error-notification]").shouldBe(visible);
    }

    @Test
    //@DisplayName("Should get error message if login with wrong login")
    void shouldGetErrorIfWrongLogin() {
        open("http://localhost:9999");

        DataGenerator.RegistrationDto registeredUser = getRegisteredUser("active");
        String wrongLogin = getRandomLogin();

        $("[data-test-id=login] input").setValue(wrongLogin);
        $("[data-test-id=password] input").setValue(registeredUser.getPassword());
        $("[data-test-id=action-login]").click();

        $("[data-test-id=error-notification]").shouldBe(visible);
    }

    @Test
    //@DisplayName("Should get error message if login with wrong password")
    void shouldGetErrorIfWrongPassword() {
        open("http://localhost:9999");

        DataGenerator.RegistrationDto registeredUser = getRegisteredUser("active");
        String wrongPassword = getRandomPassword();

        $("[data-test-id=login] input").setValue(registeredUser.getLogin());
        $("[data-test-id=password] input").setValue(wrongPassword);
        $("[data-test-id=action-login]").click();

        $("[data-test-id=error-notification]").shouldBe(visible);
    }
}
