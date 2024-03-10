package otus.sliders;

import anotations.Driver;
import components.CoursesComponent;
import components.FavoriteCoursesComponent;
import components.SpecializationsComponent;
import extensions.UIExtensions;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Link;
import io.qameta.allure.Severity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.openqa.selenium.WebDriver;
import pages.MainPage;

import static io.qameta.allure.SeverityLevel.MINOR;

@ExtendWith(UIExtensions.class)
@Execution(ExecutionMode.CONCURRENT)
public class SearchCourse_Test {

  @Driver
  private WebDriver driver;

  @Test
  @Feature("Поиск курса")
  @DisplayName("Поиск курса по названию")
  @Description("Тест поиск курса по названию")
  @Severity(MINOR)
  @Link(name = "Video", url = "http://0.0.0.0:8080/video/searchOtusCourseByTitle.mp4")
  public void searchOtusCourseByTitle() {
    MainPage mainPage = new MainPage(driver);
    mainPage.open();
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
