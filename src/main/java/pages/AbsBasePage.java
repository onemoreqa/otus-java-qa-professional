package pages;

import static com.codeborne.selenide.Selenide.$;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class AbsBasePage<T> {

    public T open() {
        Selenide.open();
        return  (T) this;
    }

    public T isPresent(String text) {
        $(String.format("[text='%s']", text)).should(Condition.visible);
        return (T) this;
    }

    public T click(String text) {
        $(String.format("[text='%s']", text)).should(Condition.visible).click();
        return (T) this;
    }

    public T clickAll(String...args) {
        Stream.of(args).map(el -> click(el)).collect(Collectors.toList());
        return (T) this;
    }

}
