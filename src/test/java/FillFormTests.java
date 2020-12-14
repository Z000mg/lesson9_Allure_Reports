import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;

public class FillFormTests {
    public static String
            first_name = "Alexander",
            last_name = "Ivanov",
            email = "exmpl@ya.ru",
            number = "9031234567",
            adress = "Lenina str.",
            subj = "Physics";
    File file = new File("src/test/resources/1.jpg");

    @BeforeAll
    static void setup(){
        Configuration.startMaximized = true;
    }

    @Test
    void lesson2Form() {
        open ("https://demoqa.com/automation-practice-form");
        $("html").shouldHave(text("Student Registration Form"));
        $("#firstName").val(first_name);
        $("#lastName").val(last_name);
        $("#userEmail").val(email);
        $x("//*[@for='gender-radio-1']").click();
        $("#userNumber").val(number);
        $("#dateOfBirthInput").click();
        $(".react-datepicker__month-select").selectOptionByValue("6");
        $(".react-datepicker__year-select").selectOptionByValue("1973");
        $(".react-datepicker__day--007").click();
        $("#subjectsInput").click();
        $("#subjectsInput").val(subj).pressEnter();
        $x("//*[@for='hobbies-checkbox-1']").click();
        $x("//*[@for='hobbies-checkbox-3']").click();
        $("#uploadPicture").uploadFile(file);
        $("#currentAddress").val(adress);
        $("#stateCity-wrapper").$(byText("Select State")).click();
        $(byText("NCR")).click();
        $("#stateCity-wrapper").$(byText("Select City")).click();
        $(byText("Delhi")).click();
        $("#submit").click();

        $x("//td[text()='Student Name']/following::td[1]").shouldHave(text(first_name + " " + last_name));
        $x("//td[text()='Student Email']/following::td[1]").shouldHave(text(email));
        $x("//td[text()='Gender']/following::td[1]").shouldHave(text("Male"));
        $x("//td[text()='Mobile']/following::td[1]").shouldHave(text(number));
        $x("//td[text()='Date of Birth']/following::td[1]").shouldHave(text("07 July,1973"));
        $x("//td[text()='Hobbies']/following::td[1]").shouldHave(text("Sports, Music"));
        $x("//td[text()='Picture']/following::td[1]").shouldHave(text("1.jpg"));
        $x("//td[text()='Address']/following::td[1]").shouldHave(text(adress));
        $x("//td[text()='State and City']/following::td[1]").shouldHave(text("NCR Delhi"));
    }
}