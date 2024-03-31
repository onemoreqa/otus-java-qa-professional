package pages;

import com.google.inject.Inject;
import components.MainMenuComponent;
import data.MenuSections;
import modules.GuiceComponentsModule;

public class ExercisePage extends AbsBasePage<ExercisePage> {

    @Inject
    MainMenuComponent mainMenuComponent = new GuiceComponentsModule().getMainMenuComponent();

    @Inject
    ExercisePage exercisePage;

    //@Inject
    //ChatPage chatPage;

    public ChatPage clickStart() {
        mainMenuComponent.mainMenuItemVisible(MenuSections.EXERCISE);
        click("Start");
        return new ChatPage();
    }

}
