package pages;

import com.google.inject.Inject;
import data.WelcomePageItems;
import modules.GuicePagesModule;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WelcomePage extends AbsBasePage<WelcomePage> {

    @Inject
    ChatPage chatPage = new GuicePagesModule().getChatPage();

    {
        super.open();
    }

    /**
     * Мы могли бы вернуть new ChatPage(), но для слабо-связанного кода это не годится
     * Почему: если меняется процедура создания ChatPage, потребуется найти все методы интерфейса,
     * где создается обьект этой страницы.
     * После инжекта, сам обьект chatPage будет создан. Selenide элементы страницы chatPage будут проинициализированы,
     * но поиск эл-та инициирован не будет, потому можно безопасо инжектить.
     *
     * В чистом селениуме так не получилось бы, т.к. при статическом локаторе инициировался бы поиск элемента.
     * решение в чистом selenium:
     *
     * private Injector injector;
     * Injector = Guice.createInjector(new GuicePagesModule());
     * return injector.getProvider(ChatPageee.class).get();
     *
     */
    public ChatPage goToMainPage() {
        Stream.of(WelcomePageItems.values())
            .map(items -> {
                isPresent(items.getPageTitle());
                click(items.getButtonName());
                return this;
            })
            .collect(Collectors.toList());

        // 2-ой вариант, через перебор enum
        //  for (WelcomePageItems step : WelcomePageItems.values()) {
        //      isPresent(step.getPageTitle());
        //      click(step.getButtonName());
        //  }
        // 1-ый вариант
        // clickAll("Next", "Next", "Skip >", "OK")
        return chatPage;
    }

    @Override
    public WelcomePage click(String text) {
        super.click(text);
        return this;
    }

}
