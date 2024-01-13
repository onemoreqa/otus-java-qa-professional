package factories.impl;

import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.chrome.ChromeOptions;

public class ChromeConfigure implements IBrowserSettings {

  @Override
  public MutableCapabilities conigure() {
    ChromeOptions chromeOptions = new ChromeOptions();
    chromeOptions.addArguments("--lang=ru");
    chromeOptions.addArguments("--no-sandbox");
    chromeOptions.addArguments("--remote-allow-origins=*");
    chromeOptions.addArguments("start-maximized");
    chromeOptions.addArguments("--no-first-run");
    chromeOptions.addArguments("--enable-extensions");
    chromeOptions.addArguments("--homepage=about:blank");
    chromeOptions.addArguments("--ignore-certificate-errors");

    return chromeOptions;
  }
}
