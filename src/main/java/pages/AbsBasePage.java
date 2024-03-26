package pages;

import com.codeborne.selenide.Selenide;

public abstract class AbsBasePage<T> {

    public T open() {
        Selenide.open();

        return  (T)this;
    }

}
