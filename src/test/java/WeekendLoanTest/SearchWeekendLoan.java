package WeekendLoanTest;

import Base.BaseClassUAT2;
import Base.DbMTEST;
import Base.GuarantorPage;
import Pages.HomePage;
import Pages.LoginPage;
import Utility.ExcelUtil;
import WeekendLoan.AddWeekendLoanPage;
import WeekendLoan.WeekendLoanResultPage;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class SearchWeekendLoan extends BaseClassUAT2 {

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
    String excelPath = "src/main/java/data/LendingData.xlsx";
    Browserintialize("chrome", "https://uatxpresso.roinet.in/Login.aspx");

    excelUtil = new ExcelUtil(excelPath);
    homePage = new HomePage();
    loginPage = new LoginPage();
    weekendLoanResultPage = new WeekendLoanResultPage();
    addWeekendLoanPage = new AddWeekendLoanPage();
    guarantorPage = new GuarantorPage();
    softAssert = new SoftAssert();
    dbMTEST = new DbMTEST();
    String cspUser = excelUtil.getCellData("WeekendLoan", 16, 1).trim();
    String getChannelId = "select userid from tm_user where usercode='" + cspUser + "'";

    ResultSet rs = dbMTEST.executeQuery(getChannelId);
    String channelId = "";
    while (rs.next()) {
      channelId = rs.getString("userid");
    }
    String updateWalletBalance = "update tm_channel set availablelimit=100.00 where channelid=" + channelId;
    dbMTEST.executeQuery(updateWalletBalance);
    loginPage.login("MONA.SHARMA", "roinet@1234", "KMJKN");
    loginPage.Login_With_OTP("222111");
    homePage.ClickonWALLET();
    homePage.goToWeekendLoan("MONA.SHARMA");
    action = new Actions(driver);

  }
  @Test
  public void test(){
    weekendLoanResultPage.enterLoanNumber("ROIE05498");
    weekendLoanResultPage.selectLoanStatus("--All--");
    weekendLoanResultPage.clickViewButton();
    weekendLoanResultPage.btnViewDetails();
  }
}
