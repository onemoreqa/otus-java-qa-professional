package otus.sliders;

import anotations.Driver;
import components.SpecializationsComponent;
import extensions.UIExtensions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebDriver;
import pages.MainPage;

@ExtendWith(UIExtensions.class)
public class SearchCourse_Test {

  @Driver
  private WebDriver driver;

  @Test
  //Д/З №1 поиск курса среди всех на странице
  public void searchOtusCourseByTitle() {
    MainPage mainPage = new MainPage(driver);
    mainPage.open();
    mainPage.searchCourseByTitle("MLOps");
  }

  @Test
  //Д/З №1 поиск самого позднего курса среди специализаций
  public void searchLastOtusSpecializationByDate() {
    MainPage mainPage = new MainPage(driver);
    mainPage.open();
    new SpecializationsComponent(driver).chooseLastCourseByDate();
  }

  @Test
  //Д/З №1 поиск самого позднего курса среди специализаций
  public void searchFirstOtusSpecializationByDate() {
    MainPage mainPage = new MainPage(driver);
    mainPage.open();
    new SpecializationsComponent(driver).chooseFirstCourseByDate();
  }

}
