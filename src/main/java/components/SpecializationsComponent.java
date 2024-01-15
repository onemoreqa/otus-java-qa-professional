package components;

import data.CourseEntry;
import data.CourseSourceData;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import pages.CoursePage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

public class SpecializationsComponent extends CoursesComponent<SpecializationsComponent> {

  @FindBy(xpath = "//section/*[text()='Специализации']/following-sibling::div/div")
  private List<WebElement> specializations;

  @FindBy(xpath = "//section/*[text()='Специализации']")
  private WebElement specializationsSection;

  public SpecializationsComponent(WebDriver driver) {
    super(driver);
  }

  /**
   * Получение названий курсов по рекомендациям
   */
  public List<String> getSpecializationCourses() {
    return getTitleCourses(specializations);
  }

  /**
   * Получение списка CourseEntry, где будет храниться основная информация по карточке курса/специализации
   */
  public List<CourseEntry> getCourseEntries(List<WebElement> coursesElements) {
    return coursesElements.stream()
            .map(webElement -> {
              CourseEntry courseEntry = new CourseEntry();
              courseEntry.setCourseTypeData(CourseSourceData.SPECIALIZATION);
              courseEntry.setTitle(webElement.findElement(By.xpath(".//img")).getAttribute("alt"));
              String dateInfo = webElement.findElement(By.xpath(".//div[@title='" + courseEntry.getTitle()
                      + "']/following-sibling::div/span")).getText();
              // Очищаем от лишней информации, чтобы оставить только дату, чтобы потом перевести в объект LocalDate
              String dateBegin = dateInfo.replaceAll("^С ", "")
                      .replaceAll("\\d+ (месяцев|месяца)$", "")
                      .trim()
                      .replaceAll("года$", "")
                      .trim();
              // Чтобы паттерн отработал нужно добавить год, там где он не указан (текущий год не пишется)
              if (!dateBegin.matches(".*\\d{4}$")) {
                dateBegin = dateBegin + " " + LocalDate.now().getYear();
              }
              DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy").withLocale(new Locale("ru", "RU"));
              courseEntry.setBeginDate(LocalDate.parse(dateBegin, formatter));

              return courseEntry;
            })
            .collect(Collectors.toList());
  }

  public void chooseLastCourseByDate() {
    chooseCourseByDate((courseEntry, courseEntry2) -> courseEntry.getBeginDate().isAfter(courseEntry2.getBeginDate()) ? courseEntry : courseEntry2);
  }

  public void chooseFirstCourseByDate() {
    chooseCourseByDate((courseEntry, courseEntry2) -> courseEntry.getBeginDate().isBefore(courseEntry2.getBeginDate()) ? courseEntry : courseEntry2);
  }

  /**
   * выбор курса на странице. Курс выбирается из множества с помощью reduce и BinaryOperator<CourseEntry> на вход ему
   */
  public void chooseCourseByDate(BinaryOperator<CourseEntry> courseEntryReduceBinaryOperator) {
    List<CourseEntry> courseEntries = getCourseEntries(specializations);
    Assertions.assertFalse(courseEntries.isEmpty(), "Не найдена ни одна специализация на странице");

    //Выше сделана проверка на пустоту списка, поэтому возвращаем в orElse первый элемент из списка
    CourseEntry firstCourseEntry = courseEntries.stream()
            .reduce(courseEntryReduceBinaryOperator)
            .orElse(courseEntries.get(0));
    chooseSpecialization(firstCourseEntry.getTitle());
  }

  public CoursePage chooseSpecialization(String title) {
    WebElement courseElement = specializationsSection.findElement(By.xpath("./following-sibling::div//*[text()='"
            + title + "']"));
    actions.moveToElement(courseElement).build().perform();
    courseElement.click();
    return new CoursePage(driver);
  }
}
