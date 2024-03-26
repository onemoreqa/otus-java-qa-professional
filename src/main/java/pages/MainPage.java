package pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.google.inject.Inject;

import static com.codeborne.selenide.Selenide.$;

public class MainPage extends AbsBasePage<MainPage> {

    @Inject
    private ChatPage chatPage;
    private SelenideElement nextButton = $("[text='next']");


    public MainPage clickNextButton() {
        nextButton.should(Condition.visible).click();
        return this;
    }

    public ChatPage clickChatbutton() {
        $("[text='chat']").shouldBe(Condition.visible).click();

        // return new ChatPage();
        // для слабо-связанного кода не хорошо писать return new ChatPage();
        // Если меняется процедура создания ChatPage, потребуется найти все методы интерфейса
        // где создается обьект этой страницы

        return chatPage;
        // Получается, что после инжекта, сам обьект chatPage будет создан
        // selenide элементы страницы chatPage будут проинициализированы, но поиск эл-та инициирован не будет
        // можно безопасо инжектить, в чистом селениуме так не получилось бы
        // при статическом локаторе инициировался бы поиск элемента

        // в чистом selenium решение:
        // private Injector injector;
        // Injector = Guice.createInjector(new GuicePagesModule());
        // return injector.getProvider(ChatPageee.class).get();
        //


    }

}
