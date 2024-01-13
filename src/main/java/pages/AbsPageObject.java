package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import waiter.BaseWaiter;

public abstract class AbsPageObject {

  protected WebDriver driver;
  protected Actions actions;
  protected BaseWaiter baseWaiter;

  public AbsPageObject(WebDriver driver) {
    this.driver = driver;
    this.actions = new Actions(driver);
    PageFactory.initElements(driver, this);
    baseWaiter = new BaseWaiter(driver);
  }
}
