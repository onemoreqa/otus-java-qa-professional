package factories;

import exceptions.BrowserNotSupportedException;
import factories.impl.RemoteChromeWebDriver;
import factories.impl.RemoteOperaWebDriver;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.openqa.selenium.support.events.EventFiringWebDriver;

import java.util.Locale;

public class DriverFactory implements IDriverFactory {

  private String browserType = System.getProperty("browser").toLowerCase(Locale.ROOT);

  @Override
  public EventFiringWebDriver getDriver(ExtensionContext extensionContext) {
    switch (this.browserType) {
      case "chrome": {
        return new EventFiringWebDriver(new RemoteChromeWebDriver().newDriver(extensionContext));
      }
      case "opera": {
        return new EventFiringWebDriver(new RemoteOperaWebDriver().newDriver(extensionContext));
      }
      default:
        try {
          throw new BrowserNotSupportedException(this.browserType);
        } catch (BrowserNotSupportedException ex) {
          ex.printStackTrace();
          return null;
        }
    }
  }
}
