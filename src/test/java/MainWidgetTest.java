import com.google.inject.Inject;
import extensions.AndroidExtension;
import modules.GuicePagesModule;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import pages.MainPage;

@ExtendWith(AndroidExtension.class)
public class MainWidgetTest {

    @Inject
    MainPage mainPage = new GuicePagesModule().getMainPage();
    //
    //    @Inject
    //    private ChatWindowComponent chatWindowComponent;

    @Test
    public void navigationMainPageWidget() {
        mainPage.goToMainPage();
    }

    @Test
    public void checkSections() {
        new MainPage().open()
                .goToMainPage();
    }

}
