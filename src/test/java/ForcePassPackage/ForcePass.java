package ForcePassPackage;

import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class ForcePass {

  @Test
  public void pass() {
    SoftAssert softAssert = new SoftAssert();
    String str = "hello";
    String str2 = "hello";
    softAssert.assertEquals(str, str2);
    softAssert.assertAll();
  }
}
