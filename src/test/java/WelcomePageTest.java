import com.google.inject.Inject;
import extensions.AndroidExtension;
import modules.GuicePagesModule;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import pages.WelcomePage;

@ExtendWith(AndroidExtension.class)
public class WelcomePageTest {

    @Inject
    WelcomePage welcomePage = new GuicePagesModule().getWelcomePage();

    @Test
    public void navigationMainPageWidget() {
        welcomePage.goToMainPage();
    }

}
