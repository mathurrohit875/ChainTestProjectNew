package OtherProductLoanTest;

import Base.BaseClassUAT2;
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class AddOtherProductsGuarantorTest extends BaseClassUAT2 {
  HomePage homePage;
  LoginPage loginPage;
  SoftAssert softAssert;
  Actions action;
  ExcelUtil excelUtil;
  DbMTEST dbMTEST;
  OtherProductLoanResultPage otherProductLoanResultPage;
  AddOtherProductLoan addOtherProductLoan;
  GuarantorPage otherProductGuarantor;
  Map<String, String> loan = new HashMap<>();
  String loanNumber = "";

  @BeforeClass
  public void setup() throws IOException, SQLException {
    String excelPath = "src/main/java/data/LendingData.xlsx";
    Browserintialize("chrome", "https://uatxpresso.roinet.in/Login.aspx");

    excelUtil = new ExcelUtil(excelPath);
    homePage = new HomePage();
    loginPage = new LoginPage();
    otherProductLoanResultPage = new OtherProductLoanResultPage();
    addOtherProductLoan = new AddOtherProductLoan();
    softAssert = new SoftAssert();
    dbMTEST = new DbMTEST();

    otherProductGuarantor = new GuarantorPage();
    String cspUser = excelUtil.getCellData("OtherProductLoan", 34, 1).trim();
    //String getChannelId = "select userid from tm_user where usercode='" + cspUser + "'";

    // ResultSet rs = dbMTEST.executeQuery(getChannelId);
    // String channelId = "";
    //while (rs.next()) {
    //  channelId = rs.getString("userid");
    // }
    //String updateWalletBalance = "update tm_channel set availablelimit=100.00 where channelid=" + channelId;
    //dbMTEST.executeQuery(updateWalletBalance);
    loginPage.login(cspUser, "roinet@1234", "KMJKN");
    loginPage.Login_With_OTP("222111");
    homePage.ClickonWALLET();
    homePage.goToOtherProductLoan();
    action = new Actions(driver);

  }

  @Test(priority = 1, testName = "add guarantor detail")
  public void addGuarantorDetail() throws IOException {
    File file = new File("OtherProductLoanWithGuarantor.txt");


    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
      // Read the loan number from the file
      loanNumber = reader.readLine(); // Assuming the loan number is on the first line
      System.out.println("Loan number read from file: " + loanNumber);
    } catch (IOException e) {
      e.printStackTrace();
    }
    otherProductLoanResultPage.enterLoanNumber(loanNumber);
    otherProductLoanResultPage.selectLoanStatus("--Select--");
    otherProductLoanResultPage.clickViewButton();
    otherProductLoanResultPage.clickGuarantorDetailButton(loanNumber);
    String bankStmt = excelUtil.getCellData("OtherProductLoan", 15, 1);

    String panDoc = excelUtil.getCellData("OtherProductLoan", 7, 1);

    String aadharDoc = excelUtil.getCellData("OtherProductLoan", 14, 1);

    String otherProductResultPage = driver.getWindowHandle();
    Set<String> windowSet = driver.getWindowHandles();
    Iterator<String> i = windowSet.iterator();
    while (i.hasNext()) {
      String guarantorWindow = i.next();
      if (!otherProductResultPage.equals(guarantorWindow)) {
        driver.switchTo().window(guarantorWindow);

        otherProductGuarantor.enterGuarantorDetail("DELHI & NCR", "GURGAON", "Rohit Mathur", "8290336521", "rohit.mathur@roinet.in",
              "Salaried", "friend", "3", "ABCDE TOWER 10, FLAT 903, NEAR HUDA MARKET, TWIN TOWER", "123456"
              , "536350660843", "BXRPM6931Q", panDoc, aadharDoc, bankStmt, "no", "22/07/1993", "Male");
        otherProductGuarantor.clickSaveButton();
        acceptAlert();

      }
    }
    driver.switchTo().window(otherProductResultPage);
  }

  /*@AfterClass
  public void quit(){
    driver.quit();

  }*/
}
