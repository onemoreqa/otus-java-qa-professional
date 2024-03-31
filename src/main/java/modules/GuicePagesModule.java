package modules;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import pages.ChatPage;
import pages.ExercisePage;
import pages.WelcomePage;

public class GuicePagesModule extends AbstractModule {

    @Provides
    @Singleton //для параллелизации по сьютам
    public WelcomePage getWelcomePage() {
        return new WelcomePage();
    }

    @Provides
    @Singleton
    public ChatPage getChatPage() {
        return new ChatPage();
    }

    @Provides
    @Singleton
    public ExercisePage getExercisePage() {
        return new ExercisePage();
    }

}
