package extensions;

import static com.github.automatedowl.tools.AllureEnvironmentWriter.allureEnvironmentWriter;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.junit5.ScreenShooterExtension;
import com.google.common.collect.ImmutableMap;
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
        allureEnvironmentWriter(
                ImmutableMap.<String, String>builder()
                        .put("REMOTE_URL", System.getProperty("remote.url"))
                        .put("PLATFORM_NAME", System.getProperty("platform.name"))
                        .put("PLATFORM_VERSION", System.getProperty("platfrom.version"))
                        .put("DEVICE_NAME", System.getProperty("device.name"))
                        .put("AVD_NAME", System.getProperty("avd.name"))
                        .put("APP_PACKAGE", System.getProperty("app.package"))
                        .put("APP_ACTIVITY", System.getProperty("app.activity"))
                        .put("APP_PATH", System.getProperty("apk.path"))
                        .build(), System.getProperty("user.dir")
                        + "/allure-results/");

        //allure.createScreenshot("Final screenshot");
        Selenide.closeWebDriver();
    }
}
