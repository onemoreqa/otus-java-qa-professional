import com.google.inject.Inject;
import extensions.AndroidExtension;
import io.qameta.allure.Owner;
import modules.GuicePagesModule;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import pages.WelcomePage;

@Owner("yigorbu4")
//@Execution(ExecutionMode.CONCURRENT)
@ExtendWith(AndroidExtension.class)
public class ChatTest {

    @Inject
    WelcomePage welcomePage = new GuicePagesModule().getWelcomePage();

    //@Test
    @DisplayName("Проверка первого ответа от чат-бота")
    public void checkChatIsWorking() {
        welcomePage.goToMainPage()
               .sendMessage("I found a bug!")
               .checkStatement("I will speak English now. I hope it’s okay, we learn English here after all")
               .sendMessage("ahaha");
    }

}
