package pages;

import org.openqa.selenium.WebDriver;
import utils.AbsBaseUtils;

public abstract class AbsBasePage<T> extends AbsBaseUtils {

  public AbsBasePage(WebDriver driver) {
    super(driver);
  }

  public T open(){

    return (T)this;
  }
}
