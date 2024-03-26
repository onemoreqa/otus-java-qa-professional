import com.google.inject.Inject;
import components.ChatWindowComponent;
import extensions.AndroidExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import pages.MainPage;

@ExtendWith(AndroidExtension.class)
public class MainWidgetTest {

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
