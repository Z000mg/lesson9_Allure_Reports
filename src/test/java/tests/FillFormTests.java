package tests;

import com.codeborne.selenide.Configuration;
import com.github.javafaker.Faker;
import com.github.javafaker.service.FakeValuesService;
import com.github.javafaker.service.RandomService;
import io.qameta.allure.Step;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.util.Locale;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.logevents.SelenideLogger.addListener;
import static helpers.AttachmentsHelper.*;
import static io.qameta.allure.Allure.step;

public class FillFormTests {
    File file = new File("resources/1.jpg");

    @BeforeAll
    static void setup(){
        addListener("AllureSelenide", new AllureSelenide().screenshots(true).savePageSource(true));

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("enableVNC", true);
        capabilities.setCapability("enableVideo", true);

        Configuration.browserCapabilities = capabilities;
        Configuration.remote = "https://user1:1234@" + System.getProperty("remote.browser.url", "selenoid.autotest.cloud") + ":4444/wd/hub/";

        Configuration.startMaximized = true;
    }

    @Test
    @DisplayName("Fill Form Check")
    void fillFormCheck() {

        Faker faker = new Faker();
        FakeValuesService fakeValuesService = new FakeValuesService(
                new Locale("en-GB"), new RandomService());
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();
        String userEmail = fakeValuesService.bothify("?????##@gmail.com");
        String userNumber = fakeValuesService.regexify("[0-9]{10}");
        String currentAddress = faker.address().fullAddress();

        step("Open Fill Form", () -> {
            open ("https://demoqa.com/automation-practice-form");
            $("html").shouldHave(text("Student Registration Form"));
        });

        step("Input Form", () -> {
            $("#firstName").val(firstName);
            $("#lastName").val(lastName);
            $("#userEmail").val(userEmail);
            $(byText("Male")).parent().click();
            $("#userNumber").val(userNumber);
            $("#dateOfBirthInput").click();
            $(".react-datepicker__month-select").selectOptionByValue("6");
            $(".react-datepicker__year-select").selectOptionByValue("1973");
            $(".react-datepicker__day--007").click();
            $("#subjectsInput").click();
            $("#subjectsInput").val("Physics").pressEnter();
            $(byText("Sports")).parent().click();
            $(byText("Music")).parent().click();
            $("#currentAddress").val(currentAddress);
            $("#stateCity-wrapper").$(byText("Select State")).click();
            $(byText("NCR")).click();
            $("#stateCity-wrapper").$(byText("Select City")).click();
            $(byText("Delhi")).click();
        });

        step("Upload File", () -> {
            $("#uploadPicture").uploadFile(file);
        });

        step("Submit", () -> {
            $("#submit").click();
        });

        step("Verify Filled Form", () -> {
            $("body").shouldHave(text("Thanks for submitting the form"));
            $("tbody").$(byText("Student Name")).parent().shouldHave(text(firstName + " " + lastName));
            $("tbody").$(byText("Student Email")).parent().shouldHave(text(userEmail));
            $("tbody").$(byText("Gender")).parent().shouldHave(text("Male"));
            $("tbody").$(byText("Mobile")).parent().shouldHave(text(userNumber));
            $("tbody").$(byText("Date of Birth")).parent().shouldHave(text("07 July,1973"));
            $("tbody").$(byText("Hobbies")).parent().shouldHave(text("Sports, Music"));
            $("tbody").$(byText("Picture")).parent().shouldHave(text("1.jpg"));
            $("tbody").$(byText("Address")).parent().shouldHave(text(currentAddress));
            $("tbody").$(byText("State and City")).parent().shouldHave(text("NCR Delhi"));
        });
    }

    @AfterEach
    @Step("Attachments")
    public void afterEach() {
        attachScreenshot("Last screenshot");
        attachPageSource();
        attachAsText("Browser console logs", getConsoleLogs());
        attachVideo();
        closeWebDriver();
    }
}