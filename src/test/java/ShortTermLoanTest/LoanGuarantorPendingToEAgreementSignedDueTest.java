package ShortTermLoanTest;

import Base.BaseClassUAT2;
import Base.DbMTEST;
import Pages.HomePage;
import Pages.LoginPage;
import ShortTermLoan.AddShortTermLoanPage;
import ShortTermLoan.ShortTermLoanResultPage;
import Utility.ExcelUtil;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

//This test will test the loan status change from Pending to EAreementSignedDue and
//EAgreementSignedDue to Pending for loan where guarantor is made mandatory from admin
public class LoanGuarantorPendingToEAgreementSignedDueTest extends BaseClassUAT2 {

  HomePage homePage;
  LoginPage loginPage;

  SoftAssert softAssert;
  Actions action;
  ExcelUtil excelUtil;
  DbMTEST dbMTEST;
  ShortTermLoanResultPage shortTermLoanResultPage;
  AddShortTermLoanPage addShortTermLoanPage;
  String loanNumber = "";


  @BeforeClass
  public void setup() throws IOException {
    String excelPath = "src/main/java/data/LendingData.xlsx";
    Browserintialize("chrome", "https://uatxpresso.roinet.in/Login.aspx");
    excelUtil = new ExcelUtil(excelPath);
    shortTermLoanResultPage = new ShortTermLoanResultPage();
    addShortTermLoanPage = new AddShortTermLoanPage();
    homePage = new HomePage();
    loginPage = new LoginPage();

    softAssert = new SoftAssert();
    loginPage.login("MONA.SHARMA", "roinet@1234", "KMJKN");
    loginPage.Login_With_OTP("222111");
    homePage.ClickonWALLET();
    homePage.goToShortTermLoan();
    action = new Actions(driver);
    dbMTEST = new DbMTEST();
  }

  @Test(priority = 1, testName = "change guarantor status to EAgreementSignedDue")
  public void changeGuarantorStatusToEAgreementSignedDue() {
    readLoanNumber();
    String guarantorStatus = "EAgreementSignedDue";
    addShortTermLoanPage.changeGuarantorStatus(guarantorStatus);
    addShortTermLoanPage.btnGoAhead();
    acceptAlert();
  }

  @Test(priority = 2, testName = "change loan status to EAgreementSignedDue")
  public void changeLoanStatusToEAgreementSignedDue() {
    acceptAlert();
    readLoanNumber();
    String loanStatus = "EAgreementSignedDue";
    addShortTermLoanPage.changeLoanStatus(loanStatus);
    addShortTermLoanPage.btnGoAhead();
    acceptAlert();
  }

  public void readLoanNumber() {
    File file = new File(prop.getProperty("shortTermLoan"));


    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
      // Read the loan number from the file
      loanNumber = reader.readLine(); // Assuming the loan number is on the first line
      System.out.println("Loan number read from file: " + loanNumber);
    } catch (IOException e) {
      System.out.println(e.getMessage());
    }
    shortTermLoanResultPage.enterLoanNumber(loanNumber);
    shortTermLoanResultPage.selectLoanStatus("--Select--");
    shortTermLoanResultPage.clickViewButton();
    shortTermLoanResultPage.btnViewDetails();
  }

  /* @Test(priority = 3,testName = "change loan status to Pending")
   public void EAgreementSignedDueToPending(){
     String loanStatus="Pending";
     addWeekendLoanPage.changeLoanStatus(loanStatus);
     addWeekendLoanPage.clickSaveButton();
     acceptAlert();
   }
 */
  @AfterClass
  public void quit() {
    driver.quit();
  }
}
