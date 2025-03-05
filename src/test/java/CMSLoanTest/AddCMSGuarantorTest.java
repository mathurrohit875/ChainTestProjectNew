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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class AddCMSGuarantorTest extends BaseClassUAT2 {
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
  String loanNumber = "";

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

    guarantorPage = new GuarantorPage();
    String cspUser = excelUtil.getCellData("CMSLoanTest", 16, 1).trim();
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
    homePage.goToCMSLoan();
    action = new Actions(driver);

  }

  @Test(priority = 1, testName = "add guarantor detail")
  public void addGuarantorDetail() throws IOException {
    File file = new File("loanNumberWithoutGuarantor.txt");


    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
      // Read the loan number from the file
      loanNumber = reader.readLine(); // Assuming the loan number is on the first line
      System.out.println("Loan number read from file: " + loanNumber);
    } catch (IOException e) {
      e.printStackTrace();
    }
    cmsLoanResultPage.enterLoanNumber("ROID05567");
    cmsLoanResultPage.selectLoanStatus("--All--");
    cmsLoanResultPage.clickViewButton();
    cmsLoanResultPage.clickGuarantorDetailButton();
    String bankStmt = excelUtil.getCellData("CMSLoanTest", 4, 1);

    String panDoc = excelUtil.getCellData("CMSLoanTest", 12, 1);

    String aadharDoc = excelUtil.getCellData("CMSLoanTest", 14, 1);

    String weekendResult = driver.getWindowHandle();
    Set<String> windowSet = driver.getWindowHandles();
    Iterator<String> i = windowSet.iterator();
    while (i.hasNext()) {
      String guarantorWindow = i.next();
      if (!weekendResult.equals(guarantorWindow)) {
        driver.switchTo().window(guarantorWindow);

        guarantorPage.enterGuarantorDetail("DELHI & NCR", "GURGAON", "Rohit Mathur", "8290336521", "rohit.mathur@roinet.in",
              "Salaried", "friend", "3", "ABCDE TOWER 10, FLAT 903, NEAR HUDA MARKET, TWIN TOWER", "123456"
              , "536350660843", "BXWPM9956K", panDoc, aadharDoc, bankStmt, "no", "22/07/1993", "Male");
        guarantorPage.clickSaveButton();
        acceptAlert();
      }
    }
    driver.switchTo().window(weekendResult);
  }

  /*@AfterClass
  public void quit(){
    driver.quit();

  }*/
}

