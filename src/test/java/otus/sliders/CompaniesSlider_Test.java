package otus.sliders;

import anotations.Driver;
import anotations.Page;
import extensions.UIExtensions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebDriver;
import pages.MainPage;

@ExtendWith(UIExtensions.class)
public class CompaniesSlider_Test {

  @Driver
  private WebDriver driver;

  @Page
  private MainPage mainPage;

/*  @Page
  private MainPage mainPage; @TODO надо реализовать по аналогии с вэбДрайвером */

  @Test
  public void testSliderCompanies() {
    mainPage
      .open();
  }
}
