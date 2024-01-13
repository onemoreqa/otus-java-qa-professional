package otus.sliders;

import anotations.Driver;
import extensions.UIExtensions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebDriver;
import pages.MainPage;

@ExtendWith(UIExtensions.class)
public class CompaniesSlider_Test {

  @Driver
  private WebDriver driver;

  @Test
  public void openPage_Test(){
    MainPage mainPage = new MainPage(driver);
    mainPage.open();
  }
}
