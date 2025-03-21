package WeekendLoanTest;


import Base.BaseClassUAT2;
import Base.ChainTestListener;
import Base.DbMTEST;
import Base.GuarantorPage;
import Pages.HomePage;
import Pages.LoginPage;
import Utility.ExcelUtil;
import WeekendLoan.AddWeekendLoanPage;
import WeekendLoan.WeekendLoanResultPage;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class ApplyWeekendLoanWithGuarantorTest extends BaseClassUAT2 {

  HomePage homePage;
  LoginPage loginPage;

  SoftAssert softAssert;
  Actions action;
  ExcelUtil excelUtil;
  DbMTEST dbMTEST;
  WeekendLoanResultPage weekendLoanResultPage;
  AddWeekendLoanPage addWeekendLoanPage;
  GuarantorPage guarantorPage;
  Map<String, String> loan = new HashMap<>();

  @BeforeClass
  public void setup() throws IOException, SQLException {
    //
    String excelPath = "src/main/java/data/LendingData.xlsx";
    Browserintialize("chrome", "https://uatxpresso.roinet.in/Login.aspx");
    ChainTestListener.log("log chrom");
    //driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    excelUtil = new ExcelUtil(excelPath);
    homePage = new HomePage();
    loginPage = new LoginPage();
    weekendLoanResultPage = new WeekendLoanResultPage();
    addWeekendLoanPage = new AddWeekendLoanPage();
    guarantorPage = new GuarantorPage();
    softAssert = new SoftAssert();
    dbMTEST = new DbMTEST();
    String cspUser = excelUtil.getCellData(prop.getProperty("WeekendSheetName"), 16, 1).trim();
    System.out.println("checking the user.: " + cspUser);
    String getChannelId = "select userid from tm_user where usercode='" + cspUser + "'";

    ResultSet rs = dbMTEST.executeQuery(getChannelId);
    String channelId = "";
    while (rs.next()) {
      channelId = rs.getString("userid");
    }
    String updateWalletBalance = "update tm_channel set availablelimit=100.00 where channelid=" + channelId;
    dbMTEST.executeUpdate(updateWalletBalance);
    loginPage.login(cspUser, "roinet@1234", "KMJKN");
    loginPage.Login_With_OTP("222111");
    homePage.ClickonWALLET();
    homePage.goToWeekendLoan(cspUser);
    action = new Actions(driver);

  }

  @Test(priority = 1, testName = "click add loan to navigate to loan page")
  public void goToLoanPage() throws IOException {
    ChainTestListener.log("log chrom");
    weekendLoanResultPage.clickAddButton();
  }

  @Test(priority = 2, testName = "apply for loan with guarantor.")
  public void applyLoan() throws Exception {
    ChainTestListener.log("log chrom");
    String spouse = excelUtil.getCellData(prop.getProperty("WeekendSheetName"), 1, 1);
    String loanAmount = excelUtil.getCellData(prop.getProperty("WeekendSheetName"), 2, 1);
    String gstStmt = excelUtil.getCellData(prop.getProperty("WeekendSheetName"), 3, 1);
    String bankStmt = excelUtil.getCellData(prop.getProperty("WeekendSheetName"), 4, 1);
    String otherDoc = excelUtil.getCellData(prop.getProperty("WeekendSheetName"), 5, 1);
    String chequeScan = excelUtil.getCellData(prop.getProperty("WeekendSheetName"), 6, 1);
    String chequeNumber = excelUtil.getCellData(prop.getProperty("WeekendSheetName"), 7, 1);
    String chequeBank = excelUtil.getCellData(prop.getProperty("WeekendSheetName"), 8, 1);
    String chequeOf = excelUtil.getCellData(prop.getProperty("WeekendSheetName"), 9, 1);
    String apprEmail = excelUtil.getCellData(prop.getProperty("WeekendSheetName"), 10, 1);
    String panNumber = excelUtil.getCellData(prop.getProperty("WeekendSheetName"), 11, 1);
    String panDoc = excelUtil.getCellData(prop.getProperty("WeekendSheetName"), 12, 1);
    String aadharNumber = excelUtil.getCellData(prop.getProperty("WeekendSheetName"), 13, 1);
    String aadharDoc = excelUtil.getCellData(prop.getProperty("WeekendSheetName"), 14, 1);
    String cibilDoc = excelUtil.getCellData(prop.getProperty("WeekendSheetName"), 15, 1);
    String walletExposureAmt = excelUtil.getCellData(prop.getProperty("WeekendSheetName"), 23, 1);
    String resStability = excelUtil.getCellData(prop.getProperty("WeekendSheetName"), 24, 1);
    addWeekendLoanPage.fillLoanDetails(spouse, loanAmount, gstStmt, bankStmt, otherDoc,
          chequeScan, chequeNumber, chequeBank, walletExposureAmt, resStability, chequeOf, apprEmail, panNumber, panDoc, aadharNumber,
          aadharDoc, cibilDoc);
    addWeekendLoanPage.clickSaveButton();
    String loanNumber = addWeekendLoanPage.getLoanNumber();
    File file = new File("weekendLoanNumber.txt");
    try {
      // Check if the file exists
      if (!file.exists()) {
        // If the file doesn't exist, create it
        boolean fileCreated = file.createNewFile();
        if (fileCreated) {
          ChainTestListener.log("log chrom");
          System.out.println("File created successfully.");
        } else {
          ChainTestListener.log("log chrom");
          System.out.println("File already exists or cannot be created.");
        }
      }
      try (FileWriter writer = new FileWriter(file)) {
        ChainTestListener.log("log chrom");
        // Write the loan number to the file
        writer.write(loanNumber);
        System.out.println("Loan number saved to file.");
      }
    } catch (IOException e) {
      System.out.println(e.getMessage());
      }

    System.out.println("loanNumber: " + loanNumber);
    weekendLoanResultPage.enterLoanNumber(loanNumber);
    weekendLoanResultPage.selectLoanStatus("--All--");
    weekendLoanResultPage.clickViewButton();
    weekendLoanResultPage.clickGuarantorDetailButton();
    String weekendResult = driver.getWindowHandle();
    Set<String> windowSet = driver.getWindowHandles();
    Iterator<String> i = windowSet.iterator();
    int max = 9999;
    int min = 1111;
    Random random = new Random();
    int pannumber = random.nextInt(max - min + 1) + min;
    while (i.hasNext()) {
      String guarantorWindow = i.next();
      if (!weekendResult.equals(guarantorWindow)) {
        driver.switchTo().window(guarantorWindow);

        guarantorPage.enterGuarantorDetail("DELHI & NCR", "GURGAON", "Rohit Mathur", "8290336521", "rohit.mathur@roinet.in",
              "Salaried", "friend", "3", "ABCDE TOWER 10, FLAT 903, NEAR HUDA MARKET, TWIN TOWER", "123456"
              , "536350660843", "BXRPM" + pannumber + "K", panDoc, aadharDoc, bankStmt, "no", "22/07/1993", "Male");
        guarantorPage.clickSaveButton();
      }

    }
    driver.switchTo().window(weekendResult);
  }

  @AfterClass
  public void quit(){
    driver.quit();
  }

}
