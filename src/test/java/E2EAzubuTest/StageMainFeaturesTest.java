package E2EAzubuTest;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Screenshots;
import com.google.common.io.Files;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.chrome.ChromeDriver;
import ru.yandex.qatools.allure.annotations.Attachment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static org.junit.Assert.assertEquals;

/**
 * Created by oryabinskiy on 12/4/2015.
 */
public class StageMainFeaturesTest extends BasicMethods {

    @BeforeClass
    public static void setup(){
        System.setProperty("webdriver.chrome.driver", "C://Program Files//Google//Chrome//Application//chromedriver.exe");
        Configuration.browser = "chrome";
    }

    @After
    public void tearDown() throws IOException {
        screenshot();
    }

    @Attachment(type = "image/png")
    public byte[] screenshot() throws IOException {
        File screenshot = Screenshots.getScreenShotAsFile();
        return Files.toByteArray(screenshot);
    }

    @Test
    public void incorrectLogin() throws IOException {
        smartOpen();
        login("incorrect", "some pass");
        $(".toast-error div").shouldBe(visible).should(have(exactText("login was not successful. please try again.")));
    }

    @Test
    public void testLogin() throws IOException {
        smartOpen();
        readDataFromFile("Stage_acc.csv");
        login(getUsername("Broadcaster"), getPassword("Broadcaster"));
        successConfirmation.shouldBe(visible);
        assertLoginSuccess();
    }

    @Test
    public void testIsStreamAppearOnHomePage() throws IOException {
        if($(".home-video-list li").is(visible)){
        } else {
            screenshot();
        }
    }

    @Test
    public void testCreatingAndDeletingGearModule() throws IOException {
        openMenuItem("My Channel");
        //Briks are not appears without refresh
        refresh();
        while(module.is(visible)){
            deleteModule();
            sleep(10000);
            refresh();
        }
        module.shouldBe(not(visible));
        openMenuItem("create a Module");
        selectModuleType("gear");
        $("#brickTitle").setValue("Test Title");
        $("#itemName").setValue("Test name");
        $("#itemURL").setValue("http://google.com");
        $("#itemCategory").setValue("Test category");
        $(".button-main").click();
        successConfirmation.shouldBe(visible);

        openMenuItem("My Channel");
        for(int i=0; i<7; i++) {
            if (module.is(visible)) {
                screenshot();
                break;
            } else {
                sleep(5000);
                refresh();
            }
        }
        deleteModule();
        module.is(not(visible));
        screenshot();
        sleep(30000);
        refresh();
        module.is(not(visible));
    }

     /* @Test
    public void testRegistration() throws IOException {
        generateNewEmailAddress();
        String emailAddress = $("#fe_text").shouldBe(visible).getAttribute("value");
        writeEmailToFile(emailAddress);
        smartOpen();
        $(".sign-up").click();
        $("#signUpEmail").setValue(emailAddress).pressEnter();
        successConfirmation.shouldBe(visible);
        open("https://10minutemail.net/");
        $$("#maillist td").find(exactText("Confirm your AzubuTV registration")).shouldBe(visible).click();
        $("#tabs-1").shouldBe(visible).should(have(text("new account creation on Azubu.tv")));
        $("#tabs-1 a").click();
        $("#form-registration-user").shouldBe(visible);
        $(".form-control[type=text]").setValue("name");
        $(".form-control[name=password_1]").setValue("123321");
        $(".form-control[name=password_2]").setValue("123321");
        $("#license-checkbox").click();
        sleep(10000);
        }*/
}

