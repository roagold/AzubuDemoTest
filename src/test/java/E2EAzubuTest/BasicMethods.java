package E2EAzubuTest;

import com.codeborne.selenide.*;
import com.google.common.io.Files;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import org.jruby.RubyProcess;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import ru.yandex.qatools.allure.annotations.Attachment;
import ru.yandex.qatools.allure.annotations.Step;

import java.io.*;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.List;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

/**
 * Created by oryabinskiy on 12/2/2015.
 */
public class BasicMethods {

    SelenideElement module = $("ng-transclude ul li");
    SelenideElement successConfirmation = $(".toast-success");
    SelenideElement avatar = $(".header-player-avatar");
    SelenideElement gameCategories =  $(".stream-list-content li");
    SelenideElement discoverMenu = $("a.ng-binding.on");
    ElementsCollection profileMenu = $$("div.megamenu-account-btn");

    String newUsername = "auto_or";
    String newPassword = "Auto_or";
    String accountFile = "Stage_acc.csv";
    String regUsersFile = "Stage_RegUsers.csv";
    String moduleName = "Gear_module";
    String urlAddress = "http://www.stg.azubu.it";

    @Step
    public void smartOpen() throws IOException {
        open(urlAddress);
        if(avatar.is(visible)){
            logout();
            assertLogoutSuccess();
        }
    }

    @Step
    public void login(String name, String pass) {
        $(".log-in").click();
        $("#loginUsername").setValue(name);
        $("#loginPassword").setValue(pass).pressEnter();
    }

    @Step
    public void logout(){
        openMenuItem("Logout");
    }

    @Step
    public void openMenuItem(String menuItem){
        avatar.click();
        $(".col-lg-3").shouldBe(visible);
        profileMenu.find(text(menuItem)).click();
    }

    @Step
    public void selectModuleType(String type){
        $("#moduleType").click();
        $$("#moduleType .ng-binding").findBy(text(type)).click();
    }

    @Step
    public void deleteModule(){
        $(".ng-scope.gridster-item").shouldBe(visible).hover();
        $(".fa-remove").shouldBe(visible).click();
    }

    @Step
    public void generateNewEmailAddress(){
        open("https://10minutemail.net/");
        $("a[href=\"new.html\"]").click();
    }

    //Assertions
    @Step
    public void assertGameCategoriesDisplayed() {
        $("a.delay-translate.ng-binding[href=\"/games\"]").click();
        gameCategories.shouldBe(visible);
    }

    @Step
    public void assertLogoutSuccess() {
        $(".login-signup").shouldBe(visible);
    }

    @Step
    public void assertLoginSuccess() {
        avatar.shouldBe(visible);
    }

    @Step
    public void readDataFromFile(String name) throws FileNotFoundException {
        CSVReader reader = new CSVReader(new FileReader(name));
    }

    //ACTIONS WITH CSV Files
    @Step
    public void writeEmailToFile(String emailAddress) throws IOException{
        Integer nextVal = 1;
        try {
            nextVal = getNextVal();
            CSVWriter writer = new CSVWriter(new FileWriter(regUsersFile, true));
            String[] record = (emailAddress + "," + newUsername + "_" + nextVal + "," + newPassword + "_" + nextVal).split(",");
            writer.writeNext(record);
            writer.close();
        } catch(IOException ioex) {
            //TODO log it if need
            System.out.println("Error. The file "+regUsersFile+" does not exist. Please create it as empty file.");
        }
    }

    @Step
    public Integer getNextVal() throws IOException {
        Integer nextVal = 0;
        LineNumberReader  lineNumberReader = new LineNumberReader(new FileReader(regUsersFile));
        lineNumberReader.skip(Long.MAX_VALUE);
        nextVal = lineNumberReader.getLineNumber() + 1;
        lineNumberReader.close();
        return nextVal;
    }


    public String getUsername(String criteria) throws FileNotFoundException, IOException {
        String result = null;
        CSVReader csvReader = new CSVReader(new FileReader(accountFile));
        String[] row = null;
        while ((row = csvReader.readNext()) != null) {
            if (row[2].equals(criteria)) {
                result = row[0];
            }
        }
        csvReader.close();
        return result;
    }

    public String getPassword(String criteria) throws FileNotFoundException, IOException {
        String result = null;
        CSVReader csvReader = new CSVReader(new FileReader(accountFile));
        String[] row = null;
        while ((row = csvReader.readNext()) != null) {
            if (row[2].equals(criteria)) {
                result = row[1];
            }
        }
        csvReader.close();
        return result;
    }

    public String getTitle(String criteria) throws Exception {
        String result = null;
        CSVReader csvReader = new CSVReader(new FileReader(moduleName));
        String[] row = null;
        while ((row = csvReader.readNext()) != null){
            if (row[1].equals(criteria)){
                result = row[0];
            }
        }
        csvReader.close();
        return result;
    }

  /*  @Test
    public void getLastUsername() throws IOException {
        String username = "";

        BufferedReader buffer = new BufferedReader(new FileReader(regUsersFile));

        CSVReader csvReader = new CSVReader(new FileReader(regUsersFile));
        List<String[]> allLines = csvReader.readAll();

        List<String> lastLine = allLines.get(allLines.size());
        System.out.println("## LAST LINE: "+lastLine);
        csvReader.close(); */


        //CSVReader csvReader = new CSVReader(new FileReader(moduleName));
        //lineNumberReader.skip(Long.valueOf(getNextVal() + 1));
        //System.out.println(" TEST: "+lineNumberReader.read(CharBuffer.allocate(2)));
        //nextVal = lineNumberReader.getLineNumber() + 1;
        //lineNumberReader.close();

        //return username;
   // }
}
