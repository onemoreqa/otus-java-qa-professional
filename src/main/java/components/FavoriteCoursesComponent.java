package components;

import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import pages.CoursePage;

import java.util.List;

public class FavoriteCoursesComponent extends CoursesComponent<FavoriteCoursesComponent> {
  @FindBy(xpath = "//*[text()='Популярные курсы']/following-sibling::div/div")
  private List<WebElement> favoriteCourses;

  @FindBy(xpath = "//section/*[text()='Популярные курсы']")
  private WebElement favoriteCoursesSection;

  public FavoriteCoursesComponent(WebDriver driver) {
    super(driver);
    this.baseWaiter.waitForCondition(ExpectedConditions.visibilityOf(favoriteCoursesSection));
  }

  public List<String> getTitleCourses() {
    return getTitleCourses(favoriteCourses);
  }

  public void searchCourseByTitle(String expectedCourseTitle) {
    List<String> allCourses = getTitleCourses(favoriteCourses);
    Assertions.assertTrue(allCourses.contains(expectedCourseTitle), String.format("На странице не найден курс %s", expectedCourseTitle));
  }

  public CoursePage chooseCourse(String title) {
    WebElement courseElement = favoriteCoursesSection.findElement(By.xpath("./following-sibling::div//*[text()='"
            + title + "']"));
    actions.moveToElement(courseElement).build().perform();
    courseElement.click();
    return new CoursePage(driver);
  }

}
