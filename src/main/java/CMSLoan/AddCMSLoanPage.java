package CMSLoan;


import Base.BaseClassUAT2;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddCMSLoanPage extends BaseClassUAT2 {

  WebDriverWait wait;
  Actions action;

  @FindBy(name = "ctl00$ContentPlaceHolder1$txtSpouseName")
  WebElement txtSpouseName;

  @FindBy(name = "ctl00$ContentPlaceHolder1$txtLoanAmountReq")
  WebElement txtLoanAmountReq;

  @FindBy(name = "ctl00$ContentPlaceHolder1$fileUploadGSTStmt")
  WebElement fileUploadGSTStmt;

  @FindBy(name = "ctl00$ContentPlaceHolder1$fileUploadBankStmt")
  WebElement fileUploadBankStmt;

  @FindBy(name = "ctl00$ContentPlaceHolder1$fileUploadOtherDocs")
  WebElement fileUploadOtherDocs;

  @FindBy(name = "ctl00$ContentPlaceHolder1$fileChequeScanCopy")
  WebElement fileChequeScanCopy;

  @FindBy(name = "ctl00$ContentPlaceHolder1$txtChequeNos")
  WebElement txtChequeNos;

  @FindBy(name = "ctl00$ContentPlaceHolder1$txtChequeBank")
  WebElement txtChequeBank;

  @FindBy(name = "ctl00$ContentPlaceHolder1$ddlChequeOf")
  WebElement ddlChequeOf;

  @FindBy(name = "ctl00$ContentPlaceHolder1$fileUploadApprEmail")
  WebElement fileUploadApprEmail;

  @FindBy(name = "ctl00$ContentPlaceHolder1$txtPANNo")
  WebElement txtPANNo;

  @FindBy(name = "ctl00$ContentPlaceHolder1$fileUploadPAN")
  WebElement fileUploadPAN;

  @FindBy(name = "ctl00$ContentPlaceHolder1$txtAadharNo")
  WebElement txtAadharNo;

  @FindBy(name = "ctl00$ContentPlaceHolder1$fileUploadAadhar")
  WebElement fileUploadAadhar;

  @FindBy(name = "ctl00$ContentPlaceHolder1$fileUploadCIBIL")
  WebElement fileUploadCIBIL;

  @FindBy(name = "ctl00$ContentPlaceHolder1$btnSave")
  WebElement btnSave;

  @FindBy(name = "ctl00$ContentPlaceHolder1$txtPenaltyAmount")
  WebElement txtPenaltyAmount;

  @FindBy(name = "ctl00$ContentPlaceHolder1$txtLoanTenureDays")
  WebElement txtLoanTenureDays;

  @FindBy(name = "ctl00$ContentPlaceHolder1$txtInterestRate")
  WebElement txtInterestRate;


  @FindBy(name = "ctl00$ContentPlaceHolder1$txtProcessingFee")
  WebElement txtProcessingFee;

  @FindBy(name = "ctl00$ContentPlaceHolder1$txtInsuranceFee")
  WebElement txtInsuranceFee;

  @FindBy(name = "ctl00$ContentPlaceHolder1$txtAgreementFee")
  WebElement txtAgreementFee;

  @FindBy(name = "ctl00$ContentPlaceHolder1$ddlLoanStatus")
  WebElement ddlLoanStatus;

  @FindBy(name = "ctl00$ContentPlaceHolder1$ddlGuarantorStatus")
  WebElement ddlGuarantorStatus;

  @FindBy(name = "ctl00$ContentPlaceHolder1$chkGuarantor")
  WebElement chkGuarantor;

  @FindBy(name = "ctl00$ContentPlaceHolder1$txtResidenceStability")
  WebElement txtResidenceStability;

  @FindBy(name = "ctl00$ContentPlaceHolder1$txtTotalLoanExposure")
  WebElement txtTotalLoanExposure;


  public AddCMSLoanPage() {
    PageFactory.initElements(driver, this);
    wait = new WebDriverWait(driver, Duration.ofSeconds(60));
    action = new Actions(driver);
  }

  public void fillLoanDetails(String spouseName, String stability, String exposure, String loanAmt, String tenureDays, String gstFile
        , String bankstmt, String otherDoc, String chequeScanCopy, String chequeNo, String chequeBank,
                              String chequeOf
        , String apprEmailFile, String panNo, String panFile, String aadharNo, String aadharFile
        , String cibilFile) {
    wait.until(ExpectedConditions.visibilityOf(txtSpouseName)).sendKeys(spouseName);
    txtResidenceStability.sendKeys(stability);
    txtTotalLoanExposure.sendKeys(exposure);
    txtLoanAmountReq.sendKeys(loanAmt);
    action.sendKeys(Keys.TAB).perform();
    wait.until(ExpectedConditions.visibilityOf(txtLoanTenureDays));

    wait.until(ExpectedConditions.visibilityOf(txtPenaltyAmount));
    txtLoanTenureDays.sendKeys(tenureDays);
    action.moveToElement(ddlChequeOf).perform();
    Select select = new Select(ddlChequeOf);
    select.selectByVisibleText(chequeOf);
    wait.until(ExpectedConditions.visibilityOf(fileUploadGSTStmt));
    fileUploadGSTStmt.sendKeys(gstFile);
    fileUploadBankStmt.sendKeys(bankstmt);
    fileUploadOtherDocs.sendKeys(otherDoc);
    fileChequeScanCopy.sendKeys(chequeScanCopy);
    txtChequeNos.sendKeys(chequeNo);
    txtChequeBank.sendKeys(chequeBank);

    fileUploadApprEmail.sendKeys(apprEmailFile);
    txtPANNo.sendKeys(panNo);
    fileUploadPAN.sendKeys(panFile);
    txtAadharNo.sendKeys(aadharNo);
    fileUploadAadhar.sendKeys(aadharFile);
    fileUploadCIBIL.sendKeys(cibilFile);
  }

  public void clickSaveButton() {
    btnSave.click();
  }

  public String getLoanNumber() throws Exception {
    wait.until(ExpectedConditions.alertIsPresent());
    String alertTxt = driver.switchTo().alert().getText();

    String pattern = "RO\\w+";

    // Create a Pattern object
    Pattern r = Pattern.compile(pattern);

    // Create a matcher object
    Matcher m = r.matcher(alertTxt);
    String loanNumber = "";
    // Check if the pattern matches and extract the loan number
    if (m.find()) {
      // Group 1 will contain the loan number
      loanNumber = m.group();
    } else {
      throw new Exception("catching exception for loan number.");
    }

    driver.switchTo().alert().accept();
    return loanNumber;
  }

  public Map<String, String> getAllValues() {
    Map<String, String> allValue = new HashMap<>();
    allValue.put("spouseName", txtSpouseName.getAttribute("value"));
    allValue.put("loanAmount", txtLoanAmountReq.getAttribute("value"));
    allValue.put("loanTenure", txtLoanTenureDays.getAttribute("value"));
    allValue.put("interestRate", txtInterestRate.getAttribute("value"));
    allValue.put("processingFee", txtProcessingFee.getAttribute("value"));
    allValue.put("insuranceFee", txtInsuranceFee.getAttribute("value"));
    allValue.put("penaltyAmount", txtPenaltyAmount.getAttribute("value"));
    allValue.put("agreementFee", txtAgreementFee.getAttribute("value"));
    allValue.put("chequeNos", txtChequeNos.getAttribute("value"));
    allValue.put("chequeBank", txtChequeBank.getAttribute("value"));
    Select select = new Select(ddlChequeOf);
    WebElement option = select.getFirstSelectedOption();
    String defaultItem = option.getText();
    System.out.println(defaultItem);
    allValue.put("ddlChequeOf", defaultItem);
    allValue.put("txtPANNo", txtPANNo.getAttribute("value"));
    allValue.put("txtAadharNo", txtAadharNo.getAttribute("value"));


    return allValue;
  }

  public void changeLoanStatus(String loanStatus) {
    wait.until(ExpectedConditions.visibilityOf(ddlLoanStatus));
    Select select = new Select(ddlLoanStatus);
    select.selectByVisibleText(loanStatus);
  }

  public void changeGuarantorStatus(String guarantorStatus) {
    wait.until(ExpectedConditions.visibilityOf(ddlGuarantorStatus));
    Select select = new Select(ddlGuarantorStatus);
    select.selectByVisibleText(guarantorStatus);
  }

  public void checkMandatoryGuarantor() {
    wait.until(ExpectedConditions.visibilityOf(chkGuarantor));
    action.moveToElement(chkGuarantor).perform();
    chkGuarantor.click();
  }
}
