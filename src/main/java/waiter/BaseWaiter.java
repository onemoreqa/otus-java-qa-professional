package waiter;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class BaseWaiter {
  public static final long DEFAULT_WAIT_SECOND = 30;
  private WebDriver driver;

  public BaseWaiter(WebDriver driver) {
    this.driver = driver;
  }

  public boolean waitForCondition(ExpectedCondition condition) {
    WebDriverWait webDriverWait = new WebDriverWait(driver, DEFAULT_WAIT_SECOND);
    try {
      webDriverWait.until(condition);
      return true;
    } catch (Exception ex) {
      return false;
    }
  }

  public boolean waitForElementVisible(WebElement element) {
    return waitForCondition(ExpectedConditions.visibilityOf(element));
  }

  public boolean waitForElementClickable(WebElement element) {
    return waitForCondition(ExpectedConditions.elementToBeClickable(element));
  }

  public void waitForDocumentReadyState(Duration duration) {
    new WebDriverWait(driver, duration.getSeconds()).until((ExpectedCondition<Boolean>) wd ->
            ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete"));
  }
}
