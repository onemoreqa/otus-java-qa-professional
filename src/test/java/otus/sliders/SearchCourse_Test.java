package otus.sliders;

import anotations.Driver;
import components.CoursesComponent;
import components.FavoriteCoursesComponent;
import components.SpecializationsComponent;
import extensions.UIExtensions;
import io.qameta.allure.Owner;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.openqa.selenium.WebDriver;
import pages.MainPage;

@Owner("yigorbu4")
@Execution(ExecutionMode.CONCURRENT)
@ExtendWith(UIExtensions.class)
public class SearchCourse_Test {

  @Driver
  private WebDriver driver;

  @Test
  @DisplayName("Поиск курса по названию = ++")
  public void searchOtusCourseByTitle() {
    MainPage mainPage = new MainPage(driver);
    mainPage.open();
    //mainPage.getAllCourses();
    new FavoriteCoursesComponent(driver).chooseCourse("++");
  }

  @Test
  @DisplayName("Поиск самой поздней специализации")
  public void searchLastOtusSpecializationByDate() {
    MainPage mainPage = new MainPage(driver);
    mainPage.open();
    new SpecializationsComponent(driver).chooseLastCourseByDate();
  }

  @Test
  @DisplayName("Поиск самой ранней специализации")
  public void searchFirstOtusSpecializationByDate() {
    MainPage mainPage = new MainPage(driver);
    mainPage.open();
    new SpecializationsComponent(driver).chooseFirstCourseByDate();
  }

}
