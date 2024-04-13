package factories;

import exceptions.BrowserNotSupportedException;
import factories.impl.RemoteChromeWebDriver;
import factories.impl.RemoteOperaWebDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;

import java.util.Locale;

public class DriverFactory implements IDriverFactory {

  private String browserType = System.getProperty("browser").toLowerCase(Locale.ROOT);

  @Override
  public EventFiringWebDriver getDriver() {
    switch (this.browserType) {
      case "chrome": {
        return new EventFiringWebDriver(new RemoteChromeWebDriver().newDriver());
      }
      case "opera": {
        return new EventFiringWebDriver(new RemoteOperaWebDriver().newDriver());
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
