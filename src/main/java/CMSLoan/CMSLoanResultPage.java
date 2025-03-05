package CMSLoan;


import Base.BaseClassUAT2;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class CMSLoanResultPage extends BaseClassUAT2 {

  WebDriverWait wait;
  Actions action;

  @FindBy(name = "ctl00$ContentPlaceHolder1$btnAddLaonRequest")
  WebElement addLoanButton;

  @FindBy(name = "ctl00$ContentPlaceHolder1$txtLoanNo")
  WebElement loanNoTxtBox;

  @FindBy(name = "ctl00$ContentPlaceHolder1$btnView")
  WebElement viewButton;

  @FindBy(name = "ctl00$ContentPlaceHolder1$grdList$ctl02$btnViewDetails")
  WebElement btnViewDetails;

  @FindBy(xpath = "//tr[@class='dgrow1']//a[contains(.,'View')]")
  WebElement guarantor;

  @FindBy(name = "ctl00$ContentPlaceHolder1$ddlSearchLoanStatus")
  WebElement ddlSearchLoanStatus;


  public CMSLoanResultPage() {
    PageFactory.initElements(driver, this);
    wait = new WebDriverWait(driver, Duration.ofSeconds(60));
    action = new Actions(driver);
  }

  public void clickAddButton() {
    wait.until(ExpectedConditions.visibilityOf(addLoanButton)).click();
  }

  public void enterLoanNumber(String loannumber) {
    wait.until(ExpectedConditions.visibilityOf(loanNoTxtBox));
    loanNoTxtBox.clear();
    loanNoTxtBox.sendKeys(loannumber);
  }

  public void clickViewButton() {
    viewButton.click();
  }

  public void btnViewDetails() {
    wait.until(ExpectedConditions.visibilityOf(btnViewDetails)).click();

  }

  public void clickGuarantorDetailButton() {
    wait.until(ExpectedConditions.visibilityOf(guarantor)).click();
  }

  public void selectLoanStatus(String status) {
    Select select = new Select(ddlSearchLoanStatus);
    select.selectByVisibleText(status);
  }
}
