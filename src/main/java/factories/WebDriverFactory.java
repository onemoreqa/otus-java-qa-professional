package factories;

import exceptions.BrowserNotSupportedException;
import factories.impl.ChromeConfigure;
import factories.impl.IBrowserSettings;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class WebDriverFactory {

  public final long implicitlyWaitSecond = Integer.parseInt(System.getProperty("webdriver.timeouts.implicitlywait", "5"));
  public final long pageLoadTimeout = Integer.parseInt(System.getProperty("webdriver.timeouts.pageloadtimeout", "30"));

  public WebDriver getWebDriver() {
    return getWebDriver(System.getProperty("browser", "chrome").toLowerCase(Locale.ROOT));
  }

  public WebDriver getWebDriver(String browserName) {
    WebDriver driver = getWebDriverWithoutSettings(browserName);
    driver.manage().timeouts().implicitlyWait(implicitlyWaitSecond, TimeUnit.SECONDS);
    driver.manage().timeouts().pageLoadTimeout(pageLoadTimeout, TimeUnit.SECONDS);
    return driver;
  }

  public WebDriver getWebDriverWithoutSettings(String browserName) {
    switch (browserName) {
      case "chrome":
        IBrowserSettings chromeSettings = new ChromeConfigure();
        WebDriverManager.chromedriver().setup();

        return new ChromeDriver((ChromeOptions) chromeSettings.conigure());
      case "firefox":
        IBrowserSettings firefoxSettings = new ChromeConfigure();
        WebDriverManager.firefoxdriver().setup();
        return new FirefoxDriver(/*(ChromeOptions) firefoxSettings.getCapabilities()*/);
      default:
        throw new BrowserNotSupportedException(browserName);
    }
  }
}
