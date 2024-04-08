package factories;

import exceptions.BrowserNotSupportedException;
import org.openqa.selenium.WebDriver;

public interface IDriverFactory {
  WebDriver getDriver() throws BrowserNotSupportedException;
}
