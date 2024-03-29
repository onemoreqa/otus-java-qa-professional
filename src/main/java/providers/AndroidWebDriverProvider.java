package providers;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverProvider;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.remote.AutomationName;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;

import javax.annotation.Nonnull;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

public class AndroidWebDriverProvider implements WebDriverProvider {
    private final long implicitlyWaitSecond = Integer.parseInt(System.getProperty("webdriver.timeouts.implicitlywait", "30"));


    @Nonnull
    @Override
    public WebDriver createDriver(@Nonnull Capabilities capabilities) {

        UiAutomator2Options options = new UiAutomator2Options();
        options.merge(capabilities);

        options.setAutomationName(AutomationName.ANDROID_UIAUTOMATOR2);
        options.setPlatformName(System.getProperty("platform.name"));
        options.setDeviceName(System.getProperty("device.name"));
        options.setPlatformVersion(System.getProperty("platform.version"));
        //options.setAvd("big_android_14.0");

        // если мы владеем *.apk и можем распаковать или узнали о разработке
        // с selenoid не работает. т.к. композ будет каждый раз удалять директорию
        options.setAppPackage("com.pyankoff.andy");
        options.setAppActivity(".MainActivity");
        //options.setApp("/home/ubuntu/Andy.apk");

        try {
            WebDriver driver = new AndroidDriver(new URL(System.getProperty("remote.url")), (Capabilities) options);
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitlyWaitSecond));

            //return new AndroidDriver(new URL(Configuration.remote), options);
            return driver;
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
}
