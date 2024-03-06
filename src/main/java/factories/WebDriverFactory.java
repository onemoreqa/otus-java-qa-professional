package factories;

import exceptions.BrowserNotSupportedException;
import factories.impl.ChromeConfigure;
import factories.impl.FirefoxConfigure;
import factories.impl.IBrowserSettings;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.github.bonigarcia.wdm.managers.FirefoxDriverManager;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URI;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class WebDriverFactory {

  public final long implicitlyWaitSecond = Integer.parseInt(System.getProperty("implicitlyWaitTimeout", "5"));
  public final long pageLoadTimeout = Integer.parseInt(System.getProperty("pageloadtimeout", "15"));

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
    if (Boolean.parseBoolean(System.getProperty("selenoid.enabled", "false"))) {
      return getRemoteWebDriver(browserName);
    } else {
      switch (browserName) {
        case "chrome":
          IBrowserSettings chromeSettings = new ChromeConfigure();
          WebDriverManager.chromedriver().setup();

          return new ChromeDriver((ChromeOptions) chromeSettings.configure());
        case "firefox":
          IBrowserSettings firefoxSettings = new FirefoxConfigure();
          WebDriverManager.firefoxdriver().setup();
          return new FirefoxDriver((FirefoxOptions) firefoxSettings.configure());
        default:
          throw new BrowserNotSupportedException(browserName);
      }
    }
  }

  public WebDriver getRemoteWebDriver(String browserName) {
    MutableCapabilities selenoidCapabilities = new DesiredCapabilities();

    Map<String, Object> selenoidOptions = new HashMap<>();
    selenoidOptions.put("browserName", browserName);
    selenoidOptions.put("browserVersion", System.getProperty("browser.version"));
    selenoidOptions.put("enableVNC", true);
    selenoidOptions.put("enableVideo", true);
    selenoidCapabilities.setCapability("selenoid:options", selenoidOptions);

    if (browserName.equals("chrome")) {
      ChromeOptions options = new ChromeOptions();
      options.addArguments("--start-fullscreen");
      selenoidCapabilities.setCapability(ChromeOptions.CAPABILITY, options);
    }

    try {
      return new RemoteWebDriver(
              URI.create(System.getProperty("selenoid.url", "http://127.0.0.1/wd/hub/")).toURL(),
              selenoidCapabilities
      );
    } catch (MalformedURLException e) {
      throw new RuntimeException("Ошибка получения URI для selenoid");
    }
  }

}
