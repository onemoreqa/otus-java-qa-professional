package modules;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import pages.ChatPage;
import pages.MainPage;

public class GuicePagesModule extends AbstractModule {


    @Provides
    @Singleton //для параллелизации по сьютам
    public MainPage getMainPage() {
        return new MainPage();
    }

    @Provides
    @Singleton //для параллелизации по сьютам
    public ChatPage getChatPage() {
        return new ChatPage();
    }

}
