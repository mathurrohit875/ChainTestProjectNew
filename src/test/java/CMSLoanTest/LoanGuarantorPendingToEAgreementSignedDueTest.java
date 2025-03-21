package CMSLoanTest;

import Base.BaseClassUAT2;
import Base.DbMTEST;
import CMSLoan.AddCMSLoanPage;
import CMSLoan.CMSLoanResultPage;
import Pages.HomePage;
import Pages.LoginPage;
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
  CMSLoanResultPage cmsLoanResultPage;
  AddCMSLoanPage addCMSLoanPage;
  String loanNumber = "";


  @BeforeClass
  public void setup() throws IOException {
    String excelPath = prop.getProperty("excelPath");
    Browserintialize("chrome", "https://uatxpresso.roinet.in/Login.aspx");
    excelUtil = new ExcelUtil(excelPath);
    cmsLoanResultPage = new CMSLoanResultPage();
    addCMSLoanPage = new AddCMSLoanPage();
    homePage = new HomePage();
    loginPage = new LoginPage();

    softAssert = new SoftAssert();
    loginPage.login("MONA.SHARMA", "roinet@1234", "KMJKN");
    loginPage.Login_With_OTP("222111");
    homePage.ClickonWALLET();
    homePage.goToCMSLoan();
    action = new Actions(driver);
    dbMTEST = new DbMTEST();
  }

  @Test(priority = 1, testName = "change guarantor status to EAgreementSignedDue")
  public void changeGuarantorStatusToEAgreementSignedDue() {
    readLoanNumber();
    String guarantorStatus = "EAgreementSignedDue";
    addCMSLoanPage.changeGuarantorStatus(guarantorStatus);
    addCMSLoanPage.clickSaveButton();
    acceptAlert();
  }

  @Test(priority = 2, testName = "change loan status to EAgreementSignedDue")
  public void changeLoanStatusToEAgreementSignedDue() {
    readLoanNumber();
    String loanStatus = "EAgreementSignedDue";
    addCMSLoanPage.changeLoanStatus(loanStatus);
    addCMSLoanPage.clickSaveButton();
    acceptAlert();
  }

  public void readLoanNumber() {
    File file = new File(prop.getProperty("cmsLoan"));


    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
      // Read the loan number from the file
      loanNumber = reader.readLine(); // Assuming the loan number is on the first line
      System.out.println("Loan number read from file: " + loanNumber);
    } catch (IOException e) {
      System.out.println(e.getMessage());
    }
    cmsLoanResultPage.enterLoanNumber(loanNumber);
    cmsLoanResultPage.selectLoanStatus("--All--");
    cmsLoanResultPage.clickViewButton();
    cmsLoanResultPage.btnViewDetails();
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
