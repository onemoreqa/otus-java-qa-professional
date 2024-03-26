import com.google.inject.Inject;
import components.ChatWindowComponent;
import extensions.AndroidExtension;
import jdk.tools.jmod.Main;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import pages.MainPage;

@ExtendWith(AndroidExtension.class)
public class MainWidget_Test {

    @Inject
    private MainPage mainPage;

    @Inject
    private ChatWindowComponent chatWindowComponent;

    @Test
    public void navigationMainPageWidget() {
        mainPage.open()
                .clickNextButton()
                .clickChatbutton();

        chatWindowComponent.getComponentEntity().click();
    }

}
