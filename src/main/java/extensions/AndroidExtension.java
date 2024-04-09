package extensions;

import static com.codeborne.selenide.Selenide.screenshot;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.junit5.ScreenShooterExtension;
import com.codeborne.selenide.logevents.SelenideLogger;
import com.google.inject.Guice;
import modules.GuiceComponentsModule;
import modules.GuicePagesModule;
import org.junit.jupiter.api.extension.*;
import providers.AndroidWebDriverProvider;
import utils.AllureMethods;

@ExtendWith({ScreenShooterExtension.class})
public class AndroidExtension implements BeforeAllCallback, AfterAllCallback {

    public AllureMethods allure;

    @Override
    public void beforeAll(ExtensionContext extensionContext) {

        Guice.createInjector(new GuicePagesModule(), new GuiceComponentsModule());

        Configuration.browserSize = null;
        Configuration.browser = AndroidWebDriverProvider.class.getName();
        Configuration.timeout = 200000L;
        Configuration.remoteReadTimeout = 200000L;
        Configuration.screenshots = false;
        Configuration.reportsFolder = "allure-results";
    }

    @Override
    public void afterAll(ExtensionContext extensionContext) {
        //screenshot(extensionContext.getDisplayName());
        //allure.createScreenshot();
        Selenide.closeWebDriver();
    }
}
