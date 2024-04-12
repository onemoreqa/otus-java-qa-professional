package extensions;

import static com.github.automatedowl.tools.AllureEnvironmentWriter.allureEnvironmentWriter;

import anotations.*;
import com.google.common.collect.ImmutableMap;
import factories.DriverFactory;
import listeners.WebDriverListener;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import utils.AllureMethods;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class UIExtensions implements BeforeAllCallback, BeforeEachCallback, AfterEachCallback {

  //private WebDriver driver = null;
  private EventFiringWebDriver driver = null;
  public AllureMethods allure;

  @Override
  public void beforeAll(ExtensionContext extensionContext) throws Exception {
    allureEnvironmentWriter(
            ImmutableMap.<String, String>builder()
                    .put("BROWSER_NAME", System.getProperty("browser"))
                    .put("BROWSER_VERSION", System.getProperty("browser.version"))
                    .put("SELENOID_URL", System.getProperty("webdriver.remote.url"))
                    .put("JAVA_VERSION", System.getProperty("java.version"))
                    .put("BASE_URL", System.getProperty("base.url"))
                    .put("PARALLEL", System.getProperty("junit.jupiter.execution.parallel.config.fixed.parallelism"))
                    .build());
  }

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
