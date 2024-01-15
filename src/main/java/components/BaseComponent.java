package components;

import org.openqa.selenium.WebDriver;
import pages.AbsPageObject;

public abstract class BaseComponent<T> extends AbsPageObject {
  public BaseComponent(WebDriver driver) {
    super(driver);
  }
}
