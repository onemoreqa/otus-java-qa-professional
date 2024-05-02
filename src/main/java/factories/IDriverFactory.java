package factories;

import exceptions.BrowserNotSupportedException;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.openqa.selenium.WebDriver;

public interface IDriverFactory {
  WebDriver getDriver(ExtensionContext extensionContext) throws BrowserNotSupportedException;
}
