package pages;

import components.FavoriteCoursesComponent;
import components.RecommendationsComponent;
import components.SpecializationsComponent;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.WebDriver;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MainPage extends AbsBasePage<MainPage> {

  public MainPage(WebDriver driver) {
    super(driver);
  }

  public void searchCourseByTitle(String expectedCourseTitle) {
    List<String> allCourses = getAllCourses();
    Assertions.assertFalse(allCourses.isEmpty(), "Не найдена ни один курс на странице");
    System.out.println("Поиск курса=" + expectedCourseTitle + ":");

    String allCoursesString = allCourses.stream().map(Object::toString).collect(Collectors.joining(", "));
    System.out.println(allCoursesString);

    Assertions.assertTrue(allCoursesString.contains(expectedCourseTitle), "Нет ни одного подходящего курса");
  }

  public List<String> getAllCourses() {
    List<String> allCourses = new ArrayList<>(new FavoriteCoursesComponent(driver).getTitleCourses());
    allCourses.addAll(new RecommendationsComponent(driver).getTitleCoursesByRecommendations());
    allCourses.addAll(new SpecializationsComponent(driver).getSpecializationCourses());
    return allCourses;
  }

}
