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
public class ExerciseTest {

    @Inject
    WelcomePage welcomePage = new GuicePagesModule().getWelcomePage();

    //@Test
    @Disabled("временно выключен")
    @DisplayName("Проверка старта Упражнений")
    public void checkExercisePage() {
        welcomePage.goToMainPage()
            .navigateToExercisePage()
            .isPresent("Learn 5 new words today")
            .clickStart()
            .sendMessage("Welcome to QA");
    }

}
