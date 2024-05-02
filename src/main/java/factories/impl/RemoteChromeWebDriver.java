package factories.impl;

import exceptions.BrowserNotSupportedException;
import extensions.UIExtensions;
import io.github.bonigarcia.wdm.config.DriverManagerType;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.RemoteWebDriver;
import utils.AllureMethods;

import java.util.logging.Level;

public class RemoteChromeWebDriver implements IDriver {

  @Override
  public WebDriver newDriver(ExtensionContext extensionContext) {
    ChromeOptions chromeOptions = new ChromeOptions();
    chromeOptions.addArguments("--no-sandbox");
    chromeOptions.addArguments("--no-first-run");
    chromeOptions.addArguments("--enable-extensions");
    chromeOptions.addArguments("--homepage=about:blank");
    chromeOptions.addArguments("--ignore-certificate-errors");
    chromeOptions.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
    chromeOptions.setCapability(CapabilityType.VERSION, System.getProperty("browser.version", "120.0"));
    chromeOptions.setCapability(CapabilityType.BROWSER_NAME, System.getProperty("browser", "chrome"));
    chromeOptions.setCapability("enableVNC", Boolean.parseBoolean(System.getProperty("enableVNC", "true")));
    //video recording settings:
    chromeOptions.setCapability("screenResolution", "1280x1024");
    chromeOptions.setCapability("enableVideo", true);
    chromeOptions.setCapability("videoScreenSize", "1280x1024");
    chromeOptions.setCapability("videoFrameRate", 12);
    //chromeOptions.setCapability("videoName", allure.getCurrentVideoName(extensionContext)); @TODO понять почему здесь не работает строка allure.getCurrentVideoName(extensionContext)
    chromeOptions.setCapability("videoName", ""
            .concat(System.getProperty("browser"))
            .concat("-")
            .concat(System.getProperty("browser.version"))
            .concat("-")
            .concat(extensionContext.getTestMethod().get().getName())
            .concat(".mp4"));
    //log settings
    //chromeOptions.setCapability("enableLog", true);
    //chromeOptions.setCapability("verbose", true);
    //chromeOptions.setCapability("log-level","DEBUG");
    //chromeOptions.setCapability("logName", selenoidVideoName.concat(".log"));
    chromeOptions.setHeadless(HEADLESS);

    LoggingPreferences logPrefs = new LoggingPreferences();
    logPrefs.enable(LogType.PERFORMANCE, Level.INFO);
    chromeOptions.setCapability(CapabilityType.LOGGING_PREFS, logPrefs);

    if (getRemoteUrl() == null) {
      try {
        downloadLocalWebDriver(DriverManagerType.CHROME);
      } catch (BrowserNotSupportedException ex) {
        ex.printStackTrace();
      }

      return new ChromeDriver(chromeOptions);
    } else
      return new RemoteWebDriver(getRemoteUrl(), chromeOptions);
  }
}
