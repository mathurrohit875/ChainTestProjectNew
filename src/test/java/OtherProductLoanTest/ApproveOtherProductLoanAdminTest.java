package OtherProductLoanTest;

import Base.BaseClassUAT2;
import Base.DbMTEST;
import OtherProductsLoan.AddOtherProductLoan;
import OtherProductsLoan.OtherProductLoanResultPage;
import Pages.HomePage;
import Pages.LoginPage;
import Utility.ExcelUtil;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;


public class ApproveOtherProductLoanAdminTest extends BaseClassUAT2 {

  HomePage homePage;
  LoginPage loginPage;

  SoftAssert softAssert;
  Actions action;
  ExcelUtil excelUtil;
  DbMTEST dbMTEST;
  OtherProductLoanResultPage otherProductLoanResultPage;
  AddOtherProductLoan addOtherProductLoan;
  String loanNumber = "";

  //Map<String, String> loan = new HashMap<>();

  @BeforeClass
  public void setup() throws IOException {
    String excelPath = "src/main/java/data/LendingData.xlsx";
    Browserintialize("chrome", "https://uatxpresso.roinet.in/Login.aspx");
    excelUtil = new ExcelUtil(excelPath);
    otherProductLoanResultPage = new OtherProductLoanResultPage();
    addOtherProductLoan = new AddOtherProductLoan();
    homePage = new HomePage();
    loginPage = new LoginPage();

    softAssert = new SoftAssert();
    loginPage.login("MONA.SHARMA", "roinet@1234", "KMJKN");
    loginPage.Login_With_OTP("222111");
    homePage.ClickonWALLET();
    homePage.goToOtherProductLoan();
    action = new Actions(driver);
    dbMTEST = new DbMTEST();
  }

  @Test(priority = 1, testName = "open loan detail on grid page")
  public void openLoan() throws IOException {
    File file = new File(prop.getProperty("otherProductLoan"));


    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
      // Read the loan number from the file
      loanNumber = reader.readLine(); // Assuming the loan number is on the first line
      System.out.println("Loan number read from file: " + loanNumber);
    } catch (IOException e) {
      System.out.println(e.getMessage());
    }
    String updateLoanStatus = "update tm_channelemi set loanstatus=3, isemandate=1 where loanno='" + loanNumber + "'";
    String updateGuarantorStatus = "update tm_channelemi set guarantoragreementstatus=3, isguarantoremandate=1 where loanno='" + loanNumber + "'";
    dbMTEST.executeUpdate(updateLoanStatus);
    dbMTEST.executeUpdate(updateGuarantorStatus);
    otherProductLoanResultPage.enterLoanNumber(loanNumber);
    otherProductLoanResultPage.selectLoanStatus("--Select--");
    otherProductLoanResultPage.clickViewButton();
    otherProductLoanResultPage.btnViewDetails();

    addOtherProductLoan.changeLoanStatus("LoanApproved");
    addOtherProductLoan.addApprovalRemark("loan approved");
    String approvalEmailDoc = excelUtil.getCellData(prop.getProperty("OtherProductSheetName"), 38, 1);
    addOtherProductLoan.attachapprovalEmailDoc(approvalEmailDoc);
    addOtherProductLoan.btnGoAhead();
    try {
      acceptAlert();
    } catch (TimeoutException t) {
      System.out.println("approval take time hence throwing exception." + t.getMessage());
    }
    /*String alertText = LendingUtility.getAlertBoxText();
    softAssert.assertEquals(alertText.equalsIgnoreCase("Your Wallet Advance request updated successfully."),true,"Wallet Advance not approved because: "+alertText);
    softAssert.assertAll();
    acceptAlert();*/
  }

  @AfterClass
  public void quit() {
    driver.quit();
  }
}
