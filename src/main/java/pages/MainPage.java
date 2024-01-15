package pages;

import components.FavoriteCoursesComponent;
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

  public List<String> getAllCourses() {
    List<String> allCourses = new ArrayList<>(new FavoriteCoursesComponent(driver).getTitleCourses());
    allCourses.addAll(new SpecializationsComponent(driver).getSpecializationCourses());

    Assertions.assertFalse(allCourses.isEmpty(), "Не найдена ни один курс на странице");

    System.out.println(allCourses.stream().map(Object::toString).collect(Collectors.joining(", ")));

    return allCourses;
  }

}
