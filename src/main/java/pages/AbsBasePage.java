package pages;

import anotations.UrlTemplate;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import java.time.Duration;
import java.util.List;

public abstract class AbsBasePage<T> extends AbsPageObject {

  public static final String BASE_URL = System.getProperty("base.url");
  public final int documentReadyStateTimeout = Integer.parseInt(System.getProperty("pageLoadTimeout", "15"));

  public AbsBasePage(WebDriver driver) {
    super(driver);
  }

  private String getPath() {
    Class clazz = getClass();
    if (clazz.isAnnotationPresent(UrlTemplate.class)) {
      return ((UrlTemplate) clazz.getAnnotation(UrlTemplate.class)).value();
    }
    return "";
  }

  @Step("Открытие главной страницы otus")
  public T open() {
    String url = BASE_URL;
    driver.get(url + getPath());
    okForAgreement();
    this.baseWaiter.waitForDocumentReadyState(Duration.ofSeconds(documentReadyStateTimeout));

    return (T)this;
  }

  @Step("Принимаем cookie")
  public void okForAgreement() {
    List<WebElement> elements = driver.findElements(By.xpath("//span[text()='Посещая наш сайт, вы принимаете']/following-sibling::div/button"));
    if (!elements.isEmpty()) {
      this.baseWaiter.waitForElementClickable(elements.get(0));
      ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", elements.get(0));
      ((JavascriptExecutor) driver).executeScript("arguments[0].click();", elements.get(0));
      PageFactory.initElements(driver, this);
    }
  }
}
