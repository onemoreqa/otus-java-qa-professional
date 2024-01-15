package components;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class RecommendationsComponent extends CoursesComponent<RecommendationsComponent> {

  private List<WebElement> recommendations;
  public RecommendationsComponent(WebDriver driver) {
    super(driver);
  }

  /**
   * Получение названий курсов по рекомендациям
   */
  public List<String> getTitleCoursesByRecommendations() {
    return getTitleCourses(recommendations);
  }

}
