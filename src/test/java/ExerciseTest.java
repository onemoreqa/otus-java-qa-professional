import com.google.inject.Inject;
import extensions.AndroidExtension;
import modules.GuicePagesModule;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import pages.WelcomePage;

@ExtendWith(AndroidExtension.class)
public class ExerciseTest {

    @Inject
    WelcomePage welcomePage = new GuicePagesModule().getWelcomePage();

    @Test
    public void checkExercisePage() {
        welcomePage.goToMainPage()
            .navigateToExercisePage()
            .isPresent("Learn 5 new words today")
            .clickStart()
            .sendMessage("Welcome to QA");
    }

}
