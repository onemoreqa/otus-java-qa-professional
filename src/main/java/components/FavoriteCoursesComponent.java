package components;

import io.qameta.allure.Step;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import pages.CoursePage;

import java.util.List;

public class FavoriteCoursesComponent extends CoursesComponent<FavoriteCoursesComponent> {
  @FindBy(xpath = "(//h2 | //h3)/following-sibling::div/div")
  private List<WebElement> allCourses;

  private String courseSectionsLocator = "(//h2 | //h3)";

  @FindBy(xpath = "//section/*[text()='Популярные курсы']")
  private WebElement favoriteCoursesSection;

  public FavoriteCoursesComponent(WebDriver driver) {
    super(driver);
    this.baseWaiter.waitForCondition(ExpectedConditions.visibilityOf(favoriteCoursesSection));
  }

  public List<String> getTitleCourses() {
    return getTitleCourses(allCourses);
  }

  @Step("Выбор курса. Клик по курсу = {title}")
  public CoursePage chooseCourse(String title) {
    WebElement courseElement = driver.findElement(By.xpath(courseSectionsLocator + "/following-sibling::*//h5[contains(text(),'"
            + title + "')]"));
    actions.moveToElement(courseElement).build().perform();
    courseElement.click();
    return new CoursePage(driver);
  }

}
