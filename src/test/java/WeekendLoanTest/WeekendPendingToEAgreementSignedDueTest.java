package WeekendLoanTest;

import Base.BaseClassUAT2;
import Base.DbMTEST;
import Pages.HomePage;
import Pages.LoginPage;
import Utility.ExcelUtil;
import WeekendLoan.AddWeekendLoanPage;
import WeekendLoan.WeekendLoanResultPage;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

//This test will test the loan status change from Pending to EAreementSignedDue and
//EAgreementSignedDue to Pending.
public class WeekendPendingToEAgreementSignedDueTest extends BaseClassUAT2 {

  HomePage homePage;
  LoginPage loginPage;

  SoftAssert softAssert;
  Actions action;
  ExcelUtil excelUtil;
  DbMTEST dbMTEST;
  WeekendLoanResultPage weekendLoanResultPage;
  AddWeekendLoanPage addWeekendLoanPage;
  String loanNumber="";

  Map<String, String> loan = new HashMap<>();
  @BeforeMethod
  public void setup() throws IOException {
    String excelPath = "src/main/java/data/LendingData.xlsx";
    Browserintialize("chrome", "https://uatxpresso.roinet.in/Login.aspx");
    excelUtil = new ExcelUtil(excelPath);
    weekendLoanResultPage=new WeekendLoanResultPage();
    addWeekendLoanPage=new AddWeekendLoanPage();
    homePage = new HomePage();
    loginPage = new LoginPage();

    softAssert = new SoftAssert();
    loginPage.login("MONA.SHARMA", "roinet@1234", "KMJKN");
    loginPage.Login_With_OTP("222111");
    homePage.ClickonWALLET();
    homePage.goToWeekendLoan("MONA.SHARMA");
    action = new Actions(driver);
    dbMTEST = new DbMTEST();
    File file = new File("loanNumber.txt");


    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
      // Read the loan number from the file
      loanNumber = reader.readLine(); // Assuming the loan number is on the first line
      System.out.println("Loan number read from file: " + loanNumber);
    } catch (IOException e) {
      e.printStackTrace();
    }
    weekendLoanResultPage.enterLoanNumber(loanNumber);
    weekendLoanResultPage.selectLoanStatus("--All--");
    weekendLoanResultPage.clickViewButton();
    weekendLoanResultPage.btnViewDetails();

  }

  @Test(priority = 1,testName = "change guarantor status to EAgreementSignedDue")
  public void changeGuarantorStatusToEAgreementSignedDue(){
    String guarantorStatus="EAgreementSignedDue";
    addWeekendLoanPage.changeGuarantorStatus(guarantorStatus);
    addWeekendLoanPage.clickSaveButton();
  }

  @Test(priority = 2,testName = "change loan status to EAgreementSignedDue",enabled = false)
  public void changeLoanStatusToEAgreementSignedDue(){
    String loanStatus="EAgreementSignedDue";
    addWeekendLoanPage.changeLoanStatus(loanStatus);
    addWeekendLoanPage.clickSaveButton();
  }

  @Test(priority = 3,testName = "change  loan status to pending",enabled = false)
  public void EAgreementSignedDueToPending(){
    String loanStatus="Pending";
    addWeekendLoanPage.changeLoanStatus(loanStatus);
    addWeekendLoanPage.clickSaveButton();
  }

  @AfterMethod
  public void quit(){
    driver.quit();
  }
}
