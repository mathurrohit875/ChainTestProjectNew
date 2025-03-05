package OtherProductLoanTest;


import Base.BaseClassUAT2;
import Base.DbMTEST;
import OtherProductsLoan.AddOtherProductLoan;
import OtherProductsLoan.OtherProductLoanResultPage;
import Pages.HomePage;
import Pages.LoginPage;
import Utility.ExcelUtil;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.HashMap;
import java.util.Map;

public class ApplyThreeMonthOtherLoanTest extends BaseClassUAT2 {

  HomePage homePage;
  LoginPage loginPage;

  SoftAssert softAssert;
  Actions action;
  ExcelUtil excelUtil;
  DbMTEST dbMTEST;
  AddOtherProductLoan addOtherProductLoan;
  OtherProductLoanResultPage otherProductLoanResultPage;
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
    softAssert = new SoftAssert();
    loginPage.login("CSP226450", "roinet@1234", "KMJKN");
    loginPage.Login_With_OTP("222111");
    homePage.ClickonWALLET();
    homePage.goToOtherProductLoan();
    action = new Actions(driver);
    dbMTEST = new DbMTEST();
  }

  @Test(priority = 1, testName = "adding loan request")
  public void addlongtermloan() throws IOException {
    otherProductLoanResultPage.clickAddLoanButton();
    String walletExposureAmt = excelUtil.getCellData("LoanDetail", 34, 1);
    String resStability = excelUtil.getCellData("LoanDetail", 35, 1);
    String spouse = excelUtil.getCellData("LoanDetail", 36, 1);

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
  }

  @Test(priority = 2, testName = "Verify detail in the Db.")
  public void verifyDataInDataBase() throws Exception {
    addOtherProductLoan.confirmLoanRequest();
    String loanNumber = addOtherProductLoan.getLoanNumber().trim();
    loan.put("loanNumber", loanNumber);
    addOtherProductLoan.acceptLoanRequest();
    String checkLoan = "select * from tm_channelemi where loanno='" + loanNumber + "'";
    System.out.println("loan query: " + checkLoan);
    ResultSet resultSet = dbMTEST.executeQuery(checkLoan);
    // Get metadata to get the number of columns
    ResultSetMetaData metaData = resultSet.getMetaData();
    int columnCount = metaData.getColumnCount();

    while (resultSet.next()) {

      // Iterate through all columns dynamically
      for (int i = 1; i <= columnCount; i++) {
        // Get the column name
        String columnName = metaData.getColumnName(i);
        // Get the value of the column as a String (you can get it in other types depending on the column)
        String columnValue = resultSet.getString(i);
        double loanamtexpected = Double.parseDouble(resultSet.getString("loanamt"));
        double loanamtactual = 20000.00;
        softAssert.assertEquals(loanamtactual, loanamtexpected, "loan amount mismatch in the Database.");
        int durationinmonthexpected = resultSet.getInt("durationinmonth");
        int durationinmonthactual = 3;
        softAssert.assertEquals(durationinmonthactual, durationinmonthexpected, "duration in month does not match");
        double loaninterestrateexpected = Double.parseDouble(resultSet.getString("loaninterestrate"));
        double loaninterestrateactual = 0.24;
        softAssert.assertEquals(loaninterestrateactual, loaninterestrateexpected, "loan interest rate does not match");
        String emifrequencyexpected = resultSet.getString("emifrequency");
        String emifrequencyactual = "D";
        //softAssert.assertEquals(emifrequencyactual, emifrequencyexpected, "emi frequency does not match.");
        String emitypeexpected = resultSet.getString("emitype");
        String emitypeactual = "F";
        softAssert.assertEquals(emitypeactual, emitypeexpected,
              "emi type does not match.");
        int totalnoofemiexpected = resultSet.getInt("totalnoofemi");
        int totalnoofemiactual = 75;
        /*softAssert.assertEquals(totalnoofemiexpected, totalnoofemiactual,
              "total number of emi does not match.");*/
        String gststatementpathexpected = resultSet.getString("gststatementpath");
        softAssert.assertEquals(true, gststatementpathexpected != null,
              "file not updated in gst statement");
        String bankstatementpathexpected = resultSet.getString("bankstatementpath");
        softAssert.assertEquals(true, bankstatementpathexpected != null,
              "file not updated in bank statement");
        String otherdocspathexpected = resultSet.getString("otherdocspath");
        softAssert.assertEquals(true, otherdocspathexpected != null,
              "file not updated in other doc ");
        String chequenosexpected = resultSet.getString("chequenos");
        String chequenosactual = excelUtil.getCellData("OtherProductLoan", 11, 1).trim();
        softAssert.assertEquals(chequenosexpected, chequenosactual,
              "cheque number does not match.");
        String chequebankexpected = resultSet.getString("chequebank");
        String chequebankactual = excelUtil.getCellData("OtherProductLoan", 12, 1).trim();
        softAssert.assertEquals(chequebankactual, chequebankexpected,
              "cheque bank does not match");
        String panpathexpected = resultSet.getString("panpath");
        softAssert.assertEquals(true, panpathexpected != null,
              "file not update in pan doc");
        String aadharpathexpected = resultSet.getString("aadharpath");
        softAssert.assertEquals(true, aadharpathexpected != null,
              "file not updated in aadhar doc");
        String cibilpathexpected = resultSet.getString("cibilpath");
        softAssert.assertEquals(true, cibilpathexpected != null,
              "file not updated in cibil doc");
        String emailpathexpected = resultSet.getString("emailpath");
        softAssert.assertEquals(true, emailpathexpected != null,
              "file not updated in email doc");
        String pannoexpected = resultSet.getString("panno");
        String pannoactual = excelUtil.getCellData("OtherProductLoan", 6, 1).trim();
        softAssert.assertEquals(pannoactual, pannoexpected, "pan number does not match.");
        String aadharnoexpected = resultSet.getString("aadharno");
        String aadharnoactual = excelUtil.getCellData("OtherProductLoan", 8, 1).trim();
        softAssert.assertEquals(aadharnoactual, aadharnoexpected, "aadhar number does not match");
        String ledgerincomefromcompexpected = resultSet.getString("ledgerincomefromcomp");
        softAssert.assertEquals(true, ledgerincomefromcompexpected != null,
              "file not updated in ledgerincome doc");
        String shopownerproofexpected = resultSet.getString("shopownerproof");
        softAssert.assertEquals(true, shopownerproofexpected != null,
              "file not updated in shopownerproof doc");
        String otherbusinessdetailexpected = resultSet.getString("otherbusinessdetail");
        String otherbusinessdetailactual = excelUtil.getCellData("OtherProductLoan", 20, 1).trim();
        softAssert.assertEquals(otherbusinessdetailactual, otherbusinessdetailexpected, "other business does not match");

        String operatorworkwithexpected = resultSet.getString("operatorworkwith");
        String operatorworkwithactual = excelUtil.getCellData("OtherProductLoan", 21, 1).trim();
        softAssert.assertEquals(operatorworkwithactual, operatorworkwithexpected, "operator work does not match");

        String otherloanfromcompexpected = resultSet.getString("otherloanfromcomp");
        String otherloanfromcompactual = excelUtil.getCellData("OtherProductLoan", 22, 1).trim();
        softAssert.assertEquals(otherloanfromcompactual, otherloanfromcompexpected, "Other loan does not match.");
        softAssert.assertAll();

      }
      System.out.println("------------");
    }
  }

  @Test(priority = 3, testName = "verify result grid data")
  public void verifyDataResultGrid() throws IOException {
    //longTermResultPage.moveToLoanNumber();
    //String usercode=longTermResultPage.getUsercode();
    //System.out.println("usecode: "+usercode);
    Map<String, String> fieldsText = otherProductLoanResultPage.getDataResultGrid();
    String usercodeData = fieldsText.get("usercodeData");
    String usernameData = fieldsText.get("usernameData");
    String loanTermData = fieldsText.get("loanTermData");
    softAssert.assertEquals(excelUtil.getCellData("OtherProductLoan", 26, 1), loanTermData, "loan term did not match.");

    String loanPenaltyData = fieldsText.get("loanPenaltyData");
    softAssert.assertEquals(excelUtil.getCellData("OtherProductLoan", 25, 1), loanPenaltyData, "loan penalty did not match.");

    String loanRepayTimeData = fieldsText.get("loanRepayTimeData");
    String loanTenureDaysData = fieldsText.get("loanTenureDaysData");
    String loanNoData = fieldsText.get("loanNoData");
    softAssert.assertEquals(loan.get("loanNumber"), loanNoData, "loan number did not match.");
    String loanAmountData = fieldsText.get("loanAmountData");
    softAssert.assertEquals(excelUtil.getCellData("OtherProductLoan", 24, 1), loanAmountData, "loan amount did not match.");
    String fileChargeData = fieldsText.get("fileChargeData");
    String fileProcessChargeData = fieldsText.get("fileProcessChargeData");
    String durationInMonthData = fieldsText.get("durationInMonthData");
    softAssert.assertEquals(excelUtil.getCellData("OtherProductLoan", 27, 1), durationInMonthData, "duration in month did not match.");
    String loanInterestRateData = fieldsText.get("loanInterestRateData");
    softAssert.assertEquals(excelUtil.getCellData("OtherProductLoan", 1, 1), loanInterestRateData, "interest rate did not match.");
    String emiFrequencyData = fieldsText.get("emiFrequencyData");
    softAssert.assertEquals(excelUtil.getCellData("OtherProductLoan", 28, 1), emiFrequencyData, "emi freq does not match.");
    String emiTypeData = fieldsText.get("emiTypeData");
    softAssert.assertEquals(excelUtil.getCellData("OtherProductLoan", 29, 1), emiTypeData, "emi type does not match.");
    String totalPaidAmountData = fieldsText.get("totalPaidAmountData");
    String balanceAmountData = fieldsText.get("balanceAmountData");
    String numberOfDueEmisData = fieldsText.get("numberOfDueEmisData");
    softAssert.assertEquals(excelUtil.getCellData("OtherProductLoan", 30, 1), numberOfDueEmisData, "number of emi does not match.");
    String nextDueDateData = fieldsText.get("nextDueDateData");
    String approvedByData = fieldsText.get("approvedByData");
    String approvedRemarksData = fieldsText.get("approvedRemarksData");
    String approvedDateData = fieldsText.get("approvedDateData");
    String remarksData = fieldsText.get("remarksData");
    String loanStatusData = fieldsText.get("loanStatusData");
    softAssert.assertEquals(excelUtil.getCellData("OtherProductLoan", 31, 1), loanStatusData, "loan status does not match.");
    String gstStatementData = fieldsText.get("gstStatementData");
    softAssert.assertEquals(excelUtil.getCellData("OtherProductLoan", 32, 1), gstStatementData, "gst uploaded.");
    String bankStatementData = fieldsText.get("bankStatementData");
    softAssert.assertEquals(excelUtil.getCellData("OtherProductLoan", 32, 1), bankStatementData, "bank statement uploaded.");
    String otherDocsData = fieldsText.get("otherDocsData");
    softAssert.assertEquals(excelUtil.getCellData("OtherProductLoan", 32, 1), otherDocsData, "other doc uploaded.");
    String panCardData = fieldsText.get("panCardData");
    softAssert.assertEquals(excelUtil.getCellData("OtherProductLoan", 6, 1).trim(), panCardData, "pan card data does not match.");
    String gstNumberData = fieldsText.get("gstNumberData");
    String aadharData = fieldsText.get("aadharData");
    softAssert.assertEquals(excelUtil.getCellData("OtherProductLoan", 8, 1).trim(), aadharData, "aadhar number does not match.");
    String panCardDocData = fieldsText.get("panCardDocData");
    softAssert.assertEquals(excelUtil.getCellData("OtherProductLoan", 32, 1), panCardDocData, "panCardDocData doc uploaded.");
    String aadharDocData = fieldsText.get("aadharDocData");
    softAssert.assertEquals(excelUtil.getCellData("OtherProductLoan", 32, 1), aadharDocData, "aadharDocData doc uploaded.");
    String cibilDocData = fieldsText.get("cibilDocData");
    softAssert.assertEquals(excelUtil.getCellData("OtherProductLoan", 32, 1), cibilDocData, "cibilDocData doc uploaded.");
    String emailDocData = fieldsText.get("emailDocData");
    softAssert.assertEquals(excelUtil.getCellData("OtherProductLoan", 32, 1), emailDocData, "emailDocData doc uploaded.");
    softAssert.assertAll();
    String eMandateData = fieldsText.get("eMandateData");
    String eSignData = fieldsText.get("eSignData");
    String arthmateSignData = fieldsText.get("arthmateSignData");
    String getLoanFromArthmateData = fieldsText.get("getLoanFromArthmateData");
    String viewDetailsData = fieldsText.get("viewDetailsData");
    String reviewReportData = fieldsText.get("reviewReportData");
    String foreClosureData = fieldsText.get("foreClosureData");
    String emailDocPDFData = fieldsText.get("emailDocPDFData");
    String defaultClosureData = fieldsText.get("defaultClosureData");
  }

}
