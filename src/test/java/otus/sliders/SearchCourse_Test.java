package otus.sliders;

import anotations.Driver;
import components.CoursesComponent;
import components.FavoriteCoursesComponent;
import components.SpecializationsComponent;
import extensions.UIExtensions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.openqa.selenium.WebDriver;
import pages.MainPage;

@ExtendWith(UIExtensions.class)
@Execution(ExecutionMode.CONCURRENT)
public class SearchCourse_Test {

  @Driver
  private WebDriver driver;

  @Test
  public void searchOtusCourseByTitle() {
    MainPage mainPage = new MainPage(driver);
    mainPage.open();
    //mainPage.getAllCourses();
    new FavoriteCoursesComponent(driver).chooseCourse("ML");
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
