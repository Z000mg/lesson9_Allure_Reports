import com.codeborne.selenide.Configuration;
import com.github.javafaker.Faker;
import com.github.javafaker.service.FakeValuesService;
import com.github.javafaker.service.RandomService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Locale;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selenide.*;
import static io.qameta.allure.Allure.step;

public class FillFormTests {
    File file = new File("src/test/resources/1.jpg");

    @BeforeAll
    static void setup(){
        Configuration.startMaximized = true;
    }

    @Test
    void lesson2Form() {

        Faker faker = new Faker();
        FakeValuesService fakeValuesService = new FakeValuesService(
                new Locale("en-GB"), new RandomService());
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();
        String userEmail = fakeValuesService.bothify("?????##@gmail.com");
        String userNumber = fakeValuesService.regexify("[0-9]{10}");
        String currentAddress = faker.address().fullAddress();

        step("Открываем форму для ввода", () -> {
            open ("https://demoqa.com/automation-practice-form");
            $("html").shouldHave(text("Student Registration Form"));
        });

        step("Заполняем поля для ввода", () -> {
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

        step("Загружаем файл", () -> {
            $("#uploadPicture").uploadFile(file);
        });

        step("Submit", () ->{
            $("#submit").click();
        });

        step("Проверяем заполненную форму", () -> {
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
}