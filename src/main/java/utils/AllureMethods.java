package utils;

import io.qameta.allure.Allure;
import lombok.SneakyThrows;
import org.junit.jupiter.api.TestInfo;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.ByteArrayInputStream;

public class AllureMethods {

  private final Object driver;

  public AllureMethods(WebDriver driver) {
    this.driver = driver;
  }

  public void createScreenshot(String screenDescription) {

    Allure.addAttachment(screenDescription,
            new ByteArrayInputStream(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES)));
  }

}
