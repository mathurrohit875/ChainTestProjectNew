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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class OtherProductGuarantorView extends BaseClassUAT2 {
  HomePage homePage;
  LoginPage loginPage;

  SoftAssert softAssert;
  Actions action;
  ExcelUtil excelUtil;
  DbMTEST dbMTEST;
  OtherProductLoanResultPage otherProductLoanResultPage;
  AddOtherProductLoan addOtherProductLoan;
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
    otherProductLoanResultPage = new OtherProductLoanResultPage();
    addOtherProductLoan = new AddOtherProductLoan();
    softAssert = new SoftAssert();
    dbMTEST = new DbMTEST();

    guarantorPage = new GuarantorPage();
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
  public void addGuarantorDetail() throws IOException, SQLException {
    File file = new File("shortTermLoanWithGuarantor.txt");


    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
      // Read the loan number from the file
      loanNumber = reader.readLine(); // Assuming the loan number is on the first line
      System.out.println("Loan number read from file: " + loanNumber);
    } catch (IOException e) {
      e.printStackTrace();
    }
    otherProductLoanResultPage.enterLoanNumber("ROIF05541");
    otherProductLoanResultPage.selectLoanStatus("--Select--");
    otherProductLoanResultPage.clickViewButton();
    otherProductLoanResultPage.clickGuarantorDetailButton("ROIF05541");

    String shortTermResult = driver.getWindowHandle();
    Set<String> windowSet = driver.getWindowHandles();
    Iterator<String> i = windowSet.iterator();
    while (i.hasNext()) {
      String guarantorWindow = i.next();
      if (!shortTermResult.equals(guarantorWindow)) {
        driver.switchTo().window(guarantorWindow);
        //otherProductGuarantor.getText();

        String query = "select * from tm_loanguarantor where loanno='ROIF05541'";
        Map<String, String> guarantorDetail = guarantorPage.getText();

        ResultSet rs = dbMTEST.executeQuery(query);
        String loanno = "", emiid = "", loanamt = "", addendumnumber = "", guarantorname = "", guarantormobileno = "",
              customerrelation = "", customerknownyear = "", guarantoroccupation = "",
              guarantoraadharno = "", guarantorpan = "", guarantorpandoc = "",
              guarantoraadhardoc = "", bankstatementdoc = "", otherrelativepartnerloandeatils = "",
              createddate = "", createdby = "", lastupdatedate = "", lastupdateby = "", lastactive = "",
              stateid = "", districtid = "", address = "", pincode = "", emailid = "", dob = "", gender = "",
              panresponse = "", panverficationstatus = "", panverifyby = "", panverifydate = "";
        while (rs.next()) {
          loanno = rs.getString("loanno");
          emiid = rs.getString("emiid");
          loanamt = rs.getString("loanamt");
          addendumnumber = rs.getString("addendumnumber");
          guarantorname = rs.getString("guarantorname");
          guarantormobileno = rs.getString("guarantormobileno");
          customerrelation = rs.getString("customerrelation");
          customerknownyear = rs.getString("customerknownyear");
          guarantoroccupation = rs.getString("guarantoroccupation");
          guarantoraadharno = rs.getString("guarantoraadharno");
          guarantorpan = rs.getString("guarantorpan");
          guarantorpandoc = rs.getString("guarantorpandoc");
          guarantoraadhardoc = rs.getString("guarantoraadhardoc");
          bankstatementdoc = rs.getString("bankstatementdoc");
          otherrelativepartnerloandeatils = rs.getString("otherrelativepartnerloandeatils");
          createddate = rs.getString("createddate");
          createdby = rs.getString("createdby");
          lastupdatedate = rs.getString("lastupdatedate");
          lastupdateby = rs.getString("lastupdateby");
          lastactive = rs.getString("lastactive");
          stateid = rs.getString("stateid");
          districtid = rs.getString("districtid");
          address = rs.getString("address");
          pincode = rs.getString("pincode");
          emailid = rs.getString("emailid");
          dob = rs.getString("dob");
          gender = rs.getString("gender");
          panresponse = rs.getString("panresponse");
          panverficationstatus = rs.getString("panverficationstatus");
          panverifyby = rs.getString("panverifyby");
          panverifydate = rs.getString("panverifydate");

        }
        System.out.println("Wallet Advance No: " + guarantorDetail.get("Wallet Advance No"));
        System.out.println("Wallet Advance Amount: " + guarantorDetail.get("Wallet Advance Amount"));
        System.out.println("Name: " + guarantorDetail.get("Name"));
        System.out.println("DOB: " + guarantorDetail.get("DOB"));
        System.out.println("Gender: " + guarantorDetail.get("Gender"));
        System.out.println("Mobile No: " + guarantorDetail.get("Mobile No"));
        System.out.println("Email-Id: " + guarantorDetail.get("Email-Id"));
        System.out.println("Customer Relation: " + guarantorDetail.get("Customer Relation"));
        System.out.println("Customer Known Year: " + guarantorDetail.get("Customer Known Year"));
        System.out.println("State: " + guarantorDetail.get("State"));
        System.out.println("District: " + guarantorDetail.get("District"));
        System.out.println("Address: " + guarantorDetail.get("Address"));
        System.out.println("Pincode: " + guarantorDetail.get("Pincode"));
        System.out.println("Guarantor Aadhar No.: " + guarantorDetail.get("Guarantor Aadhar No."));
        System.out.println("Guarantor Pan: " + guarantorDetail.get("Guarantor Pan"));
        System.out.println("Occupation: " + guarantorDetail.get("Occupation"));
        System.out.println("Active Partner Wallet Advance: " + guarantorDetail.get("Active Partner Wallet Advance"));
        System.out.println("Create Date: " + guarantorDetail.get("Create Date"));
        System.out.println("PAN: " + guarantorDetail.get("PAN"));
        System.out.println("Aadhar: " + guarantorDetail.get("Aadhar"));
        System.out.println("Bank Statement: " + guarantorDetail.get("Bank Statement"));

        softAssert.assertEquals(loanno, guarantorDetail.get("Wallet Advance No"));
        softAssert.assertEquals(loanamt, guarantorDetail.get("Wallet Advance Amount"));
        softAssert.assertEquals(guarantorname, guarantorDetail.get("Name"));
        softAssert.assertEquals(guarantormobileno, guarantorDetail.get("Mobile No"));
        softAssert.assertEquals(customerrelation, guarantorDetail.get("Customer Relation"));
        softAssert.assertEquals(customerknownyear, guarantorDetail.get("Customer Known Year"));
        softAssert.assertEquals(guarantoroccupation, guarantorDetail.get("Occupation"));
        softAssert.assertEquals(guarantoraadharno, guarantorDetail.get("Guarantor Aadhar No."));
        softAssert.assertEquals(guarantorpan, guarantorDetail.get("Guarantor Pan"));
        //softAssert.assertEquals(guarantorpandoc, "");
        //softAssert.assertEquals(guarantoraadhardoc, "");
        //softAssert.assertEquals(bankstatementdoc, "");
        softAssert.assertEquals(otherrelativepartnerloandeatils, guarantorDetail.get("Active Partner Wallet Advance"));
        softAssert.assertEquals(createdby, "CSP244749");

        softAssert.assertEquals(pincode, guarantorDetail.get("Pincode"));
        softAssert.assertEquals(emailid, guarantorDetail.get("Email-Id"));
        //softAssert.assertEquals(dob, "");
        softAssert.assertEquals(gender, "M");
        softAssert.assertAll();


      }
    }
    //driver.switchTo().window(shortTermResult);
  }

  /*@AfterClass
  public void quit(){
    driver.quit();

  }*/
}
