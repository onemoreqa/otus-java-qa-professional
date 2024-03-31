package pages;

import com.google.inject.Inject;
import components.ChatWindowComponent;
import components.MainMenuComponent;
import data.MenuSections;
import modules.GuiceComponentsModule;
import modules.GuicePagesModule;

public class ChatPage extends AbsBasePage<ChatPage> {

    @Inject
    MainMenuComponent mainMenuComponent = new GuiceComponentsModule().getMainMenuComponent();

    @Inject
    ChatWindowComponent chatWindowComponent = new GuiceComponentsModule().getChatWindowComponent();

    @Inject
    ExercisePage exercisePage = new GuicePagesModule().getExercisePage();

    public ChatPage sendMessage(String text) {
        chatWindowComponent.sendMessage(text);
        return this;
    }

    public ChatPage checkStatement(String statement) {
        chatWindowComponent.checkStatement(statement);
        return this;
    }

    public ExercisePage navigateToExercisePage() {
        mainMenuComponent.select(MenuSections.EXERCISE);
        return exercisePage;
    }


}
