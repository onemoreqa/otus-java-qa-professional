package providers;

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

    @Nonnull
    @Override
    public WebDriver createDriver(@Nonnull Capabilities capabilities) {

        Duration duration = Duration.ofMinutes(10);
        UiAutomator2Options options = new UiAutomator2Options();
        options.merge(capabilities);

        options.setAutomationName(AutomationName.ANDROID_UIAUTOMATOR2);
        options.setPlatformName(System.getProperty("platform.name"));
        options.setDeviceName(System.getProperty("device.name"));
        options.setPlatformVersion(System.getProperty("platform.version"));
        options.setAvd(System.getProperty("avd.name"));

        if (!System.getProperty("remote.url").equals("http://0.0.0.0:4723/wd/hub")) {
            options.setApp(System.getProperty("apk.path"));
            //options.setAvd("android8.1-1");
        } else {
            options.setAvd(System.getProperty("avd.name"));
            options.setAppPackage(System.getProperty("app.package"));
            options.setAppActivity(System.getProperty("app.activity"));
        }

        options.setAdbExecTimeout(duration);
        options.setAndroidInstallTimeout(duration);
        options.setAvdLaunchTimeout(duration);
        options.setAvdReadyTimeout(duration);
        options.setAutoWebviewTimeout(duration);
        options.setNewCommandTimeout(duration);
        options.setUnlockSuccessTimeout(duration);
        options.setUiautomator2ServerInstallTimeout(duration);
        options.setUiautomator2ServerLaunchTimeout(duration);
        options.setUiautomator2ServerReadTimeout(duration);

        try {
            return new AndroidDriver(new URL(System.getProperty("remote.url")), options);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
}
