package LongTermLoanTest;

import Base.BaseClassUAT2;
import Base.ChainTestListener;
import Base.DbMTEST;
import Base.GuarantorPage;
import LongTermLoan.AddLongTermLoan;
import LongTermLoan.LongTermResultPage;
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
import java.util.Iterator;
import java.util.Set;

public class ApplyLongTermWithGuarantorTest extends BaseClassUAT2 {

  HomePage homePage;
  LoginPage loginPage;

  SoftAssert softAssert;
  Actions action;
  ExcelUtil excelUtil;
  DbMTEST dbMTEST;
  LongTermResultPage longTermResultPage;
  AddLongTermLoan addLongTermLoan;
  GuarantorPage guarantorPage;

  @BeforeClass
  public void setup() throws IOException, SQLException {
    //
    String excelPath = "src/main/java/data/LendingData.xlsx";
    Browserintialize("chrome", "https://uatxpresso.roinet.in/Login.aspx");
    ChainTestListener.log("log chrom");
    excelUtil = new ExcelUtil(excelPath);
    homePage = new HomePage();
    loginPage = new LoginPage();
    longTermResultPage = new LongTermResultPage();
    addLongTermLoan = new AddLongTermLoan();
    guarantorPage = new GuarantorPage();
    softAssert = new SoftAssert();
    dbMTEST = new DbMTEST();
    String cspUser = excelUtil.getCellData("LoanDetail", 37, 1).trim();
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
    homePage.goToLongTermLoan();
    action = new Actions(driver);
  }

  @Test(priority = 1, testName = "adding loan request")
  public void addlongtermloan() throws Exception {
    longTermResultPage.clickAddLoanButton();
    String walletExposureAmt = excelUtil.getCellData("LoanDetail", 34, 1);
    String resStability = excelUtil.getCellData("LoanDetail", 35, 1);
    String spouse = excelUtil.getCellData("LoanDetail", 36, 1);

    addLongTermLoan.enterLoanAmount("200000", walletExposureAmt, resStability, spouse);
    addLongTermLoan.DurationInMonth("3 Months");
    addLongTermLoan.SelectFrequency("Daily");
    addLongTermLoan.clickCheckbox();
    String panNumber = excelUtil.getCellData("LoanDetail", 6, 1).trim();
    String panDocPath = excelUtil.getCellData("LoanDetail", 7, 1).trim();
    String aadharNumber = excelUtil.getCellData("LoanDetail", 8, 1).trim();
    String aadharDocPath = excelUtil.getCellData("LoanDetail", 9, 1).trim();
    String chequeScanCopyDoc = excelUtil.getCellData("LoanDetail", 10, 1).trim();
    String chequeNumber = excelUtil.getCellData("LoanDetail", 11, 1).trim();
    String chequeBank = excelUtil.getCellData("LoanDetail", 12, 1).trim();
    String chequeOption = excelUtil.getCellData("LoanDetail", 13, 1).trim();
    String gstDoc = excelUtil.getCellData("LoanDetail", 14, 1).trim();
    String bankStatement = excelUtil.getCellData("LoanDetail", 15, 1).trim();
    String otherDoc = excelUtil.getCellData("LoanDetail", 16, 1).trim();
    String apprEmail = excelUtil.getCellData("LoanDetail", 17, 1).trim();
    String ledgIncome = excelUtil.getCellData("LoanDetail", 18, 1).trim();
    String shopowner = excelUtil.getCellData("LoanDetail", 19, 1).trim();
    String otherBusiness = excelUtil.getCellData("LoanDetail", 20, 1).trim();
    String operator = excelUtil.getCellData("LoanDetail", 21, 1).trim();
    String otherloan = excelUtil.getCellData("LoanDetail", 22, 1).trim();
    String cibil = excelUtil.getCellData("LoanDetail", 23, 1).trim();

    addLongTermLoan.EnterLoanDetails(panNumber, panDocPath, aadharNumber, aadharDocPath, chequeScanCopyDoc, chequeNumber, chequeBank, chequeOption, gstDoc, bankStatement, otherDoc, apprEmail, ledgIncome, shopowner, otherBusiness, operator, otherloan, cibil);
    addLongTermLoan.submitLoanRequest();
    addLongTermLoan.confirmLoanRequest();
    String loanNumber = addLongTermLoan.getLoanNumber();
    File file = new File("LongTermloanNumber.txt");
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
  }

  @Test(priority = 2, testName = "add guarantor")
  public void addGuarantor() throws IOException {
    File file = new File("LongTermloanNumber.txt");
    String loanNumber = "";

    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
      // Read the loan number from the file
      loanNumber = reader.readLine(); // Assuming the loan number is on the first line
      System.out.println("Loan number read from file: " + loanNumber);
    } catch (IOException e) {
      e.printStackTrace();
    }
    longTermResultPage.enterLoanNumber(loanNumber);
    longTermResultPage.selectLoanStatus("--Select--");
    longTermResultPage.clickView();
    longTermResultPage.clickGuarantorDetailButton();
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
