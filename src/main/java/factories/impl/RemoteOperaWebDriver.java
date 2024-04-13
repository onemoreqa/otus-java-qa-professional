package factories.impl;

import exceptions.BrowserNotSupportedException;
import io.github.bonigarcia.wdm.config.DriverManagerType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.opera.OperaOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.RemoteWebDriver;

public class RemoteOperaWebDriver implements IDriver {

  @Override
  public WebDriver newDriver() {
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
