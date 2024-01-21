package ui;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebDriver;
import ui.anotations.Driver;
import ui.components.FavoriteCoursesComponent;
import ui.components.SpecializationsComponent;
import ui.extensions.UIExtensions;
import ui.pages.MainPage;

@Tag("ui")
@ExtendWith(UIExtensions.class)
public class SearchCourse_Test {

  @Driver
  private WebDriver driver;

  @Test
  public void searchOtusCourseByTitle() {
    MainPage mainPage = new MainPage(driver);
    mainPage.open();
    //mainPage.getAllCourses();
    new FavoriteCoursesComponent(driver).chooseCourse("ev");
  }

  @Test
  public void searchLastOtusSpecializationByDate() {
    MainPage mainPage = new MainPage(driver);
    mainPage.open();
    new SpecializationsComponent(driver).chooseLastCourseByDate();
  }

  @Test
  public void searchFirstOtusSpecializationByDate() {
    MainPage mainPage = new MainPage(driver);
    mainPage.open();
    new SpecializationsComponent(driver).chooseFirstCourseByDate();
  }

}
