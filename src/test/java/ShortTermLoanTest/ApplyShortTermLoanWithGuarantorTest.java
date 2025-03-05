package ShortTermLoanTest;

import Base.BaseClassUAT2;
import Base.DbMTEST;
import Base.GuarantorPage;
import Pages.HomePage;
import Pages.LoginPage;
import ShortTermLoan.AddShortTermLoanPage;
import ShortTermLoan.ShortTermLoanResultPage;
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

public class ApplyShortTermLoanWithGuarantorTest extends BaseClassUAT2 {
  HomePage homePage;
  LoginPage loginPage;

  SoftAssert softAssert;
  Actions action;
  ExcelUtil excelUtil;
  DbMTEST dbMTEST;
  ShortTermLoanResultPage shortTermLoanResultPage;
  AddShortTermLoanPage addshortTermLoanPage;
  Map<String, String> loan = new HashMap<>();
  GuarantorPage guarantorPage;

  @BeforeClass
  public void setup() throws IOException, SQLException {
    String excelPath = "src/main/java/data/LendingData.xlsx";
    Browserintialize("chrome", "https://uatxpresso.roinet.in/Login.aspx");

    excelUtil = new ExcelUtil(excelPath);
    homePage = new HomePage();
    loginPage = new LoginPage();
    guarantorPage = new GuarantorPage();
    shortTermLoanResultPage = new ShortTermLoanResultPage();
    addshortTermLoanPage = new AddShortTermLoanPage();
    softAssert = new SoftAssert();
    dbMTEST = new DbMTEST();
    String cspUser = excelUtil.getCellData("ShortTermLoan", 18, 1).trim();
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
    homePage.goToShortTermLoan();
    action = new Actions(driver);

  }

  @Test(priority = 1, testName = "click add loan to navigate to loan page")
  public void goToLoanPage() throws IOException {
    shortTermLoanResultPage.clickAddButton();
  }

  @Test(priority = 2, testName = "apply for loan")
  public void applyLoan() throws Exception {
    String spouse = excelUtil.getCellData("ShortTermLoan", 1, 1);
    String loanAmount = excelUtil.getCellData("ShortTermLoan", 2, 1);
    String gstStmt = excelUtil.getCellData("ShortTermLoan", 3, 1);
    String bankStmt = excelUtil.getCellData("ShortTermLoan", 4, 1);
    String otherDoc = excelUtil.getCellData("ShortTermLoan", 5, 1);
    String chequeScan = excelUtil.getCellData("ShortTermLoan", 6, 1);
    String chequeNumber = excelUtil.getCellData("ShortTermLoan", 7, 1);
    String chequeBank = excelUtil.getCellData("ShortTermLoan", 8, 1);
    String chequeOf = excelUtil.getCellData("ShortTermLoan", 9, 1);
    String apprEmail = excelUtil.getCellData("ShortTermLoan", 10, 1);
    String panNumber = excelUtil.getCellData("ShortTermLoan", 11, 1);
    String panDoc = excelUtil.getCellData("ShortTermLoan", 12, 1);
    String aadharNumber = excelUtil.getCellData("ShortTermLoan", 13, 1);
    String aadharDoc = excelUtil.getCellData("ShortTermLoan", 14, 1);
    String cibilDoc = excelUtil.getCellData("ShortTermLoan", 15, 1);
    String ledgerIncome = excelUtil.getCellData("ShortTermLoan", 4, 1);
    String shopOwnerProof = excelUtil.getCellData("ShortTermLoan", 4, 1);
    String otherBusiness = excelUtil.getCellData("ShortTermLoan", 23, 1);
    String otherOperator = excelUtil.getCellData("ShortTermLoan", 24, 1);
    String otherLoan = excelUtil.getCellData("ShortTermLoan", 24, 1);
    String stability = excelUtil.getCellData("ShortTermLoan", 25, 1);
    String exposure = excelUtil.getCellData("ShortTermLoan", 26, 1);
    addshortTermLoanPage.fillLoanDetails(spouse, stability, exposure, loanAmount, gstStmt, bankStmt, otherDoc,
          chequeScan, chequeNumber, chequeBank, chequeOf, apprEmail, panNumber, panDoc, aadharNumber,
          aadharDoc, ledgerIncome, shopOwnerProof, otherBusiness, otherOperator, otherLoan, cibilDoc);
    addshortTermLoanPage.clickSaveButton();
    acceptAlert();
    String loanNumber = addshortTermLoanPage.getLoanNumber();
    System.out.println("loanNumber: " + loanNumber);
    File file = new File("shortTermLoanWithGuarantor.txt");
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
    File file = new File("shortTermLoanWithGuarantor.txt");
    String loanNumber = "";

    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
      // Read the loan number from the file
      loanNumber = reader.readLine(); // Assuming the loan number is on the first line
      System.out.println("Loan number read from file: " + loanNumber);
    } catch (IOException e) {
      e.printStackTrace();
    }
    shortTermLoanResultPage.enterLoanNumber(loanNumber);
    shortTermLoanResultPage.selectLoanStatus("--Select--");
    shortTermLoanResultPage.clickViewButton();
    shortTermLoanResultPage.clickGuarantorDetailButton();
    String longTermResult = driver.getWindowHandle();
    Set<String> windowSet = driver.getWindowHandles();
    Iterator<String> i = windowSet.iterator();
    while (i.hasNext()) {
      String guarantorWindow = i.next();
      if (!longTermResult.equals(guarantorWindow)) {
        driver.switchTo().window(guarantorWindow);
        String panDocPath = excelUtil.getCellData("LoanDetail", 7, 1).trim();
        String aadharDocPath = excelUtil.getCellData("LoanDetail", 9, 1).trim();
        String bankStatement = excelUtil.getCellData("LoanDetail", 15, 1).trim();
        guarantorPage.enterGuarantorDetail("DELHI & NCR", "GURGAON", "Rohit Mathur", "8290336521", "rohit.mathur@roinet.in",
              "Salaried", "friend", "3", "ABCDE TOWER 10, FLAT 903, NEAR HUDA MARKET, TWIN TOWER", "123456"
              , "536350660843", "BXRPM9931K", panDocPath, aadharDocPath, bankStatement, "no", "22/07/1993", "Male");
        guarantorPage.clickSaveButton();
      }

    }
    driver.switchTo().window(longTermResult);
  }

}
