import com.google.inject.Inject;
import extensions.AndroidExtension;
import modules.GuicePagesModule;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import pages.WelcomePage;

@ExtendWith(AndroidExtension.class)
public class ChatTest {

    @Inject
    WelcomePage welcomePage = new GuicePagesModule().getWelcomePage();

    @Test
    public void checkChatIsWorking() {
        welcomePage.goToMainPage()
               .sendMessage("I found a bug!")
               .checkStatement("I will speak English now. I hope it’s okay, we learn English here after all")
               .sendMessage("ahaha");
    }

}
