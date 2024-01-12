package factories;

import exceptions.BrowserNotSupportedException;
import factories.impl.ChromeConfigure;
import org.openqa.selenium.support.events.EventFiringWebDriver;

public class WebDriverFactory implements IFactory<EventFiringWebDriver>{

  private String browserName = System.getProperty("browser");
  @Override
  public EventFiringWebDriver create() {
    switch (browserName) {
      case "chrome": {
        return new EventFiringWebDriver(new ChromeConfigure().conigure());
      }
      default: {
        throw new BrowserNotSupportedException(browserName);
      }
    }
  }
}
