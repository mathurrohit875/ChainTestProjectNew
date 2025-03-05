package CMSLoanTest;


import Base.BaseClassUAT2;
import Base.DbMTEST;
import CMSLoan.AddCMSLoanPage;
import CMSLoan.CMSLoanResultPage;
import Pages.HomePage;
import Pages.LoginPage;
import Utility.ExcelUtil;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class ApplyCMSLoanTest extends BaseClassUAT2 {
  HomePage homePage;
  LoginPage loginPage;

  SoftAssert softAssert;
  Actions action;
  ExcelUtil excelUtil;
  DbMTEST dbMTEST;
  CMSLoanResultPage cmsLoanResultPage;
  AddCMSLoanPage addCMSLoanPage;
  Map<String, String> loan = new HashMap<>();

  @BeforeClass
  public void setup() throws IOException, SQLException {
    String excelPath = "src/main/java/data/LendingData.xlsx";
    Browserintialize("chrome", "https://uatxpresso.roinet.in/Login.aspx");

    excelUtil = new ExcelUtil(excelPath);
    homePage = new HomePage();
    loginPage = new LoginPage();
    cmsLoanResultPage = new CMSLoanResultPage();
    addCMSLoanPage = new AddCMSLoanPage();
    softAssert = new SoftAssert();
    dbMTEST = new DbMTEST();
    String cspUser = excelUtil.getCellData("CMSLoanTest", 18, 1).trim();
    String getChannelId = "select userid from tm_user where usercode='" + cspUser + "'";

    ResultSet rs = dbMTEST.executeQuery(getChannelId);
    String channelId = "";
    while (rs.next()) {
      channelId = rs.getString("userid");
    }
    String updateWalletBalance = "update tm_channel set availablelimit=100.00 where channelid=" + channelId;
    dbMTEST.executeQuery(updateWalletBalance);
    loginPage.login(cspUser, "roinet@1234", "KMJKN");
    loginPage.Login_With_OTP("222111");
    homePage.ClickonWALLET();
    homePage.goToCMSLoan();
    action = new Actions(driver);

  }

  @Test(priority = 1, testName = "click add loan to navigate to loan page")
  public void goToLoanPage() throws IOException {
    cmsLoanResultPage.clickAddButton();
  }

  @Test(priority = 2, testName = "apply for loan")
  public void applyLoan() throws Exception {
    String spouse = excelUtil.getCellData("CMSLoanTest", 1, 1);
    String loanAmount = excelUtil.getCellData("CMSLoanTest", 19, 1);
    String loanTenureDays = "6";
    String gstStmt = excelUtil.getCellData("CMSLoanTest", 3, 1);
    String bankStmt = excelUtil.getCellData("CMSLoanTest", 4, 1);
    String otherDoc = excelUtil.getCellData("CMSLoanTest", 5, 1);
    String chequeScan = excelUtil.getCellData("CMSLoanTest", 6, 1);
    String chequeNumber = excelUtil.getCellData("CMSLoanTest", 7, 1);
    String chequeBank = excelUtil.getCellData("CMSLoanTest", 8, 1);
    String chequeOf = excelUtil.getCellData("CMSLoanTest", 9, 1);
    String apprEmail = excelUtil.getCellData("CMSLoanTest", 10, 1);
    String panNumber = excelUtil.getCellData("CMSLoanTest", 11, 1);
    String panDoc = excelUtil.getCellData("CMSLoanTest", 12, 1);
    String aadharNumber = excelUtil.getCellData("CMSLoanTest", 13, 1);
    String aadharDoc = excelUtil.getCellData("CMSLoanTest", 14, 1);
    String cibilDoc = excelUtil.getCellData("CMSLoanTest", 15, 1);
    String stability = excelUtil.getCellData("CMSLoan", 23, 1);
    String exposure = excelUtil.getCellData("CMSLoan", 24, 1);
    addCMSLoanPage.fillLoanDetails(spouse, stability, exposure, loanAmount, loanTenureDays, gstStmt, bankStmt, otherDoc,
          chequeScan, chequeNumber, chequeBank, chequeOf, apprEmail, panNumber, panDoc, aadharNumber,
          aadharDoc, cibilDoc);
    addCMSLoanPage.clickSaveButton();
    String loanNumber = addCMSLoanPage.getLoanNumber();
    System.out.println("loanNumber: " + loanNumber);
    File file = new File("loanNumberWithoutGuarantor.txt");
    try {
      // Check if the file exists
      if (!file.exists()) {
        // If the file doesn't exist, create it
        boolean fileCreated = file.createNewFile();
        if (fileCreated) {
          System.out.println("File created successfully.");
        } else {
          System.out.println("File already exists or cannot be created.");
        }
      }
      try (FileWriter writer = new FileWriter(file)) {
        // Write the loan number to the file
        writer.write(loanNumber);
        System.out.println("Loan number saved to file.");
      }
    } catch (IOException e) {
      System.out.println(e.getMessage());
    }

  }

 /* @AfterClass
  public void quit(){
    driver.quit();

  }*/
}
