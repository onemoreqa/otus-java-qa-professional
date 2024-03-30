package pages;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

import com.codeborne.selenide.Condition;

import java.util.Arrays;
import java.util.stream.Stream;

public class ChatPage extends AbsBasePage<ChatPage> {

    private String defaultMessage = "[text='Type a message...']";

    public ChatPage sendMessage(String text) {
        $(defaultMessage).shouldBe(Condition.visible);
        $(defaultMessage).sendKeys(text);
        isPresent(text)
            .click("Send")
            .isPresent(text);
        return this;
    }

    public ChatPage checkResponse(String expectedResponse) {
        $(defaultMessage).shouldBe(Condition.visible);
        System.out.println($x("//android.widget.TextView[@text='" + expectedResponse + "']").shouldBe(Condition.visible).text());
        return this;
    }

    public ChatPage checkStatement(String statement) {
        String[] words = statement.split(" ");
        System.out.println(Arrays.toString(words));

        Stream.of(words).forEach(i -> checkResponse(i));
        return this;
    }


}
