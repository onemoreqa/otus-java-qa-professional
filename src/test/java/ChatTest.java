import com.google.inject.Inject;
import extensions.AndroidExtension;
import modules.GuicePagesModule;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import pages.MainPage;

@ExtendWith(AndroidExtension.class)
public class ChatTest {

    @Inject
    MainPage mainPage = new GuicePagesModule().getMainPage();

    @Test
    public void checkChatIsWorking() {
        mainPage.goToMainPage()
               .sendMessage("I found a bug!")
               .checkStatement("I will speak English now. I hope it’s okay, we learn English here after all")
               .sendMessage("ahaha");
    }

}
