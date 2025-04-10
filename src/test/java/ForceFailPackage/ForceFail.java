package ForceFailPackage;

import Base.BaseClassUAT2;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class ForceFail extends BaseClassUAT2 {

  @Test(testName = "test failed")
  public void fail() {
    SoftAssert softAssert = new SoftAssert();
    String str = "force fail";
    String str2 = "hello";
    softAssert.assertEquals(str, str2, "the string is failed");
    softAssert.assertAll();
  }
}
