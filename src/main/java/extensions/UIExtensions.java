package extensions;

import anotations.*;
import factories.DriverFactory;
import factories.WebDriverFactory;
import listeners.WebDriverListener;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.openqa.selenium.support.events.WebDriverEventListener;
import utils.AllureMethods;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class UIExtensions implements BeforeEachCallback, AfterEachCallback {

  //private WebDriver driver = null;
  private EventFiringWebDriver driver = null;
  public AllureMethods allure;

  @Override
  public void beforeEach(ExtensionContext extensionContext) {
    driver = new DriverFactory().getDriver();
    driver.register(new WebDriverListener());
    allure = new AllureMethods(driver);

    List<Field> fields = getAnnotatedFields(Driver.class, extensionContext);
    for (Field field : fields) {
      if (field.getType().getName().equals(WebDriver.class.getName())) {
        try {
          field.setAccessible(true);
          field.set(extensionContext.getTestInstance().get(), driver);
        } catch (IllegalAccessException e) {
          throw new Error(String.format("Could not access or set webdriver in field: %s - is this field public?", field), e);
        }
      }
    }
  }

  private List<Field> getAnnotatedFields(Class<? extends Annotation> annotation, ExtensionContext extensionContext) {
    List<Field> list = new ArrayList<>();
    Class<?> testClass = extensionContext.getTestClass().get();
    while (testClass != null) {
      for (Field field : testClass.getDeclaredFields()) {
        if (field.isAnnotationPresent(annotation)) {
          list.add(field);
        }
      }
      testClass = testClass.getSuperclass();
    }
    return list;
  }

  @Override
  public void afterEach(ExtensionContext extensionContext) {
    allure.createScreenshot("Финальный скрин");
    if(driver != null) {
      driver.close();
      driver.quit();
    }
  }
}
