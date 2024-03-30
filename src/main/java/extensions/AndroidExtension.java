package extensions;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.google.inject.Guice;
import modules.GuiceComponentsModule;
import modules.GuicePagesModule;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import providers.AndroidWebDriverProvider;

public class AndroidExtension implements BeforeAllCallback, AfterAllCallback {

    @Override
    public void beforeAll(ExtensionContext extensionContext) {

        Guice.createInjector(new GuicePagesModule(), new GuiceComponentsModule());

        Configuration.browserSize = null;
        Configuration.browser = AndroidWebDriverProvider.class.getName();
    }

    @Override
    public void afterAll(ExtensionContext extensionContext) {
        Selenide.closeWebDriver();
    }
}
