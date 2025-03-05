package OtherProductLoanTest;

import Base.BaseClassUAT2;
import Base.ChainTestListener;
import Base.DbMTEST;
import Base.GuarantorPage;
import OtherProductsLoan.AddOtherProductLoan;
import OtherProductsLoan.OtherProductLoanResultPage;
import Pages.HomePage;
import Pages.LoginPage;
import Utility.ExcelUtil;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class ApplyOtherProductWithGuarantorTest extends BaseClassUAT2 {


  HomePage homePage;
  LoginPage loginPage;

  SoftAssert softAssert;
  Actions action;
  ExcelUtil excelUtil;
  DbMTEST dbMTEST;
  AddOtherProductLoan addOtherProductLoan;
  OtherProductLoanResultPage otherProductLoanResultPage;
  GuarantorPage guarantorPage;
  Map<String, String> loan = new HashMap<>();

  @BeforeClass
  public void setup() throws IOException {
    String excelPath = "src/main/java/data/LendingData.xlsx";
    Browserintialize("chrome", "https://uatxpresso.roinet.in/Login.aspx");

    excelUtil = new ExcelUtil(excelPath);
    otherProductLoanResultPage = new OtherProductLoanResultPage();
    homePage = new HomePage();
    loginPage = new LoginPage();
    addOtherProductLoan = new AddOtherProductLoan();
    guarantorPage = new GuarantorPage();
    softAssert = new SoftAssert();
    String cspUser = excelUtil.getCellData("OtherProductLoan", 34, 1);
    loginPage.login(cspUser, "roinet@1234", "KMJKN");
    loginPage.Login_With_OTP("222111");
    homePage.ClickonWALLET();
    homePage.goToOtherProductLoan();
    action = new Actions(driver);
    dbMTEST = new DbMTEST();
  }

  @Test(priority = 1, testName = "adding loan request")
  public void addlongtermloan() throws Exception {
    otherProductLoanResultPage.clickAddLoanButton();
    String walletExposureAmt = excelUtil.getCellData("OtherProductLoan", 35, 1);
    String resStability = excelUtil.getCellData("OtherProductLoan", 36, 1);
    String spouse = excelUtil.getCellData("OtherProductLoan", 37, 1);

    addOtherProductLoan.enterLoanAmount("200000", walletExposureAmt, resStability, spouse);
    addOtherProductLoan.DurationInMonth("3 Months");
    //addOtherProductLoan.SelectFrequency("Daily");
    addOtherProductLoan.clickCheckbox();
    String panNumber = excelUtil.getCellData("OtherProductLoan", 6, 1).trim();
    String panDocPath = excelUtil.getCellData("OtherProductLoan", 7, 1).trim();
    String aadharNumber = excelUtil.getCellData("OtherProductLoan", 8, 1).trim();
    String aadharDocPath = excelUtil.getCellData("OtherProductLoan", 9, 1).trim();
    String chequeScanCopyDoc = excelUtil.getCellData("OtherProductLoan", 10, 1).trim();
    String chequeNumber = excelUtil.getCellData("OtherProductLoan", 11, 1).trim();
    String chequeBank = excelUtil.getCellData("OtherProductLoan", 12, 1).trim();
    String chequeOption = excelUtil.getCellData("OtherProductLoan", 13, 1).trim();
    String gstDoc = excelUtil.getCellData("OtherProductLoan", 14, 1).trim();
    String bankStatement = excelUtil.getCellData("OtherProductLoan", 15, 1).trim();
    String otherDoc = excelUtil.getCellData("OtherProductLoan", 16, 1).trim();
    String apprEmail = excelUtil.getCellData("OtherProductLoan", 17, 1).trim();
    String ledgIncome = excelUtil.getCellData("OtherProductLoan", 18, 1).trim();
    String shopowner = excelUtil.getCellData("OtherProductLoan", 19, 1).trim();
    String otherBusiness = excelUtil.getCellData("OtherProductLoan", 20, 1).trim();
    String operator = excelUtil.getCellData("OtherProductLoan", 21, 1).trim();
    String otherloan = excelUtil.getCellData("OtherProductLoan", 22, 1).trim();
    String cibil = excelUtil.getCellData("OtherProductLoan", 23, 1).trim();
    addOtherProductLoan.EnterLoanDetails(panNumber, panDocPath, aadharNumber, aadharDocPath, chequeScanCopyDoc, chequeNumber, chequeBank, chequeOption, gstDoc, bankStatement, otherDoc, apprEmail, ledgIncome, shopowner, otherBusiness, operator, otherloan, cibil);
    addOtherProductLoan.submitLoanRequest();
    addOtherProductLoan.confirmLoanRequest();
    String loanNumber = addOtherProductLoan.getLoanNumber();
    File file = new File("OtherProductloanNumber.txt");
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
    File file = new File("OtherProductloanNumber.txt");
    String loanNumber = "";

    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
      // Read the loan number from the file
      loanNumber = reader.readLine(); // Assuming the loan number is on the first line
      System.out.println("Loan number read from file: " + loanNumber);
    } catch (IOException e) {
      e.printStackTrace();
    }
    otherProductLoanResultPage.enterLoanNumber(loanNumber);
    otherProductLoanResultPage.selectLoanStatus("--Select--");
    otherProductLoanResultPage.clickView();
    otherProductLoanResultPage.clickGuarantorDetailButton(loanNumber);
    String longTermResult = driver.getWindowHandle();
    Set<String> windowSet = driver.getWindowHandles();
    Iterator<String> i = windowSet.iterator();
    while (i.hasNext()) {
      String guarantorWindow = i.next();
      if (!longTermResult.equals(guarantorWindow)) {
        driver.switchTo().window(guarantorWindow);
        String panDocPath = excelUtil.getCellData("OtherProductLoan", 7, 1).trim();
        String aadharDocPath = excelUtil.getCellData("OtherProductLoan", 9, 1).trim();
        String bankStatement = excelUtil.getCellData("OtherProductLoan", 15, 1).trim();
        guarantorPage.enterGuarantorDetail("DELHI & NCR", "GURGAON", "Rohit Mathur", "8290336521", "rohit.mathur@roinet.in",
              "Salaried", "friend", "3", "ABCDE TOWER 10, FLAT 903, NEAR HUDA MARKET, TWIN TOWER", "123456"
              , "536350660843", "BXRPM9931K", panDocPath, aadharDocPath, bankStatement, "no", "22/07/1993", "Male");
        guarantorPage.clickSaveButton();
      }

    }
    driver.switchTo().window(longTermResult);
  }
}


