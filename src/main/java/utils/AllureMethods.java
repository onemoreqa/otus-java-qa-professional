package utils;

import io.qameta.allure.Allure;
import io.qameta.allure.model.Link;
import org.bouncycastle.oer.its.etsi102941.Url;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.ByteArrayInputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class AllureMethods {

  private final Object driver;

  public AllureMethods(WebDriver driver) {
    this.driver = driver;
  }

  public void createScreenshot(String screenDescription) {

    Allure.addAttachment(screenDescription,
            new ByteArrayInputStream(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES)));
  }

  public void addVideo(ExtensionContext extensionContext) {
    Allure.addAttachment("VIDEO", "text/plain", getCurrentVideoLink(extensionContext).getUrl());
    //Allure.link(getCurrentVideoLink(extensionContext).getUrl());
    //Allure.addLinks(getCurrentVideoLink(extensionContext));
  }

  public Link getCurrentVideoLink(ExtensionContext extensionContext) {
    return new Link().setUrl("https://onqa.su/video/".concat(getCurrentVideoName(extensionContext)));
  }

  public String getCurrentVideoName(ExtensionContext extensionContext) {
    String newUrl = ""
            .concat(System.getProperty("browser"))
            .concat("-")
            .concat(System.getProperty("browser.version"))
            .concat("-")
            .concat(getCurrentTestMethod(extensionContext))
            .concat(".mp4");
    return newUrl;
  }

  public String getCurrentTestMethod(ExtensionContext extensionContext) {
    return extensionContext.getTestMethod().get().getName();
  }

}
