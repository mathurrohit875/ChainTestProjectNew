package CMSLoanTest;

import Base.BaseClassUAT2;
import Base.DbMTEST;
import Base.GuarantorPage;
import CMSLoan.AddCMSLoanPage;
import CMSLoan.CMSLoanResultPage;
import Pages.HomePage;
import Pages.LoginPage;
import Utility.ExcelUtil;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class ApplyCMSLoanWithGuarantorTest extends BaseClassUAT2 {

  HomePage homePage;
  LoginPage loginPage;

  SoftAssert softAssert;
  Actions action;
  ExcelUtil excelUtil;
  DbMTEST dbMTEST;
  CMSLoanResultPage cmsLoanResultPage;
  AddCMSLoanPage addCMSLoanPage;
  GuarantorPage guarantorPage;
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
    guarantorPage = new GuarantorPage();
    dbMTEST = new DbMTEST();
    String cspUser = excelUtil.getCellData("CMSLoan", 18, 1).trim();
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
    String spouse = excelUtil.getCellData("CMSLoan", 1, 1);
    String loanAmount = excelUtil.getCellData("CMSLoan", 19, 1);
    String loanTenureDays = "6";
    String gstStmt = excelUtil.getCellData("CMSLoan", 3, 1);
    String bankStmt = excelUtil.getCellData("CMSLoan", 4, 1);
    String otherDoc = excelUtil.getCellData("CMSLoan", 5, 1);
    String chequeScan = excelUtil.getCellData("CMSLoan", 6, 1);
    String chequeNumber = excelUtil.getCellData("CMSLoan", 7, 1);
    String chequeBank = excelUtil.getCellData("CMSLoan", 8, 1);
    String chequeOf = excelUtil.getCellData("CMSLoan", 9, 1);
    String apprEmail = excelUtil.getCellData("CMSLoan", 10, 1);
    String panNumber = excelUtil.getCellData("CMSLoan", 11, 1);
    String panDoc = excelUtil.getCellData("CMSLoan", 12, 1);
    String aadharNumber = excelUtil.getCellData("CMSLoan", 13, 1);
    String aadharDoc = excelUtil.getCellData("CMSLoan", 14, 1);
    String cibilDoc = excelUtil.getCellData("CMSLoan", 15, 1);
    String stability = excelUtil.getCellData("CMSLoan", 23, 1);
    String exposure = excelUtil.getCellData("CMSLoan", 24, 1);
    addCMSLoanPage.fillLoanDetails(spouse, stability, exposure, loanAmount, loanTenureDays, gstStmt, bankStmt, otherDoc,
          chequeScan, chequeNumber, chequeBank, chequeOf, apprEmail, panNumber, panDoc, aadharNumber,
          aadharDoc, cibilDoc);
    addCMSLoanPage.clickSaveButton();
    String loanNumber = addCMSLoanPage.getLoanNumber();
    System.out.println("loanNumber: " + loanNumber);
    File file = new File("cmsTermLoanWithGuarantor.txt");
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

  @Test(priority = 3, testName = "add guarantor")
  public void addGuarantor() throws IOException {
    File file = new File("cmsTermLoanWithGuarantor.txt");
    String loanNumber = "";

    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
      // Read the loan number from the file
      loanNumber = reader.readLine(); // Assuming the loan number is on the first line
      System.out.println("Loan number read from file: " + loanNumber);
    } catch (IOException e) {
      e.printStackTrace();
    }
    cmsLoanResultPage.enterLoanNumber(loanNumber);
    cmsLoanResultPage.selectLoanStatus("--All--");
    cmsLoanResultPage.clickViewButton();
    cmsLoanResultPage.clickGuarantorDetailButton();
    String cmsLoanResultPage = driver.getWindowHandle();
    Set<String> windowSet = driver.getWindowHandles();
    Iterator<String> i = windowSet.iterator();
    while (i.hasNext()) {
      String guarantorWindow = i.next();
      if (!cmsLoanResultPage.equals(guarantorWindow)) {
        driver.switchTo().window(guarantorWindow);
        String bankStmt = excelUtil.getCellData("CMSLoan", 4, 1);
        String panDoc = excelUtil.getCellData("CMSLoan", 12, 1);
        String aadharDoc = excelUtil.getCellData("CMSLoan", 14, 1);
        guarantorPage.enterGuarantorDetail("DELHI & NCR", "GURGAON", "Rohit Mathur", "8290336521", "rohit.mathur@roinet.in",
              "Salaried", "friend", "3", "ABCDE TOWER 10, FLAT 903, NEAR HUDA MARKET, TWIN TOWER", "123456"
              , "536350660843", "BXRPM9931K", panDoc, aadharDoc, bankStmt, "no", "22/07/1993", "Male");
        guarantorPage.clickSaveButton();
      }
    }
    driver.switchTo().window(cmsLoanResultPage);
  }
}
