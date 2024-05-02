package factories.impl;

import exceptions.BrowserNotSupportedException;
import io.github.bonigarcia.wdm.config.DriverManagerType;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.opera.OperaOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.RemoteWebDriver;

public class RemoteOperaWebDriver implements IDriver {

  @Override
  public WebDriver newDriver(ExtensionContext extensionContext) {
    OperaOptions operaOptions = new OperaOptions();
    operaOptions.addArguments("--no-sandbox");
    operaOptions.addArguments("--no-first-run");
    operaOptions.addArguments("--enable-extensions");
    operaOptions.addArguments("--homepage=about:blank");
    operaOptions.addArguments("--ignore-certificate-errors");
    operaOptions.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
    operaOptions.setCapability(CapabilityType.VERSION, System.getProperty("browser.version", "105.0"));
    operaOptions.setCapability(CapabilityType.BROWSER_NAME, System.getProperty("browser", "opera"));
    operaOptions.setCapability("enableVNC", Boolean.parseBoolean(System.getProperty("enableVNC", "true")));
    //video recording settings:
    operaOptions.setCapability("screenResolution", "1280x1024");
    operaOptions.setCapability("enableVideo", true);
    operaOptions.setCapability("videoScreenSize", "1280x1024");
    operaOptions.setCapability("videoFrameRate", 12);
    //operaOptions.setCapability("videoName", allure.getCurrentVideoName(extensionContext)); @TODO понять почему здесь не работает строка allure.getCurrentVideoName(extensionContext)
    operaOptions.setCapability("videoName", extensionContext.getTestMethod().get().getName()
            .concat("-")
            .concat(System.getProperty("browser"))
            .concat("-")
            .concat(System.getProperty("browser.version"))
            .concat(".mp4"));

    if (getRemoteUrl() == null) {
      try {
        System.setProperty("webdriver.opera.driver", "/usr/bin/opera");
        downloadLocalWebDriver(DriverManagerType.OPERA);
      } catch (BrowserNotSupportedException ex) {
        ex.printStackTrace();
      }

      return new OperaDriver(operaOptions);
    } else
      return new RemoteWebDriver(getRemoteUrl(), operaOptions);
  }
}
