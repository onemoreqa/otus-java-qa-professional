package components;

import com.google.inject.Inject;
import data.MenuSections;
import pages.AbsBasePage;
import pages.ChatPage;
import pages.ExercisePage;

public class MainMenuComponent extends AbsBasePage {

    @Inject
    ChatPage chatPage;

    @Inject
    ExercisePage exercisePage;

    public MainMenuComponent mainMenuItemVisible(MenuSections menuSections) {
        isPresent(menuSections.getName());
        return this;
    }

    public AbsBasePage select(MenuSections menuSections) {
        click(menuSections.getName());

        switch (menuSections) {
            case CHAT:
                return chatPage;
            case EXERCISE:
                return exercisePage;
            default:
                throw new RuntimeException(String.format("Unsupported Menu Item - %s", menuSections.getName()));
        }
    }
}
