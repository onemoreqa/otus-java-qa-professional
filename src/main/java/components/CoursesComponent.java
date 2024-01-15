package components;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;
import java.util.stream.Collectors;

public abstract class CoursesComponent<T> extends BaseComponent<CoursesComponent> {
  public CoursesComponent(WebDriver driver) {
    super(driver);
  }

  @FindBy(xpath = "//div/*[text()='Рекомендации для вас']/following-sibling::div/div")
  private List<WebElement> recommendations;

  public List<String> getTitleCourses(List<WebElement> coursesElements) {
    return coursesElements.stream()
            .map(webElement -> webElement.findElement(By.xpath(".//img")).getAttribute("alt"))
            .collect(Collectors.toList());
  }

  public List<String> getTitleCoursesByRecommendations() {
    return getTitleCourses(recommendations);
  }
}
